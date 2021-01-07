package com.gmail.petrikov05.app.service;

import java.util.List;

import com.gmail.petrikov05.app.repository.model.User;
import com.gmail.petrikov05.app.service.exception.AdministratorChangingException;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.exception.UserExistenceException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.user.AddUserDTO;
import com.gmail.petrikov05.app.service.model.user.LoginUserDTO;
import com.gmail.petrikov05.app.service.model.user.UpdateUserProfileDTO;
import com.gmail.petrikov05.app.service.model.user.UserDTO;
import com.gmail.petrikov05.app.service.model.user.UserProfileDTO;
import com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum;

public interface UserService {

    LoginUserDTO getUserByEmail(String username);

    PaginationWithEntitiesDTO<UserDTO> getUsersByPage(int page);

    List<String> deleteUsers(List<Long> ids) throws UserExistenceException, AdministratorChangingException;

    UserDTO updateRole(Long id, UserRoleDTOEnum role) throws UserExistenceException, AdministratorChangingException;

    UserDTO updatePassword(Long id) throws UserExistenceException, AdministratorChangingException;

    UserDTO addUser(AddUserDTO userDTO) throws UserExistenceException;

    UserProfileDTO getUserProfile() throws AnonymousUserException;

    UserProfileDTO updateProfile(UpdateUserProfileDTO updateUserProfileDTO) throws AnonymousUserException;

    UserProfileDTO changePassword(String newPassword) throws AnonymousUserException;

    User getCurrentUser() throws AnonymousUserException;

}
