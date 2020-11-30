package com.gmail.petrikov05.app.service.impl;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.petrikov05.app.repository.UserRepository;
import com.gmail.petrikov05.app.repository.model.User;
import com.gmail.petrikov05.app.repository.model.constant.UserRoleEnum;
import com.gmail.petrikov05.app.service.UserService;
import com.gmail.petrikov05.app.service.exception.AdministratorChangingException;
import com.gmail.petrikov05.app.service.exception.UserExistenceException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.user.AddUserDTO;
import com.gmail.petrikov05.app.service.model.user.LoginUserDTO;
import com.gmail.petrikov05.app.service.model.user.UserDTO;
import com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum;
import com.gmail.petrikov05.app.service.util.MailUtil;
import com.gmail.petrikov05.app.service.util.PageUtil;
import com.gmail.petrikov05.app.service.util.PasswordUtil;
import com.gmail.petrikov05.app.service.util.converter.UserConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.gmail.petrikov05.app.service.constant.MailMessage.MAIL_PASSWORD_MESSAGE;
import static com.gmail.petrikov05.app.service.constant.MailMessage.MAIL_SUBJECT;
import static com.gmail.petrikov05.app.service.constant.PageConstant.COUNT_OF_USER_BY_PAGE;
import static com.gmail.petrikov05.app.service.constant.UserConstant.EMAIL_SUPER_ADMIN;
import static com.gmail.petrikov05.app.service.util.PageUtil.getStartPosition;
import static com.gmail.petrikov05.app.service.util.converter.UserConverter.convertObjectToFullUserDTO;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailUtil mailUtil;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            MailUtil mailUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailUtil = mailUtil;
    }

    @Override
    @Transactional
    public LoginUserDTO getUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user != null) {
            logger.trace("user (" + user.getRole() + ") created");
            return UserConverter.convertObjectToLoginUserDTO(user);
        }
        logger.warn("User with email (" + email + ") not found");
        return null;
    }

    @Override
    @Transactional
    public PaginationWithEntitiesDTO<UserDTO> getUsersByPage(int page) {
        int startPosition = getStartPosition(page, COUNT_OF_USER_BY_PAGE);
        List<User> users = userRepository.getUsersByPage(startPosition, COUNT_OF_USER_BY_PAGE);
        List<UserDTO> userDTOS = users.stream()
                .map(UserConverter::convertObjectToFullUserDTO)
                .collect(Collectors.toList());
        PaginationWithEntitiesDTO<UserDTO> paginationWithEntitiesDTO = new PaginationWithEntitiesDTO<>();
        paginationWithEntitiesDTO.setEntities(userDTOS);
        int pages = getPages();
        paginationWithEntitiesDTO.setPages(pages);
        return paginationWithEntitiesDTO;
    }

    @Override
    @Transactional
    public List<String> deleteUsers(List<Long> ids) throws UserExistenceException, AdministratorChangingException {
        List<String> deletedEmails = new ArrayList<>();
        for (Long id : ids) {
            User user = userRepository.getUserById(id);
            checkUser(id, user);
            userRepository.delete(user);
            deletedEmails.add(user.getEmail());
        }
        return deletedEmails;
    }

    @Override
    @Transactional
    public UserDTO updateRole(Long id, UserRoleDTOEnum roleDTO) throws UserExistenceException, AdministratorChangingException {
        User user = userRepository.getUserById(id);
        checkUser(id, user);
        UserRoleEnum role = UserRoleEnum.valueOf(roleDTO.name());
        user.setRole(role);
        return convertObjectToFullUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO changePassword(Long id) throws UserExistenceException, AdministratorChangingException {
        User user = userRepository.getUserById(id);
        checkUser(id, user);
        String newPassword = PasswordUtil.generatePassword();
        String cryptPassword = passwordEncoder.encode(newPassword);
        user.setPassword(cryptPassword);
        userRepository.merge(user);
        mailUtil.sendMessage(user.getEmail(), MAIL_SUBJECT, MAIL_PASSWORD_MESSAGE + newPassword);
        return convertObjectToFullUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO addUser(AddUserDTO addUserDTO) throws UserExistenceException {
        User user = userRepository.getUserByEmail(addUserDTO.getEmail());
        if (user != null) {
            logger.info("attempt to add exist user");
            throw new UserExistenceException("attempt to add exist user");
        }
        User addUser = UserConverter.convertAddUserDTOToObject(addUserDTO);
        String password = PasswordUtil.generatePassword();
        String cryptPassword = passwordEncoder.encode(password);
        addUser.setPassword(cryptPassword);
        addUser.setIsDeleted(false);
        userRepository.persist(addUser);
        mailUtil.sendMessage(addUserDTO.getEmail(), MAIL_SUBJECT, MAIL_PASSWORD_MESSAGE + password);
        return convertObjectToFullUserDTO(addUser);
    }

    private void checkUser(Long id, User user) throws UserExistenceException, AdministratorChangingException {
        if (user == null) {
            logger.info("user with id = " + id + " not found");
            throw new UserExistenceException("user not found");
        }
        if (user.getEmail().equals(EMAIL_SUPER_ADMIN)) {
            logger.info("attempt update or delete super administrator");
            throw new AdministratorChangingException("action not successful");
        }
    }

    private int getPages() {
        Long countOfEntities = userRepository.getCountOfEntities();
        return PageUtil.getCountOfPage(countOfEntities, COUNT_OF_USER_BY_PAGE);
    }

}
