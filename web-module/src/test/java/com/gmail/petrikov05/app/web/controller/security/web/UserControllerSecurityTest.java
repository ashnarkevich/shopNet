package com.gmail.petrikov05.app.web.controller.security.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
class UserControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    /* get "/users" */
    @Test
    public void getUsersLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/users")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getUsersLikeAdministratorUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/users")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getUsersLikeSaleUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(get("/users")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getUsersLikeCustomerUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(get("/users")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void getUsersLikeApiUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(get("/users")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* get "/users/add" */
    @Test
    public void showAddUserLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/users/add")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void showAddUserPageLikeAdministratorUser_returnStatusOk() throws Exception {
        mockMvc.perform(get("/users/add")
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void showAddUserPageLikeSaleUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(get("/users/add")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void showAddUserPageLikeCustomerUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(get("/users/add")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void showAddUserPageLikeApiUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(get("/users/add")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* post "/users/add" */
    @Test
    public void addUserLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(post("/users/add")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void addUserLikeAdministratorUser_returnStatusOk() throws Exception {
        mockMvc.perform(post("/users/add")
                .contentType(MediaType.TEXT_HTML)
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void addUserLikeSaleUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(post("/users/add")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void addUserLikeCustomerUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(post("/users/add")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void addUserLikeApiUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(post("/users/add")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* post "/users/delete */
    @Test
    public void deleteUserLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(post("/users/delete")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void deleteUserLikeAdministratorUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(post("/users/delete")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users?page=" + VALID_PAGE));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void deleteUserLikeSaleUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(post("/users/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void deleteUserLikeCustomerUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(post("/users/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void deleteUserLikeApiUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(post("/users/delete")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* get "/users/{id}/updatePass*/
    @Test
    public void updateUserPasswordLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/users/1/updatePass")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void updateUserPasswordLikeAdministratorUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/users/1/updatePass")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(VALID_PAGE))
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users?page=" + VALID_PAGE));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void updateUserPasswordLikeSaleUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(get("/users/1/updatePass")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void updateUserPasswordLikeCustomerUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(get("/users/1/updatePass")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void updateUserPasswordLikeApiUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(get("/users/1/updatePass")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    /* get "/users/{id}/updateRole */
    @Test
    public void updateUserRoleLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/users/1/updateRole")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void updateUserRoleLikeAdministratorUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(get("/users/1/updateRole")
                .contentType(MediaType.TEXT_HTML)
                .param("page", String.valueOf(VALID_PAGE))
                .param("role", "SALE_USER")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users?page=" + VALID_PAGE));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void updateUserRoleLikeSaleUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(get("/users/1/updateRole")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void updateUserRoleLikeCustomerUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(get("/users/1/updateRole")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void updateUserRoleLikeApiUser_returnStatusRedirect403() throws Exception {
        mockMvc.perform(get("/users/1/updateRole")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

}