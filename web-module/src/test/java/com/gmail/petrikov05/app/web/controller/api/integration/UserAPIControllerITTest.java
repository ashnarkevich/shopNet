package com.gmail.petrikov05.app.web.controller.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.petrikov05.app.service.model.user.AddUserDTO;
import com.gmail.petrikov05.app.service.model.user.UserDTO;
import com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_FIRST_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_LAST_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_PATRONYMIC;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_ROLE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(roles = "SECURE_API_USER")
@TestPropertySource("/application-integration.properties")
public class UserAPIControllerITTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void addUser_returnAddedUser() throws Exception {
        AddUserDTO addUser = getValidAddUserDTO();
        String content = objectMapper.writeValueAsString(addUser);
        UserDTO returnedUser = getValidAddedUserDTO();
        String expectedResult = objectMapper.writeValueAsString(returnedUser);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }

    private UserDTO getValidAddedUserDTO() {
        UserDTO user = new UserDTO();
        user.setId(4L);
        user.setEmail("email@email.email");
        user.setLastName(VALID_USER_LAST_NAME);
        user.setFirstName(VALID_USER_FIRST_NAME);
        user.setPatronymic(VALID_USER_PATRONYMIC);
        user.setRole(UserRoleDTOEnum.valueOf(VALID_USER_ROLE));
        return user;
    }

    private AddUserDTO getValidAddUserDTO() {
        AddUserDTO addUserDTO = new AddUserDTO();
        addUserDTO.setEmail("email@email.email");
        addUserDTO.setRole(UserRoleDTOEnum.valueOf(VALID_USER_ROLE));
        addUserDTO.setLastName(VALID_USER_LAST_NAME);
        addUserDTO.setFirstName(VALID_USER_FIRST_NAME);
        addUserDTO.setPatronymic(VALID_USER_PATRONYMIC);
        return addUserDTO;
    }

}
