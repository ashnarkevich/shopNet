package com.gmail.petrikov05.app.web.controller.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum.CUSTOMER_USER;
import static com.gmail.petrikov05.app.web.controller.constant.TestUserConstant.VALID_EMAIL;
import static com.gmail.petrikov05.app.web.controller.constant.TestUserConstant.VALID_FIRST_NAME;
import static com.gmail.petrikov05.app.web.controller.constant.TestUserConstant.VALID_LAST_NAME;
import static com.gmail.petrikov05.app.web.controller.constant.TestUserConstant.VALID_PAGE;
import static com.gmail.petrikov05.app.web.controller.constant.TestUserConstant.VALID_PATRONYMIC;
import static com.gmail.petrikov05.app.web.controller.constant.TestUserConstant.VALID_ROLE;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
@WithMockUser(roles = "ADMINISTRATOR")
class UserControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getUsers_returnUsersPage() throws Exception {
        mockMvc.perform(
                get("/users")
                        .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("pages"))
                .andExpect(model().attributeExists("page"))
                .andExpect(content().string(containsString("admin@shop.com")))
                .andExpect(content().string(containsString("test@test.test")))
                .andExpect(content().string(containsString("ADMINISTRATOR")))
                .andExpect(content().string(containsString("best@best.com")))
                .andExpect(content().string(containsString("SALE_USER")))
                .andExpect(content().string(containsString("TestLastName")))
                .andExpect(content().string(containsString("TestFirstName")))
                .andExpect(content().string(containsString("TestPatronymic")));
    }

    @Test
    public void deleteUsers_returnUsersByPage() throws Exception {
        mockMvc.perform(
                post("/users/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users?page=" + VALID_PAGE));
    }

    @Test
    public void updateRoleUser_redirectUsersPage() throws Exception {
        mockMvc.perform(
                get("/users/2/updateRole")
                        .param("page", String.valueOf(VALID_PAGE))
                        .param("role", String.valueOf(CUSTOMER_USER))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users?page=" + VALID_PAGE))
                .andExpect(flash().attribute(
                        "message",
                        "user (" + VALID_EMAIL + ") updated. Enter role: " + CUSTOMER_USER));
    }

    @Test
    public void changePas_redirectUsersPage() throws Exception {
        mockMvc.perform(
                get("/users/3/changePass")
                        .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(redirectedUrl("/users?page=" + VALID_PAGE))
                .andExpect(flash().attribute(
                        "message", "A password changed. A new password sent on the email: best@best.com"));
    }

    @Test
    public void showAddUser_returnUserPage() throws Exception {
        mockMvc.perform(
                get("/users/add")
        ).andExpect(status().isOk())
                .andExpect(view().name("user_add"));
    }

    @Test
    public void addUser_redirectUsers() throws Exception {
        mockMvc.perform(
                post("/users/add")
                        .param("lastName", VALID_LAST_NAME)
                        .param("firstName", VALID_FIRST_NAME)
                        .param("patronymic", VALID_PATRONYMIC)
                        .param("email", "notexistuser@mail.com")
                        .param("role", VALID_ROLE)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute(
                        "message", "User with email (notexistuser@mail.com) added"));
    }

}