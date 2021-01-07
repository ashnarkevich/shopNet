package com.gmail.petrikov05.app.service.util.converter;

import com.gmail.petrikov05.app.repository.model.User;
import com.gmail.petrikov05.app.repository.model.UserDetails;
import com.gmail.petrikov05.app.repository.model.UserInformation;
import com.gmail.petrikov05.app.repository.model.constant.UserRoleEnum;
import com.gmail.petrikov05.app.service.model.user.AddUserDTO;
import com.gmail.petrikov05.app.service.model.user.LoginUserDTO;
import com.gmail.petrikov05.app.service.model.user.UserDTO;
import com.gmail.petrikov05.app.service.model.user.UserProfileDTO;
import com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum;

public class UserConverter {

    public static LoginUserDTO convertObjectToLoginUserDTO(User user) {
        LoginUserDTO userDTO = new LoginUserDTO();
        userDTO.setUsername(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(UserRoleDTOEnum.valueOf(user.getRole().name()));
        return userDTO;
    }

    public static UserDTO convertObjectToFullUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(UserRoleDTOEnum.valueOf(user.getRole().name()));
        userDTO.setIsDeleted(user.getDeleted());
        UserDetails userDetails = user.getUserDetails();
        userDTO.setLastName(userDetails.getLastName());
        userDTO.setFirstName(userDetails.getFirstName());
        userDTO.setPatronymic(userDetails.getPatronymic());
        return userDTO;
    }

    public static User convertAddUserDTOToObject(AddUserDTO addUserDTO) {
        User user = new User();
        user.setEmail(addUserDTO.getEmail());
        user.setRole(UserRoleEnum.valueOf(addUserDTO.getRole().name()));
        UserDetails userDetails = new UserDetails();
        userDetails.setLastName(addUserDTO.getLastName());
        userDetails.setFirstName(addUserDTO.getFirstName());
        userDetails.setPatronymic(addUserDTO.getPatronymic());
        user.setUserDetails(userDetails);
        userDetails.setUser(user);
        UserInformation userInformation = new UserInformation();
        user.setUserInformation(userInformation);
        userInformation.setUser(user);
        return user;
    }

    public static UserProfileDTO convertObjectToUserProfileDTO(User user) {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId(user.getId());
        UserDetails userDetails = user.getUserDetails();
        userProfileDTO.setLastName(userDetails.getLastName());
        userProfileDTO.setFirstName(userDetails.getFirstName());
        userProfileDTO.setPatronymic(userDetails.getPatronymic());
        UserInformation userInformation = user.getUserInformation();
        if (userInformation.getAddress() != null) {
            userProfileDTO.setAddress(userInformation.getAddress());
        }
        if (userInformation.getPhone() != null) {
            userProfileDTO.setPhone(userInformation.getPhone());
        }
        return userProfileDTO;
    }

}
