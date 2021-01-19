package com.gmail.petrikov05.app.web.controller.web.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_ADDRESS;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_FIRST_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_LAST_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PASSWORD;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_PATRONYMIC;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_PHONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
@WithMockUser(roles = "CUSTOMER_USER", username = "test@test.test")
class ProfileControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getUserProfile_returnUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/profile")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_LAST_NAME);
        assertThat(actualResult).contains(VALID_USER_FIRST_NAME);
        assertThat(actualResult).contains(VALID_USER_PATRONYMIC);
        assertThat(actualResult).contains(VALID_USER_ADDRESS);
        assertThat(actualResult).contains(VALID_USER_PHONE);
    }

    @Test
    public void showUpdateProfilePage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/profile/update")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_LAST_NAME);
        assertThat(actualResult).contains(VALID_USER_FIRST_NAME);
        assertThat(actualResult).contains(VALID_USER_PATRONYMIC);
        assertThat(actualResult).contains(VALID_USER_ADDRESS);
        assertThat(actualResult).contains(VALID_USER_PHONE);
    }

    @Test
    public void updateProfile() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                post("/profile/update")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", VALID_USER_PATRONYMIC)
                        .param("address", VALID_USER_ADDRESS)
                        .param("phone", VALID_USER_PHONE)
        ).andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_LAST_NAME);
        assertThat(actualResult).contains(VALID_USER_FIRST_NAME);
        assertThat(actualResult).contains(VALID_USER_PATRONYMIC);
        assertThat(actualResult).contains(VALID_USER_ADDRESS);
        assertThat(actualResult).contains(VALID_USER_PHONE);
    }

    @Test
    public void getChangeProfilePasswordPage() throws Exception {
        mockMvc.perform(
                get("/profile/changePass")
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_change_pass"));
    }

    @Test
    public void changePasswordUserProfile() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                post("/profile/changePass")
                        .param("newPassword", VALID_PASSWORD)
                        .param("confirmPassword", VALID_PASSWORD)
        ).andExpect(status().isOk())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_LAST_NAME);
        assertThat(actualResult).contains(VALID_USER_FIRST_NAME);
        assertThat(actualResult).contains(VALID_USER_PATRONYMIC);
        assertThat(actualResult).contains(VALID_USER_ADDRESS);
        assertThat(actualResult).contains(VALID_USER_PHONE);
    }

}