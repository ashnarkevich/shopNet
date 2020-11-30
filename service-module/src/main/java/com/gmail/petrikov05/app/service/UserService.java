package com.gmail.petrikov05.app.service;

import java.util.List;

import com.gmail.petrikov05.app.service.exception.AdministratorChangingException;
import com.gmail.petrikov05.app.service.exception.UserExistenceException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.user.AddUserDTO;
import com.gmail.petrikov05.app.service.model.user.LoginUserDTO;
import com.gmail.petrikov05.app.service.model.user.UserDTO;
import com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum;

public interface UserService {

    LoginUserDTO getUserByEmail(String username);

    PaginationWithEntitiesDTO<UserDTO> getUsersByPage(int page);

    List<String> deleteUsers(List<Long> ids) throws UserExistenceException, AdministratorChangingException;

    UserDTO updateRole(Long id, UserRoleDTOEnum role) throws UserExistenceException, AdministratorChangingException;

    UserDTO changePassword(Long id) throws UserExistenceException, AdministratorChangingException;

    UserDTO addUser(AddUserDTO userDTO) throws UserExistenceException;

}
