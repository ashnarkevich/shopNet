package com.gmail.petrikov05.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.gmail.petrikov05.app.repository.UserRepository;
import com.gmail.petrikov05.app.repository.model.User;
import com.gmail.petrikov05.app.repository.model.UserDetails;
import com.gmail.petrikov05.app.repository.model.UserInformation;
import com.gmail.petrikov05.app.repository.model.constant.UserRoleEnum;
import com.gmail.petrikov05.app.service.exception.AdministratorChangingException;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.exception.UserExistenceException;
import com.gmail.petrikov05.app.service.impl.UserServiceImpl;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.user.AddUserDTO;
import com.gmail.petrikov05.app.service.model.user.LoginUserDTO;
import com.gmail.petrikov05.app.service.model.user.UpdateUserProfileDTO;
import com.gmail.petrikov05.app.service.model.user.UserDTO;
import com.gmail.petrikov05.app.service.model.user.UserProfileDTO;
import com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum;
import com.gmail.petrikov05.app.service.util.MailUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_ADDRESS;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_COUNT_OF_ENTITIES;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_EMAIL;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_EMAIL_SUPER_ADMIN;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_IS_DELETED;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_OBJECT_BY_PAGE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PAGES;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PASSWORD;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PATRONYMIC;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_PHONE;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_START_POSITION;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_USER_ID;
import static com.gmail.petrikov05.app.service.constant.TestConstant.VALID_USER_ROLE;
import static com.gmail.petrikov05.app.service.constant.UserConstant.USER_ANONYMOUS;
import static com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum.CUSTOMER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MailUtil mailUtil;

    @BeforeEach
    public void setup() {
        this.userService = new UserServiceImpl(userRepository, passwordEncoder, mailUtil);
    }

    /* get user by email */
    @Test
    void getUserByEmail_returnUser() {
        User returnedUser = getValidUser();
        when(userRepository.getUserByEmail(VALID_EMAIL)).thenReturn(returnedUser);
        LoginUserDTO actualUserDTO = userService.getUserByEmail(VALID_EMAIL);
        assertThat(actualUserDTO).isNotNull();
        verify(userRepository, times(1)).getUserByEmail(VALID_EMAIL);
    }

    @Test
    void getUserInvalidEmail_returnNull() {
        when(userRepository.getUserByEmail(VALID_EMAIL)).thenReturn(null);
        LoginUserDTO actualUserDTO = userService.getUserByEmail(VALID_EMAIL);
        assertThat(actualUserDTO).isNull();
    }

    @Test
    void getUserByEmail_returnValidUser() {
        User returnedUser = getValidUser();
        when(userRepository.getUserByEmail(VALID_EMAIL)).thenReturn(returnedUser);
        User expectedUser = getValidUser();
        LoginUserDTO actualUserDTO = userService.getUserByEmail(VALID_EMAIL);
        assertThat(actualUserDTO.getUsername()).isEqualTo(VALID_EMAIL);
        assertThat(actualUserDTO.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(actualUserDTO.getRole().name()).isEqualTo(expectedUser.getRole().name());
    }

    /* get users by page with pagination */
    @Test
    void getUsers_returnUsers() {
        PaginationWithEntitiesDTO<UserDTO> actualUsersByPage = userService.getUsersByPage(VALID_PAGE);
        assertThat(actualUsersByPage).isNotNull();
        verify(userRepository, times(1))
                .getUsersByPage(anyInt(), anyInt());
        verify(userRepository, times(1)).getCountOfEntities();
    }

    @Test
    void getUsers_returnValidUsers() {
        List<User> returnedUsers = getUsers();
        when(userRepository.getCountOfEntities()).thenReturn(VALID_COUNT_OF_ENTITIES);
        when(userRepository.getUsersByPage(VALID_START_POSITION, VALID_OBJECT_BY_PAGE))
                .thenReturn(returnedUsers);
        PaginationWithEntitiesDTO<UserDTO> actualUserDTOSByPage = userService.getUsersByPage(VALID_PAGE);
        assertThat(actualUserDTOSByPage.getEntities().get(0).getId()).isEqualTo(returnedUsers.get(0).getId());
        assertThat(actualUserDTOSByPage.getEntities().get(0).getEmail()).isEqualTo(returnedUsers.get(0).getEmail());
        assertThat(actualUserDTOSByPage.getEntities().get(0).getLastName())
                .isEqualTo(returnedUsers.get(0).getUserDetails().getLastName());
        assertThat(actualUserDTOSByPage.getEntities().get(0).getFirstName())
                .isEqualTo(returnedUsers.get(0).getUserDetails().getFirstName());
        assertThat(actualUserDTOSByPage.getEntities().get(0).getPatronymic())
                .isEqualTo(returnedUsers.get(0).getUserDetails().getPatronymic());
        assertThat(actualUserDTOSByPage.getEntities().get(0).getIsDeleted())
                .isEqualTo(returnedUsers.get(0).getDeleted());
        assertThat(actualUserDTOSByPage.getEntities().get(0).getRole().name())
                .isEqualTo(returnedUsers.get(0).getRole().name());
        assertThat(actualUserDTOSByPage.getPages()).isEqualTo(VALID_PAGES);
    }

    /* delete users */
    @Test
    void deleteOneUser_returnListOfDeletedEmails() throws UserExistenceException, AdministratorChangingException {
        List<Long> ids = getValidListOfId(1);
        User returnedUser = getValidUser();
        when(userRepository.getUserById(anyLong())).thenReturn(returnedUser);
        List<String> actualEmails = userService.deleteUsers(ids);
        assertThat(actualEmails).isNotNull();
        verify(userRepository, times(1)).getUserById(anyLong());
        verify(userRepository, times(1)).delete(any());
    }

    @Test
    void deleteSomeUsers_returnListOfDeletedEmails() throws UserExistenceException, AdministratorChangingException {
        List<Long> ids = getValidListOfId(3);
        User returnedUser = getValidUser();
        when(userRepository.getUserById(anyLong())).thenReturn(returnedUser);
        List<String> actualEmails = userService.deleteUsers(ids);
        assertThat(actualEmails).isNotNull();
        verify(userRepository, times(3)).getUserById(anyLong());
        verify(userRepository, times(3)).delete(any());
    }

    @Test()
    void deleteInvalidUsers_returnUserExistenceException() throws UserExistenceException {
        List<Long> ids = getValidListOfId(1);
        when(userRepository.getUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserExistenceException.class)
                .isThrownBy(() -> userService.deleteUsers(ids));
        assertThrows(
                UserExistenceException.class,
                () -> userService.deleteUsers(ids),
                "user not found"
        );
    }

    @Test
    void deleteUsersWithSuperAdministrator_returnAdministratorChangingException() {
        List<Long> ids = getValidListOfId(1);
        User returnedUser = getValidUser();
        returnedUser.setEmail(VALID_EMAIL_SUPER_ADMIN);
        when(userRepository.getUserById(anyLong())).thenReturn(returnedUser);
        assertThatExceptionOfType(AdministratorChangingException.class)
                .isThrownBy(() -> userService.deleteUsers(ids));
        assertThrows(
                AdministratorChangingException.class,
                () -> userService.deleteUsers(ids),
                "attempt to remove super administrator"
        );
    }

    /* update role */
    @Test
    void updateRole_returnUserDTO() throws UserExistenceException, AdministratorChangingException {
        User returnedUser = getValidUser();
        when(userRepository.getUserById(VALID_USER_ID)).thenReturn(returnedUser);
        UserDTO actualUserDTO = userService.updateRole(VALID_USER_ID, CUSTOMER_USER);
        assertThat(actualUserDTO).isNotNull();
        assertThat(actualUserDTO.getEmail()).isEqualTo(VALID_EMAIL);
        assertThat(actualUserDTO.getRole()).isEqualByComparingTo(CUSTOMER_USER);

    }

    @Test
    void updateRole_callBusinessLogic() throws UserExistenceException, AdministratorChangingException {
        User returnedUser = getValidUser();
        when(userRepository.getUserById(VALID_USER_ID)).thenReturn(returnedUser);
        userService.updateRole(VALID_USER_ID, CUSTOMER_USER);
        verify(userRepository, times(1)).getUserById(VALID_USER_ID);
    }

    @Test
    void updateRoleNotExistUser_returnUserExistenceException() throws UserExistenceException {
        when(userRepository.getUserById(VALID_USER_ID)).thenReturn(null);
        assertThatExceptionOfType(UserExistenceException.class)
                .isThrownBy(() -> userService.updateRole(VALID_USER_ID, CUSTOMER_USER));
        assertThrows(
                UserExistenceException.class,
                () -> userService.updateRole(VALID_USER_ID, CUSTOMER_USER),
                "user not found");
    }

    @Test
    void updateRoleWithSuperAdministrator_returnAdministratorChangingException() {
        User returnedUser = getValidUser();
        returnedUser.setEmail(VALID_EMAIL_SUPER_ADMIN);
        when(userRepository.getUserById(anyLong())).thenReturn(returnedUser);
        assertThatExceptionOfType(AdministratorChangingException.class)
                .isThrownBy(() -> userService.updateRole(VALID_USER_ID, CUSTOMER_USER));
        assertThrows(
                AdministratorChangingException.class,
                () -> userService.updateRole(VALID_USER_ID, CUSTOMER_USER),
                "attempt to update super administrator"
        );
    }

    /* change user password */
    @Test
    void changePassword_returnUserDTO() throws UserExistenceException, AdministratorChangingException {
        User returnedUser = getValidUser();
        when(userRepository.getUserById(VALID_USER_ID)).thenReturn(returnedUser);
        UserDTO actualUserDTO = userService.updatePassword(VALID_USER_ID);
        assertThat(actualUserDTO).isNotNull();
        assertThat(actualUserDTO.getEmail()).isEqualTo(VALID_EMAIL);
    }

    @Test
    void changePassword_callBusinessLogic() throws UserExistenceException, AdministratorChangingException {
        User returnedUser = getValidUser();
        when(userRepository.getUserById(anyLong())).thenReturn(returnedUser);
        UserDTO actualUserDTO = userService.updatePassword(VALID_USER_ID);
        verify(userRepository, times(1)).getUserById(VALID_USER_ID);
        verify(userRepository, times(1)).merge(returnedUser);
        verify(mailUtil, times(1)).sendMessage(anyString(), anyString(), anyString());
    }

    @Test
    void changePasswordNotExistUser_returnUserExistenceException() throws UserExistenceException {
        when(userRepository.getUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserExistenceException.class)
                .isThrownBy(() -> userService.updatePassword(VALID_USER_ID));
        assertThrows(
                UserExistenceException.class,
                () -> userService.updatePassword(VALID_USER_ID),
                "user not found");
    }

    @Test
    void changePasswordWithSuperAdministrator_returnAdministratorChangingException() {
        User returnedUser = getValidUser();
        returnedUser.setEmail(VALID_EMAIL_SUPER_ADMIN);
        when(userRepository.getUserById(anyLong())).thenReturn(returnedUser);
        assertThatExceptionOfType(AdministratorChangingException.class)
                .isThrownBy(() -> userService.updatePassword(anyLong()));
        assertThrows(
                AdministratorChangingException.class,
                () -> userService.updatePassword(anyLong()),
                "attempt to update super administrator"
        );
    }

    /* add new user */
    @Test
    void addUser_returnUserDTO() throws UserExistenceException {
        AddUserDTO addUserDTO = getAddUserDTO();
        UserDTO actualResult = userService.addUser(addUserDTO);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getLastName()).isEqualTo(addUserDTO.getLastName());
        assertThat(actualResult.getFirstName()).isEqualTo(addUserDTO.getFirstName());
        assertThat(actualResult.getPatronymic()).isEqualTo(addUserDTO.getPatronymic());
        assertThat(actualResult.getEmail()).isEqualTo(addUserDTO.getEmail());
        assertThat(actualResult.getRole()).isEqualTo(addUserDTO.getRole());
    }

    @Test
    void addUser_callBusinessLogic() throws UserExistenceException {
        AddUserDTO addUserDTO = getAddUserDTO();
        userService.addUser(addUserDTO);
        verify(userRepository, times(1)).getUserByEmail(any());
        verify(userRepository, times(1)).add(any());
    }

    @Test
    void addExistUser_returnUserExistenceException() {
        AddUserDTO addUserDTO = getAddUserDTO();
        User returnedUser = getValidUser();
        when(userRepository.getUserByEmail(anyString())).thenReturn(returnedUser);
        assertThatExceptionOfType(UserExistenceException.class)
                .isThrownBy(() -> userService.addUser(addUserDTO));
        assertThrows(
                UserExistenceException.class,
                () -> userService.addUser(addUserDTO),
                "user exist"
        );
    }

    /* get user profile */
    @Test
    void getUserProfile_returnUserProfile() throws AnonymousUserException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(VALID_EMAIL);
        User returnedUser = getValidUser();
        when(userRepository.getUserByEmail(anyString())).thenReturn(returnedUser);
        UserProfileDTO actualUserProfile = userService.getUserProfile();
        assertThat(actualUserProfile).isNotNull();
        assertThat(actualUserProfile.getLastName()).isEqualTo(VALID_LAST_NAME);
        assertThat(actualUserProfile.getFirstName()).isEqualTo(VALID_FIRST_NAME);
        assertThat(actualUserProfile.getPatronymic()).isEqualTo(VALID_PATRONYMIC);
        assertThat(actualUserProfile.getAddress()).isEqualTo(VALID_ADDRESS);
        assertThat(actualUserProfile.getPhone()).isEqualTo(VALID_PHONE);
    }

    @Test
    void getUserProfile_callLogic() throws AnonymousUserException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(VALID_EMAIL);
        User returnedUser = getValidUser();
        when(userRepository.getUserByEmail(anyString())).thenReturn(returnedUser);
        userService.getUserProfile();
        verify(userRepository, times(1)).getUserByEmail(any());
    }

    @Test
    void getProfile_returnAnonymousUserException() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_ANONYMOUS);
        assertThatExceptionOfType(AnonymousUserException.class)
                .isThrownBy(() -> userService.getUserProfile());
        assertThrows(
                AnonymousUserException.class,
                () -> userService.getUserProfile()
        );
    }

    /* update user profile */
    @Test
    void updateProfile_returnProfile() throws AnonymousUserException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(VALID_EMAIL);
        UpdateUserProfileDTO updateUserProfileDTO = getValidUpdateUserProfileDTO();
        User returnedUser = getValidUser();
        when(userRepository.getUserByEmail(anyString())).thenReturn(returnedUser);
        UserProfileDTO actualUser = userService.updateProfile(updateUserProfileDTO);
        assertThat(actualUser).isNotNull();
    }

    @Test
    void updateProfile_callLogic() throws AnonymousUserException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(VALID_EMAIL);
        UpdateUserProfileDTO updateUserProfileDTO = getValidUpdateUserProfileDTO();
        User returnedUser = getValidUser();
        when(userRepository.getUserByEmail(VALID_EMAIL)).thenReturn(returnedUser);
        userService.updateProfile(updateUserProfileDTO);
        verify(SecurityContextHolder.getContext(), times(1)).getAuthentication();
        verify(userRepository, times(1)).getUserByEmail(anyString());
    }

    /* change password user profile */
    @Test
    void changePassword_returnUserProfileDTO() throws AnonymousUserException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(VALID_EMAIL);
        User returnedUser = getValidUser();
        when(userRepository.getUserByEmail(VALID_EMAIL)).thenReturn(returnedUser);
        UserProfileDTO actualUser = userService.changePassword(VALID_PASSWORD);
        verify(userRepository, times(1)).getUserByEmail(anyString());
        assertThat(actualUser).isNotNull();
        //        assertThat(actualUser).;
    }

    @Test
    void changePassword_returnAnonymousUserException() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_ANONYMOUS);
        assertThatExceptionOfType(AnonymousUserException.class)
                .isThrownBy(() -> userService.getUserProfile());
        assertThrows(
                AnonymousUserException.class,
                () -> userService.getUserProfile()
        );
    }

    /* get current user */
    @Test
    void getCurrentUser_returnValidUser() throws AnonymousUserException {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(VALID_EMAIL);
        User returnedUser = getValidUser();
        when(userRepository.getUserByEmail(any())).thenReturn(returnedUser);
        User actualUser = userService.getCurrentUser();
        verify(userRepository, times(1)).getUserByEmail(any());
        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getEmail()).isEqualTo(VALID_EMAIL);
        assertThat(actualUser.getRole()).isEqualByComparingTo(UserRoleEnum.ADMINISTRATOR);
    }

    @Test
    void getCurrentUser_returnAnonymousUserException() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_ANONYMOUS);
        assertThatExceptionOfType(AnonymousUserException.class)
                .isThrownBy(() -> userService.getUserProfile());
        assertThrows(
                AnonymousUserException.class,
                () -> userService.getUserProfile()
        );
    }

    private UpdateUserProfileDTO getValidUpdateUserProfileDTO() {
        UpdateUserProfileDTO updateUserProfileDTO = new UpdateUserProfileDTO();
        updateUserProfileDTO.setLastName(VALID_LAST_NAME + "upd");
        updateUserProfileDTO.setFirstName(VALID_FIRST_NAME + "upd");
        updateUserProfileDTO.setPatronymic(VALID_PATRONYMIC + "upd");
        updateUserProfileDTO.setAddress(VALID_ADDRESS + "upd");
        updateUserProfileDTO.setPhone(VALID_PHONE + "upd");
        return updateUserProfileDTO;
    }

    private AddUserDTO getAddUserDTO() {
        AddUserDTO addUserDTO = new AddUserDTO();
        addUserDTO.setLastName(VALID_LAST_NAME);
        addUserDTO.setFirstName(VALID_FIRST_NAME);
        addUserDTO.setPatronymic(VALID_PATRONYMIC);
        addUserDTO.setEmail(VALID_EMAIL);
        addUserDTO.setRole(UserRoleDTOEnum.valueOf(VALID_USER_ROLE));
        return addUserDTO;
    }

    private List<Long> getValidListOfId(int size) {
        return LongStream.range(1, size + 1).boxed()
                .collect(Collectors.toList());
    }

    private List<User> getUsers() {
        List<User> users = new ArrayList<>();
        User user = getValidUser();
        users.add(user);
        return users;
    }

    private User getValidUser() {
        User user = new User();
        user.setEmail(VALID_EMAIL);
        user.setRole(UserRoleEnum.valueOf(VALID_USER_ROLE));
        user.setPassword(VALID_PASSWORD);
        user.setDeleted(VALID_IS_DELETED);
        user.setUserDetails(getValidUserDetails());
        user.setUserInformation(getValidUserInformation());
        return user;
    }

    private UserInformation getValidUserInformation() {
        UserInformation userInformation = new UserInformation();
        userInformation.setAddress(VALID_ADDRESS);
        userInformation.setPhone(VALID_PHONE);
        return userInformation;
    }

    private UserDetails getValidUserDetails() {
        UserDetails userDetails = new UserDetails();
        userDetails.setLastName(VALID_LAST_NAME);
        userDetails.setFirstName(VALID_FIRST_NAME);
        userDetails.setPatronymic(VALID_PATRONYMIC);
        return userDetails;
    }

}