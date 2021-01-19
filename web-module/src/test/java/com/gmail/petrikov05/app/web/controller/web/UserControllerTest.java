package com.gmail.petrikov05.app.web.controller.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.petrikov05.app.service.UserService;
import com.gmail.petrikov05.app.service.exception.AdministratorChangingException;
import com.gmail.petrikov05.app.service.exception.UserExistenceException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.user.AddUserDTO;
import com.gmail.petrikov05.app.service.model.user.UserDTO;
import com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum;
import com.gmail.petrikov05.app.web.config.TestConfig;
import com.gmail.petrikov05.app.web.controller.UserController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum.CUSTOMER_USER;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_FIELD_NOT_MARKED;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_PASSWORD_UPDATE_FAIL;
import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_USER_NOT_FOUND;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_EMAIL;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_IS_DELETED;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGES;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_FIRST_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_LAST_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_PATRONYMIC;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_ROLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = UserController.class)
@Import(TestConfig.class)
@WithMockUser(roles = "ADMINISTRATOR")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    /* show users page */
    @Test
    public void getUsers_returnUsersPage() throws Exception {
        PaginationWithEntitiesDTO<UserDTO> returnedPaginationWithEntitiesDTO = getValidPaginationWithEntitiesDTO();
        when(userService.getUsersByPage(anyInt())).thenReturn(returnedPaginationWithEntitiesDTO);
        mockMvc.perform(
                get("/users")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk())
                .andExpect(view().name("users"));
    }

    @Test
    public void getUsersWithInvalidPage_returnBadRequest() throws Exception {
        mockMvc.perform(get("/users")
                .contentType(MediaType.TEXT_HTML)
                .param("page", "a")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void getUsers_returnUserWithValidPage() throws Exception {
        PaginationWithEntitiesDTO<UserDTO> returnedPaginationWithEntitiesDTO = getValidPaginationWithEntitiesDTO();
        when(userService.getUsersByPage(anyInt())).thenReturn(returnedPaginationWithEntitiesDTO);
        mockMvc.perform(
                get("/users")
                        .contentType(MediaType.TEXT_HTML)
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().isOk())
                .andExpect(model().attribute("page", VALID_PAGE));
    }

    @Test
    public void getUsers_returnValidUsersWithPages() throws Exception {
        PaginationWithEntitiesDTO<UserDTO> returnedPaginationWithEntitiesDTO = getValidPaginationWithEntitiesDTO();
        when(userService.getUsersByPage(anyInt())).thenReturn(returnedPaginationWithEntitiesDTO);
        mockMvc.perform(
                get("/users")
                        .contentType(MediaType.TEXT_HTML)
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().isOk())
                .andExpect(model().attributeExists("pages"));
    }

    @Test
    public void getUsers_callBusinessLogic() throws Exception {
        PaginationWithEntitiesDTO<UserDTO> returnedPaginationWithEntitiesDTO = getValidPaginationWithEntitiesDTO();
        when(userService.getUsersByPage(anyInt())).thenReturn(returnedPaginationWithEntitiesDTO);
        mockMvc.perform(
                get("/users")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1)).getUsersByPage(anyInt());
    }

    @Test
    public void getUsers_returnUsersWithValidId() throws Exception {
        PaginationWithEntitiesDTO<UserDTO> returnedPaginationWithEntitiesDTO = getValidPaginationWithEntitiesDTO();
        when(userService.getUsersByPage(anyInt())).thenReturn(returnedPaginationWithEntitiesDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/users")
                        .contentType(MediaType.TEXT_HTML)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ID));
    }

    @Test
    public void getUsers_returnUsersWithValidEmail() throws Exception {
        PaginationWithEntitiesDTO<UserDTO> returnedPaginationWithEntitiesDTO = getValidPaginationWithEntitiesDTO();
        when(userService.getUsersByPage(anyInt())).thenReturn(returnedPaginationWithEntitiesDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/users")
                        .contentType(MediaType.TEXT_HTML)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_EMAIL);
    }

    @Test
    public void getUsers_returnUsersWithValidRole() throws Exception {
        PaginationWithEntitiesDTO<UserDTO> returnedPaginationWithEntitiesDTO = getValidPaginationWithEntitiesDTO();
        when(userService.getUsersByPage(anyInt())).thenReturn(returnedPaginationWithEntitiesDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/users")
                        .contentType(MediaType.TEXT_HTML)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_ROLE);
    }

    @Test
    public void getUsers_returnUsersWithValidLastName() throws Exception {
        PaginationWithEntitiesDTO<UserDTO> returnedPaginationWithEntitiesDTO = getValidPaginationWithEntitiesDTO();
        when(userService.getUsersByPage(anyInt())).thenReturn(returnedPaginationWithEntitiesDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/users")
                        .contentType(MediaType.TEXT_HTML)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_LAST_NAME);
    }

    @Test
    public void getUsers_returnUsersWithValidFirstName() throws Exception {
        PaginationWithEntitiesDTO<UserDTO> returnedPaginationWithEntitiesDTO = getValidPaginationWithEntitiesDTO();
        when(userService.getUsersByPage(anyInt())).thenReturn(returnedPaginationWithEntitiesDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/users")
                        .contentType(MediaType.TEXT_HTML)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_FIRST_NAME);
    }

    @Test
    public void getUsers_returnUsersWithValidPatronymic() throws Exception {
        PaginationWithEntitiesDTO<UserDTO> returnedPaginationWithEntitiesDTO = getValidPaginationWithEntitiesDTO();
        when(userService.getUsersByPage(anyInt())).thenReturn(returnedPaginationWithEntitiesDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/users")
                        .contentType(MediaType.TEXT_HTML)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_PATRONYMIC);
    }

    @Test
    public void getUsers_returnUsersWithValidIsDeleted() throws Exception {
        PaginationWithEntitiesDTO<UserDTO> returnedPaginationWithEntitiesDTO = getValidPaginationWithEntitiesDTO();
        when(userService.getUsersByPage(anyInt())).thenReturn(returnedPaginationWithEntitiesDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/users")
                        .contentType(MediaType.TEXT_HTML)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains("<span>deleted</span>");
    }

    /* delete users*/
    @Test
    public void deleteUsers_returnRedirect() throws Exception {
        mockMvc.perform(
                post("/users/delete")
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users?page=" + VALID_PAGE));
    }

    @Test
    public void deleteUsersWithEmptyIds_returnBadRequest() throws Exception {
        mockMvc.perform(
                post("/users/delete")
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users?page=" + VALID_PAGE))
                .andExpect(flash().attribute("message", MESSAGE_FIELD_NOT_MARKED));
    }

    @Test
    public void deleteUsers_callBusinessLogic() throws Exception, UserExistenceException, AdministratorChangingException {
        mockMvc.perform(
                post("/users/delete")
                        .param("page", String.valueOf(VALID_PAGE))
                        .param("id", String.valueOf(2))
                        .param("id", String.valueOf(3))
        ).andExpect(status().is3xxRedirection());
        verify(userService, times(1)).deleteUsers(anyList());
    }

    @Test
    public void deleteUsers_returnListWithDeletedEmails() throws Exception {
        mockMvc.perform(
                post("/users/delete")
                        .param("page", String.valueOf(VALID_PAGE))
                        .param("id", String.valueOf(2))
                        .param("id", String.valueOf(3))
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"));
    }

    @Test
    public void deleteUsers_returnValidList() throws Exception, UserExistenceException, AdministratorChangingException {
        List<String> returnedEmails = getValidListOfEmails();
        when(userService.deleteUsers(anyList())).thenReturn(returnedEmails);
        mockMvc.perform(
                post("/users/delete")
                        .param("page", String.valueOf(VALID_PAGE))
                        .param("id", String.valueOf(2))
        ).andExpect(flash().attribute("message", VALID_EMAIL + " deleted"));
    }

    @Test
    public void deleteNotAvailableUsers_redirectUsersWithMessage() throws Exception, UserExistenceException, AdministratorChangingException {
        when(userService.deleteUsers(anyList())).thenThrow(new UserExistenceException("not found user"));
        mockMvc.perform(
                post("/users/delete")
                        .param("page", String.valueOf(VALID_PAGE))
                        .param("id", String.valueOf(2))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users?page=" + VALID_PAGE))
                .andExpect(flash().attribute("message", "not found user : deleting users failed"));
    }

    @Test
    public void deleteUsersWithSuperAdministrator_redirectUsersWithMessage() throws Exception, UserExistenceException, AdministratorChangingException {
        when(userService.deleteUsers(anyList()))
                .thenThrow(new AdministratorChangingException("attempt to remove super administrator"));
        mockMvc.perform(
                post("/users/delete")
                        .param("page", String.valueOf(VALID_PAGE))
                        .param("id", String.valueOf(2))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users?page=" + VALID_PAGE))
                .andExpect(flash().attribute(
                        "message", "attempt to remove super administrator"));
    }

    /* update user role */
    @Test
    public void updateRole_redirectUsersWithPage() throws Exception, UserExistenceException, AdministratorChangingException {
        UserDTO returnedUser = getValidUserDTO();
        when(userService.updateRole(anyLong(), any(UserRoleDTOEnum.class)))
                .thenReturn(returnedUser);
        mockMvc.perform(
                get("/users/2/updateRole")
                        .param("page", String.valueOf(VALID_PAGE))
                        .param("role", String.valueOf(CUSTOMER_USER))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users?page=" + VALID_PAGE));
    }

    @Test
    public void updateRole_returnBadRequest() throws Exception {
        mockMvc.perform(
                get("/users/2/updateRole")
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void updateRole_callBusinessLogic() throws Exception, UserExistenceException, AdministratorChangingException {
        UserDTO returnedUser = getValidUserDTO();
        when(userService.updateRole(anyLong(), any(UserRoleDTOEnum.class)))
                .thenReturn(returnedUser);
        mockMvc.perform(
                get("/users/2/updateRole")
                        .param("page", String.valueOf(VALID_PAGE))
                        .param("role", String.valueOf(CUSTOMER_USER))
        ).andExpect(status().is3xxRedirection());
        verify(userService, times(1)).updateRole(anyLong(), any(UserRoleDTOEnum.class));
    }

    @Test
    public void updateRole_redirectUsersWithMassage() throws Exception, UserExistenceException, AdministratorChangingException {
        UserDTO returnedUser = getValidUserDTO();
        returnedUser.setRole(CUSTOMER_USER);
        when(userService.updateRole(anyLong(), any(UserRoleDTOEnum.class)))
                .thenReturn(returnedUser);
        mockMvc.perform(
                get("/users/2/updateRole")
                        .param("page", String.valueOf(VALID_PAGE))
                        .param("role", String.valueOf(CUSTOMER_USER))
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute(
                        "message",
                        "user (" + VALID_EMAIL + ") updated. Enter role: " + UserRoleDTOEnum.CUSTOMER_USER));
    }

    @Test
    public void updateRoleNotExistUser_redirectUsersWithMassage() throws Exception, UserExistenceException, AdministratorChangingException {
        when(userService.updateRole(anyLong(), any(UserRoleDTOEnum.class)))
                .thenThrow(new UserExistenceException("user not found"));
        mockMvc.perform(
                get("/users/2/updateRole")
                        .param("page", String.valueOf(VALID_PAGE))
                        .param("role", String.valueOf(CUSTOMER_USER))
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute(
                        "message", MESSAGE_USER_NOT_FOUND));
    }

    @Test
    public void updateRoleSuperAdministrator_redirectUsersWithMassage() throws Exception, UserExistenceException, AdministratorChangingException {
        when(userService.updateRole(anyLong(), any(UserRoleDTOEnum.class)))
                .thenThrow(new AdministratorChangingException("Try to delete Super administrator"));
        mockMvc.perform(
                get("/users/2/updateRole")
                        .param("page", String.valueOf(VALID_PAGE))
                        .param("role", String.valueOf(CUSTOMER_USER))
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute(
                        "message", "Try to delete Super administrator"));
    }

    /* change user password */
    @Test
    public void updatePassword_redirectUsersWithPage() throws Exception, UserExistenceException, AdministratorChangingException {
        UserDTO returnedUserDTO = getValidUserDTO();
        when(userService.updatePassword(anyLong())).thenReturn(returnedUserDTO);
        mockMvc.perform(
                get("/users/3/updatePass")
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users?page=" + VALID_PAGE));
    }

    @Test
    public void updatePassword_returnBadRequest() throws Exception {
        mockMvc.perform(
                get("/users/a/updatePass")
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void updatePasswordWithInvalidPage_returnBadRequest() throws Exception {
        mockMvc.perform(
                get("/users/2/updatePass")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void updatePassword_callBusinessLogic() throws Exception, UserExistenceException, AdministratorChangingException {
        UserDTO returnedUserDTO = getValidUserDTO();
        when(userService.updatePassword(anyLong())).thenReturn(returnedUserDTO);
        mockMvc.perform(
                get("/users/2/updatePass")
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection());
        verify(userService, times(1)).updatePassword(anyLong());
    }

    @Test
    public void updatePassword_redirectUsersWithMessage() throws Exception, UserExistenceException, AdministratorChangingException {
        UserDTO returnedUserDTO = getValidUserDTO();
        when(userService.updatePassword(anyLong())).thenReturn(returnedUserDTO);
        mockMvc.perform(
                get("/users/2/updatePass")
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "The password changed. A new password sent on the email: test@test.test"));
    }

    @Test
    public void updatePassword_returnUserExistenceException() throws Exception, UserExistenceException, AdministratorChangingException {
        when(userService.updatePassword(anyLong())).thenThrow(new UserExistenceException("user not found"));
        mockMvc.perform(
                get("/users/2/updatePass")
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "A password didn't change."));
    }

    @Test
    public void updatePasswordSuperAdministrator_returnAdmin() throws Exception, UserExistenceException, AdministratorChangingException {
        when(userService.updatePassword(anyLong()))
                .thenThrow(new AdministratorChangingException("super administrator doesn't delete"));
        mockMvc.perform(
                get("/users/2/updatePass")
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", MESSAGE_PASSWORD_UPDATE_FAIL));
    }

    /* show add page */
    @Test
    public void showAddPage_returnStatusOk() throws Exception {
        mockMvc.perform(
                get("/users/add")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk())
                .andExpect(view().name("user_add"));
    }

    @Test
    public void showAddPage_returnListOfRoles() throws Exception {
        mockMvc.perform(
                get("/users/add")
        ).andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void showAddPage_returnValidPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/users/add")
        ).andReturn();
        String actualContent = mvcResult.getResponse().getContentAsString();
        assertThat(actualContent).contains(VALID_USER_ROLE);
        assertThat(actualContent).contains("role1");
    }

    /* add new user */
    @Test
    public void addUser_returnStatusRedirect() throws Exception, UserExistenceException {
        UserDTO returnedUser = getValidUserDTO();
        when(userService.addUser(any())).thenReturn(returnedUser);
        mockMvc.perform(
                post("/users/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", VALID_USER_PATRONYMIC)
                        .param("email", VALID_EMAIL)
                        .param("role", VALID_USER_ROLE)
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    public void addInvalidUser_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
        ).andExpect(status().isOk())
                .andExpect(view().name("user_add"));
    }

    @Test
    public void addUserWithEmptyLastName_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", "")
        ).andExpect(status().isOk());
    }

    @Test
    public void addUserWithSmallLastName_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", getStringLength(1))
        ).andExpect(status().isOk());
    }

    @Test
    public void addUserWithBigLastName_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", getStringLength(42))
        ).andExpect(status().isOk());
    }

    @Test
    public void addUserWithInvalidLastName_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", "232")
        ).andExpect(status().isOk());
    }

    @Test
    public void addUserWithEmptyFirstName_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", "")
        ).andExpect(status().isOk());
    }

    @Test
    public void addUserWithSmallFirstName_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", getStringLength(1))
        ).andExpect(status().isOk());
    }

    @Test
    public void addUserWithBigFirstName_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", getStringLength(21))
        ).andExpect(status().isOk());
    }

    @Test
    public void addUserWithInvalidFirstName_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", "wer2w")
        ).andExpect(status().isOk());
    }

    @Test
    public void addUserWithEmptyPatronymic_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", "")
        ).andExpect(status().isOk());
    }

    @Test
    public void addUserWithSmallPatronymic_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", getStringLength(1))
        ).andExpect(status().isOk());
    }

    @Test
    public void addUserWithBigPatronymic_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", getStringLength(41))
        ).andExpect(status().isOk());
    }

    @Test
    public void addUserWithInvalidPatronymic_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", "we3we")
        ).andExpect(status().isOk());
    }

    @Test
    public void addUserWithEmptyEmail_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", VALID_USER_PATRONYMIC)
                        .param("email", "")
        ).andExpect(status().isOk());
    }

    @Test
    public void addUserWithBigEmail_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", VALID_USER_PATRONYMIC)
                        .param("email", getStringLength(51))
        ).andExpect(status().isOk());
    }

    @Test
    public void addUserWithInvalidEmail_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", VALID_USER_PATRONYMIC)
                        .param("email", getStringLength(35))
        ).andExpect(status().isOk());
    }

    @Test
    public void addUserWithEmptyRole_returnStatusOk() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", VALID_USER_PATRONYMIC)
                        .param("email", VALID_EMAIL)
        ).andExpect(status().isOk());
    }

    @Test
    public void addUser_callBusinessLogic() throws Exception {
        UserDTO returnedUser = getValidUserDTO();
        try {
            when(userService.addUser(any())).thenReturn(returnedUser);
        } catch (UserExistenceException e) {
            e.printStackTrace();
        }
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", VALID_USER_PATRONYMIC)
                        .param("email", VALID_EMAIL)
                        .param("role", VALID_USER_ROLE)
        ).andExpect(status().is3xxRedirection());
        try {
            verify(userService, times(1)).addUser(any());
        } catch (UserExistenceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addUser_returnAddedUserDTO() throws Exception, UserExistenceException {
        UserDTO returnedUser = getValidUserDTO();
        when(userService.addUser(any())).thenReturn(returnedUser);
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", VALID_USER_PATRONYMIC)
                        .param("email", VALID_EMAIL)
                        .param("role", VALID_USER_ROLE)
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("message", "User with email (" + VALID_EMAIL + ") added"));
    }

    @Test
    public void addUser_returnUserExistenceException() throws Exception, UserExistenceException {
        when(userService.addUser(any())).thenThrow(new UserExistenceException("user with this email exists"));
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", VALID_USER_PATRONYMIC)
                        .param("email", VALID_EMAIL)
                        .param("role", VALID_USER_ROLE)
        ).andExpect(status().isOk())
                .andExpect(model().attribute("error", "user with email (" + VALID_EMAIL + ") exists"));
    }

    private AddUserDTO getValidAddUserDTO() {
        AddUserDTO addUserDTO = new AddUserDTO();
        addUserDTO.setLastName(VALID_USER_LAST_NAME);
        addUserDTO.setFirstName(VALID_USER_FIRST_NAME);
        addUserDTO.setPatronymic(VALID_USER_PATRONYMIC);
        addUserDTO.setEmail(VALID_EMAIL);
        addUserDTO.setRole(UserRoleDTOEnum.valueOf(VALID_USER_ROLE));
        return addUserDTO;
    }

    private String getStringLength(int length) {
        return IntStream.range(0, length)
                .boxed()
                .map(x -> "a")
                .collect(Collectors.joining());
    }

    private List<String> getValidListOfEmails() {
        return IntStream.range(1, 2)
                .mapToObj(x -> VALID_EMAIL)
                .collect(Collectors.toList());
    }

    private PaginationWithEntitiesDTO<UserDTO> getValidPaginationWithEntitiesDTO() {
        List<UserDTO> userDTOS = getValidUserDTOS();
        return new PaginationWithEntitiesDTO<>(userDTOS, VALID_PAGES);
    }

    private List<UserDTO> getValidUserDTOS() {
        List<UserDTO> userDTOS = new ArrayList<>();
        UserDTO userDTO = getValidUserDTO();
        userDTOS.add(userDTO);
        return userDTOS;
    }

    private UserDTO getValidUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(VALID_ID);
        userDTO.setEmail(VALID_EMAIL);
        userDTO.setRole(UserRoleDTOEnum.valueOf(VALID_USER_ROLE));
        userDTO.setLastName(VALID_USER_LAST_NAME);
        userDTO.setFirstName(VALID_USER_FIRST_NAME);
        userDTO.setPatronymic(VALID_USER_PATRONYMIC);
        userDTO.setIsDeleted(VALID_IS_DELETED);
        return userDTO;
    }

}