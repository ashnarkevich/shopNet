package com.gmail.petrikov05.app.web.controller.web;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gmail.petrikov05.app.service.UserService;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.model.user.UserProfileDTO;
import com.gmail.petrikov05.app.web.config.TestConfig;
import com.gmail.petrikov05.app.web.controller.ProfileController;
import com.gmail.petrikov05.app.web.validator.PasswordValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.gmail.petrikov05.app.web.constant.MessageConstant.MESSAGE_CHANGE_DIFFERENT_PASSWORD_FAIL;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_ADDRESS;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_FIRST_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_ID;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_LAST_NAME;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_PASSWORD;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_PATRONYMIC;
import static com.gmail.petrikov05.app.web.constant.TestConstant.VALID_USER_PHONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
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

@WebMvcTest(controllers = ProfileController.class)
@Import(TestConfig.class)
@WithMockUser(roles = "CUSTOMER_USER")
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private PasswordValidator passwordValidator;

    @Test
    public void getProfile_returnStatusIsOk() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        mockMvc.perform(
                get("/profile")
        ).andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @Test
    public void getProfile_callBusinessLogic() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        mockMvc.perform(
                get("/profile")
        ).andExpect(status().isOk())
                .andExpect(view().name("profile"));
        verify(userService, times(1)).getUserProfile();
    }

    @Test
    public void getProfile_returnValidUserProfileDTO() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        mockMvc.perform(
                get("/profile")
        ).andExpect(model().attributeExists("user"));
    }

    @Test
    public void getProfile_returnUserProfileDTOWithValidId() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/profile")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ID));
    }

    @Test
    public void getProfile_returnUserProfileDTOWithValidLastName() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/profile")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_LAST_NAME);
    }

    @Test
    public void getProfile_returnUserProfileDTOWithValidFirstName() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/profile")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_FIRST_NAME);
    }

    @Test
    public void getProfile_returnUserProfileDTOWithValidPatronymic() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/profile")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_PATRONYMIC);
    }

    @Test
    public void getProfile_returnUserProfileDTOWithValidAddress() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/profile")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_ADDRESS);
    }

    @Test
    public void getProfile_returnUserProfileDTOWithValidPhone() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/profile")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_PHONE);
    }

    @Test
    public void getProfileAnonymousUser_redirectHomeWithMessage() throws Exception, AnonymousUserException {
        when(userService.getUserProfile()).thenThrow(new AnonymousUserException());
        mockMvc.perform(
                get("/profile")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("error"));
    }

    /* shop update profile page */
    @Test
    public void getProfileUpdatePage_returnStatusIsOk() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        mockMvc.perform(
                get("/profile/update")
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_update"));
    }

    @Test
    public void getProfileUpdatePage_callBusinessLogic() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        mockMvc.perform(
                get("/profile/update")
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_update"));
        verify(userService, times(1)).getUserProfile();
    }

    @Test
    public void getProfileUpdatePage_returnValidUserProfileDTO() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        mockMvc.perform(
                get("/profile/update")
        ).andExpect(model().attributeExists("user"));
    }

    @Test
    public void getProfileUpdatePage_returnUserProfileDTOWithValidId() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/profile/update")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(String.valueOf(VALID_ID));
    }

    @Test
    public void getProfileUpdatePage_returnUserProfileDTOWithValidLastName() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/profile/update")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_LAST_NAME);
    }

    @Test
    public void getProfileUpdatePage_returnUserProfileDTOWithValidFirstName() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/profile/update")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_FIRST_NAME);
    }

    @Test
    public void getProfileUpdatePage_returnUserProfileDTOWithValidPatronymic() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/profile/update")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_PATRONYMIC);
    }

    @Test
    public void getProfileUpdatePage_returnUserProfileDTOWithValidAddress() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/profile/update")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_ADDRESS);
    }

    @Test
    public void getProfileUpdatePage_returnUserProfileDTOWithValidPhone() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUserProfileDTO = getValidUserProfileDTO();
        when(userService.getUserProfile()).thenReturn(returnedUserProfileDTO);
        MvcResult mvcResult = mockMvc.perform(
                get("/profile/update")
        ).andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(VALID_USER_PHONE);
    }

    @Test
    public void getProfileUpdatePageWithAnonymousUser_redirectHomeWithMessage() throws Exception, AnonymousUserException {
        when(userService.getUserProfile()).thenThrow(new AnonymousUserException());
        mockMvc.perform(
                get("/profile/update")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("error"));
    }

    /* update profile */
    @Test
    public void updateProfile_redirectProfile() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUser = getValidUserProfileDTO();
        when(userService.updateProfile(any())).thenReturn(returnedUser);
        mockMvc.perform(
                post("/profile/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", VALID_USER_PATRONYMIC)
        ).andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @Test
    public void updateInvalidProfile_redirectProfileUpdate() throws Exception {
        mockMvc.perform(
                post("/profile/update")
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_update"));
    }

    @Test
    public void updateProfileWithInvalidLastName_redirectProfileUpdate() throws Exception {
        mockMvc.perform(
                post("/profile/update")
                        .param("lastName", "1a2a2")
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_update"));
    }

    @Test
    public void updateProfileWithSmallLastName_redirectProfileUpdate() throws Exception {
        mockMvc.perform(
                post("/profile/update")
                        .param("lastName", getValidStringByLength(1))
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_update"));
    }

    @Test
    public void updateProfileWithLongLastName_redirectProfileUpdate() throws Exception {
        mockMvc.perform(
                post("/profile/update")
                        .param("lastName", getValidStringByLength(41))
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_update"));
    }

    @Test
    public void updateProfileWithEmptyFirstName_redirectProfileUpdate() throws Exception {
        mockMvc.perform(
                post("/profile/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", "")
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_update"));
    }

    @Test
    public void updateProfileWithInvalidFirstName_redirectProfileUpdate() throws Exception {
        mockMvc.perform(
                post("/profile/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", "1d2d3")
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_update"));
    }

    @Test
    public void updateProfileWithSmallFirstName_redirectProfileUpdate() throws Exception {
        mockMvc.perform(
                post("/profile/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", getValidStringByLength(1))
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_update"));
    }

    @Test
    public void updateProfileWithLongFirstName_redirectProfileUpdate() throws Exception {
        mockMvc.perform(
                post("/profile/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", getValidStringByLength(21))
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_update"));
    }

    @Test
    public void updateProfileWithEmtyPatronymic_redirectProfileUpdate() throws Exception {
        mockMvc.perform(
                post("/profile/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", getValidStringByLength(21))
                        .param("patronymic", "")
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_update"));
    }

    @Test
    public void updateProfileWithSmallPatronymic_redirectProfileUpdate() throws Exception {
        mockMvc.perform(
                post("/profile/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", getValidStringByLength(1))
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_update"));
    }

    @Test
    public void updateProfileWithInvalidPatronymic_redirectProfileUpdate() throws Exception {
        mockMvc.perform(
                post("/profile/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", "1m1m1")
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_update"));
    }

    @Test
    public void updateProfileWithLongPatronymic_redirectProfileUpdate() throws Exception {
        mockMvc.perform(
                post("/profile/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", getValidStringByLength(41))
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_update"));
    }

    @Test
    public void updateProfile_callBusinessLogic() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUser = getValidUserProfileDTO();
        when(userService.updateProfile(any())).thenReturn(returnedUser);
        mockMvc.perform(
                post("/profile/update")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", VALID_USER_PATRONYMIC)
        ).andExpect(status().isOk())
                .andExpect(view().name("profile"));
        Mockito.verify(userService, times(1)).updateProfile(any());
    }

    @Test
    public void updateProfile_returnProfileWithMessage() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUser = getValidUserProfileDTO();
        when(userService.updateProfile(any())).thenReturn(returnedUser);
        mockMvc.perform(
                post("/profile/update")
                        .param("lastName", VALID_USER_LAST_NAME)
                        .param("firstName", VALID_USER_FIRST_NAME)
                        .param("patronymic", VALID_USER_PATRONYMIC)
        ).andExpect(status().isOk())
                .andExpect(model().attributeExists("message"));
    }

    /* get change password user profile page */
    @Test
    public void getChangePasswordPage_returnStatusOk() throws Exception {
        mockMvc.perform(
                get("/profile/changePass")
                        .param("newPassword", VALID_PASSWORD)
                        .param("confirmPassword", VALID_PASSWORD)
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_change_pass"));
    }

    @Test
    public void getChangePasswordPage_returnNewPasswordDTO() throws Exception {
        mockMvc.perform(
                get("/profile/changePass")
                        .param("newPassword", VALID_PASSWORD)
                        .param("confirmPassword", VALID_PASSWORD)
        ).andExpect(status().isOk())
                .andExpect(model().attributeExists("password"));
    }

    /* change password user profile */
    @Test
    public void changePassword_returnProfile() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUser = getValidUserProfileDTO();
        when(userService.changePassword(anyString())).thenReturn(returnedUser);
        mockMvc.perform(
                post("/profile/changePass")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("newPassword", VALID_PASSWORD)
                        .param("confirmPassword", VALID_PASSWORD)
        ).andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @Test
    public void changePasswordWithEmptyPasswordConfirm_returnProfileChangePass() throws Exception {
        mockMvc.perform(
                post("/profile/changePass")
                        .param("newPassword", VALID_PASSWORD)
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_change_pass"));
    }

    @Test
    public void changePasswordWithEmptyPassword_returnProfileChangePass() throws Exception {
        mockMvc.perform(
                post("/profile/changePass")
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_change_pass"));
    }

    @Test
    public void changePasswordWithInvalidPassword_returnProfileChangePass() throws Exception {
        mockMvc.perform(
                post("/profile/changePass")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("newPassword", " ")
                        .param("confirmPassword", VALID_PASSWORD)
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_change_pass"));
    }

    @Test
    public void changePasswordWithInvalidPasswordConfirm_returnProfileChangePass() throws Exception {
        mockMvc.perform(
                post("/profile/changePass")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("newPassword", VALID_PASSWORD)
                        .param("confirmPassword", " ")
        ).andExpect(status().isOk())
                .andExpect(view().name("profile_change_pass"));
    }

    @Test
    public void changePasswordWithDifferentPassword_redirectProfileChangePass() throws Exception, AnonymousUserException {
        doAnswer(new CallsRealMethods()).when(passwordValidator).validate(any(), any());
        MvcResult mvcResult = mockMvc.perform(
                post("/profile/changePass")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("newPassword", VALID_PASSWORD)
                        .param("confirmPassword", "otherPass")
        ).andExpect(view().name("profile_change_pass"))
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).contains(MESSAGE_CHANGE_DIFFERENT_PASSWORD_FAIL);
    }

    @Test
    public void changePassword_callBusinessLogic() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUser = getValidUserProfileDTO();
        when(userService.changePassword(anyString())).thenReturn(returnedUser);
        mockMvc.perform(
                post("/profile/changePass")
                        .param("newPassword", VALID_PASSWORD)
                        .param("confirmPassword", VALID_PASSWORD)
        ).andExpect(status().isOk());
        verify(userService, times(1)).changePassword(VALID_PASSWORD);
    }

    @Test
    public void changePassword_returnProfileWithMessage() throws Exception, AnonymousUserException {
        UserProfileDTO returnedUser = getValidUserProfileDTO();
        when(userService.changePassword(anyString())).thenReturn(returnedUser);
        mockMvc.perform(
                post("/profile/changePass")
                        .param("newPassword", VALID_PASSWORD)
                        .param("confirmPassword", VALID_PASSWORD)
        ).andExpect(status().isOk())
                .andExpect(model().attributeExists("message"));
    }

    @Test
    public void changePassword_redirectHomeWithError() throws Exception, AnonymousUserException {
        when(userService.changePassword(anyString())).thenThrow(new AnonymousUserException());
        mockMvc.perform(
                post("/profile/changePass")
                        .param("newPassword", VALID_PASSWORD)
                        .param("confirmPassword", VALID_PASSWORD)
        ).andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("error"));
    }

    private String getValidStringByLength(int length) {
        return IntStream.range(0, length)
                .mapToObj(x -> "a")
                .collect(Collectors.joining());
    }

    private UserProfileDTO getValidUserProfileDTO() {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId(VALID_ID);
        userProfileDTO.setLastName(VALID_USER_LAST_NAME);
        userProfileDTO.setFirstName(VALID_USER_FIRST_NAME);
        userProfileDTO.setPatronymic(VALID_USER_PATRONYMIC);
        userProfileDTO.setAddress(VALID_USER_ADDRESS);
        userProfileDTO.setPhone(VALID_USER_PHONE);
        return userProfileDTO;
    }

}