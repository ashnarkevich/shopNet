package com.gmail.petrikov05.app.web.controller.security.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.petrikov05.app.service.model.user.AddUserDTO;
import com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_FIRST_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_LAST_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_PATRONYMIC;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_ROLE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-integration.properties")
class UserAPIControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    /* post "/api/users" */
    @Test
    public void addUserLikeUnauthenticatedUser_returnStatusUnauthorized() throws Exception {
        mockMvc.perform(post("/api/users")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "SECURE_API_USER")
    public void addUserLikeApiUser_returnStatusCreated() throws Exception {
        AddUserDTO addUser = getValidAddUserDTO();
        String content = objectMapper.writeValueAsString(addUser);
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        ).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void addUserLikeAdministratorUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(post("/api/users")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void addUserLikeSaleUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(post("/api/users")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void addUserLikeCustomerUser_returnStatusRedirect() throws Exception {
        mockMvc.perform(post("/api/users")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403"));
    }

    private AddUserDTO getValidAddUserDTO() {
        AddUserDTO addUser = new AddUserDTO();
        addUser.setEmail("test@api.com");
        addUser.setLastName(VALID_USER_LAST_NAME);
        addUser.setFirstName(VALID_USER_FIRST_NAME);
        addUser.setPatronymic(VALID_USER_PATRONYMIC);
        addUser.setRole(UserRoleDTOEnum.valueOf(VALID_USER_ROLE));
        return addUser;
    }

}