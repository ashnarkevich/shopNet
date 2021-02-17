package com.gmail.petrikov05.app.web.security.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PASSWORD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
class ProfileControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    /* get "/profile" */
    @Test
    public void getProfileLikeUnauthenticatedUser_returnRedirectLogin() throws Exception {
        mockMvc.perform(get("/profile")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@test.test", roles = "CUSTOMER_USER")
    public void getProfileLikeCustomerUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/profile")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getProfileLikeSaleUser_redirect403() throws Exception {
        mockMvc.perform(get("/profile")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getProfileLikeAdministratorUser_redirect403() throws Exception {
        mockMvc.perform(get("/profile")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getProfileLikeApiUserUser_redirect403() throws Exception {
        mockMvc.perform(get("/profile")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* get "/profile/update" */
    @Test
    public void showUpdateProfilePageLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/profile/update")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@test.test", roles = "CUSTOMER_USER")
    public void showUpdateProfilePageLikeCustomerUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/profile/update")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void showUpdateProfilePageLikeSaleUser_redirect403() throws Exception {
        mockMvc.perform(get("/profile/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void showUpdateProfilePageLikeAdministratorUser_redirect403() throws Exception {
        mockMvc.perform(get("/profile/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void showUpdateProfilePageLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(get("/profile/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* post "/profile/update" */
    @Test
    public void updateProfileLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(post("/profile/update")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@test.test", roles = "CUSTOMER_USER")
    public void updateProfileLikeCustomerUser_returnStatusOk() throws Exception {
        mockMvc.perform(post("/profile/update")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void updateProfileLikeSaleUser_redirect403() throws Exception {
        mockMvc.perform(post("/profile/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void updateProfileLikeAdministratorUser_redirect403() throws Exception {
        mockMvc.perform(post("/profile/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void updateProfileLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(post("/profile/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* get "/profile/changePass" */
    @Test
    public void showChangePassProfilePageLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/profile/changePass")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@test.test", roles = "CUSTOMER_USER")
    public void showChangePassProfilePageLikeCustomerUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/profile/changePass")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void showChangePassProfilePageLikeSaleUser_redirect403() throws Exception {
        mockMvc.perform(get("/profile/changePass")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void showChangePassProfilePageLikeAdministratorUser_redirect403() throws Exception {
        mockMvc.perform(get("/profile/changePass")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void showChangePassProfilePageLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(get("/profile/changePass")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* post "/profile/changePass" */
    @Test
    public void changePassProfileLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(post("/profile/changePass")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@test.test", roles = "CUSTOMER_USER")
    public void changePassProfileLikeCustomerUser_returnStatusOk() throws Exception {
        mockMvc.perform(post("/profile/changePass")
                .param("newPassword", VALID_PASSWORD)
                .param("confirmPassword", VALID_PASSWORD)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void changePassProfileLikeSaleUser_redirect403() throws Exception {
        mockMvc.perform(post("/profile/changePass")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void changePassProfileLikeAdministratorUser_redirect403() throws Exception {
        mockMvc.perform(post("/profile/changePass")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void changePassProfileLikeApiUser_redirect403() throws Exception {
        mockMvc.perform(post("/profile/changePass")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

}