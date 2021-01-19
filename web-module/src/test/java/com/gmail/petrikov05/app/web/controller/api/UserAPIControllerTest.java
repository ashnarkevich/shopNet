package com.gmail.petrikov05.app.web.controller.api;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.petrikov05.app.service.UserService;
import com.gmail.petrikov05.app.service.exception.UserExistenceException;
import com.gmail.petrikov05.app.service.model.user.AddUserDTO;
import com.gmail.petrikov05.app.service.model.user.UserDTO;
import com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum;
import com.gmail.petrikov05.app.web.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.service.constant.ValidationMessages.MESSAGE_NOT_EMPTY;
import static com.gmail.petrikov05.app.service.constant.ValidationMessages.MESSAGE_REGEX_EMAIL;
import static com.gmail.petrikov05.app.service.constant.ValidationMessages.MESSAGE_REGEX_NAME;
import static com.gmail.petrikov05.app.service.constant.ValidationMessages.MESSAGE_SIZE_FIRST_NAME;
import static com.gmail.petrikov05.app.service.constant.ValidationMessages.MESSAGE_SIZE_LAST_NAME;
import static com.gmail.petrikov05.app.service.constant.ValidationMessages.MESSAGE_SIZE_MAX_EMAIL;
import static com.gmail.petrikov05.app.service.constant.ValidationMessages.MESSAGE_SIZE_PATRONYMIC;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_EMAIL;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_FIRST_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_LAST_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_PATRONYMIC;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_ROLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserAPIController.class)
@WithMockUser(roles = "SECURE_API_USER")
@Import(TestConfig.class)
class UserAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    /* add user */
    @Test
    void addUser_returnStatusCreated() throws Exception {
        AddUserDTO addUserDTO = getValidAddUserDTO();
        String content = objectMapper.writeValueAsString(addUserDTO);
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andExpect(status().isCreated());
    }

    @Test
    void addInvalidUser_returnStatusBadRequest() throws Exception {
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void addUserWithInvalidEmail_returnError() throws Exception {
        AddUserDTO addUserDTO = getValidAddUserDTO();
        addUserDTO.setEmail("not_email");
        String content = objectMapper.writeValueAsString(addUserDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(MESSAGE_REGEX_EMAIL);
    }

    @Test
    void addUserWithEmptyEmail_returnError() throws Exception {
        AddUserDTO addUserDTO = getValidAddUserDTO();
        addUserDTO.setEmail("");
        String content = objectMapper.writeValueAsString(addUserDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(MESSAGE_NOT_EMPTY);
    }

    @Test
    void addUserWithLongEmail_returnError() throws Exception {
        AddUserDTO addUserDTO = getValidAddUserDTO();
        String longEmail = getStringWithLength(42) + "@mail.com";
        addUserDTO.setEmail(longEmail);
        String content = objectMapper.writeValueAsString(addUserDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(MESSAGE_SIZE_MAX_EMAIL);
    }

    @Test
    void addUserWithInvalidLastName_returnError() throws Exception {
        AddUserDTO addUserDTO = getValidAddUserDTO();
        addUserDTO.setLastName("invalid last name");
        String content = objectMapper.writeValueAsString(addUserDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(MESSAGE_REGEX_NAME);
    }

    @Test
    void addUserWithEmptyLastName_returnError() throws Exception {
        AddUserDTO addUserDTO = getValidAddUserDTO();
        addUserDTO.setLastName("");
        String content = objectMapper.writeValueAsString(addUserDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(MESSAGE_NOT_EMPTY);
    }

    @Test
    void addUserWithLongLastName_returnError() throws Exception {
        AddUserDTO addUserDTO = getValidAddUserDTO();
        addUserDTO.setLastName(getStringWithLength(41));
        String content = objectMapper.writeValueAsString(addUserDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(MESSAGE_SIZE_LAST_NAME);
    }

    @Test
    void addUserWithInvalidFirstName_returnError() throws Exception {
        AddUserDTO addUserDTO = getValidAddUserDTO();
        addUserDTO.setFirstName("invalid first name");
        String content = objectMapper.writeValueAsString(addUserDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(MESSAGE_REGEX_NAME);
    }

    @Test
    void addUserWithEmptyFirstName_returnError() throws Exception {
        AddUserDTO addUserDTO = getValidAddUserDTO();
        addUserDTO.setFirstName("");
        String content = objectMapper.writeValueAsString(addUserDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(MESSAGE_NOT_EMPTY);
    }

    @Test
    void addUserWithLongFirstName_returnError() throws Exception {
        AddUserDTO addUserDTO = getValidAddUserDTO();
        addUserDTO.setFirstName(getStringWithLength(21));
        String content = objectMapper.writeValueAsString(addUserDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(MESSAGE_SIZE_FIRST_NAME);
    }

    @Test
    void addUserWithInvalidPatronymic_returnError() throws Exception {
        AddUserDTO addUserDTO = getValidAddUserDTO();
        addUserDTO.setPatronymic("qw21");
        String content = objectMapper.writeValueAsString(addUserDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(MESSAGE_REGEX_NAME);
    }

    @Test
    void addUserWithEmptyPatronymic_returnError() throws Exception {
        AddUserDTO addUserDTO = getValidAddUserDTO();
        addUserDTO.setPatronymic("");
        String content = objectMapper.writeValueAsString(addUserDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(MESSAGE_SIZE_PATRONYMIC);
    }

    @Test
    void addUserWithLongPatronymic_returnError() throws Exception {
        AddUserDTO addUserDTO = getValidAddUserDTO();
        addUserDTO.setPatronymic(getStringWithLength(41));
        String content = objectMapper.writeValueAsString(addUserDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(MESSAGE_SIZE_PATRONYMIC);
    }

    @Test
    void addUser_callBusinessLogic() throws Exception, UserExistenceException {
        AddUserDTO addUserDTO = getValidAddUserDTO();
        String content = objectMapper.writeValueAsString(addUserDTO);
        mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andExpect(status().isCreated());
        verify(userService, times(1)).addUser(any());
    }

    @Test
    void addUser_returnValidUser() throws Exception, UserExistenceException {
        AddUserDTO addUser = getValidAddUserDTO();
        String content = objectMapper.writeValueAsString(addUser);
        UserDTO returnedUser = getValidUserDTO();
        when(userService.addUser(any())).thenReturn(returnedUser);
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String expectedResult = objectMapper.writeValueAsString(returnedUser);
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void addExistenceUser_returnErrorMessage() throws Exception, UserExistenceException {
        AddUserDTO addUser = getValidAddUserDTO();
        String content = objectMapper.writeValueAsString(addUser);
        String errorMessage = "user with this email already exists";
        when(userService.addUser(any())).thenThrow(new UserExistenceException(errorMessage));
        MvcResult mvcResult = mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(errorMessage);
    }

    private UserDTO getValidUserDTO() {
        UserDTO user = new UserDTO();
        user.setId(VALID_ID);
        user.setEmail(VALID_EMAIL);
        user.setLastName(VALID_USER_LAST_NAME);
        user.setFirstName(VALID_USER_FIRST_NAME);
        user.setPatronymic(VALID_USER_PATRONYMIC);
        user.setRole(UserRoleDTOEnum.valueOf(VALID_USER_ROLE));
        return user;
    }

    private String getStringWithLength(int length) {
        return IntStream.range(0, length)
                .mapToObj(x -> "a")
                .collect(Collectors.joining());
    }

    private AddUserDTO getValidAddUserDTO() {
        AddUserDTO addUserDTO = new AddUserDTO();
        addUserDTO.setEmail(VALID_EMAIL);
        addUserDTO.setRole(UserRoleDTOEnum.valueOf(VALID_USER_ROLE));
        addUserDTO.setLastName(VALID_USER_LAST_NAME);
        addUserDTO.setFirstName(VALID_USER_FIRST_NAME);
        addUserDTO.setPatronymic(VALID_USER_PATRONYMIC);
        return addUserDTO;
    }

}