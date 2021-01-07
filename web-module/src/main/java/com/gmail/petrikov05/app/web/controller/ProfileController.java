package com.gmail.petrikov05.app.web.controller;

import javax.validation.Valid;

import com.gmail.petrikov05.app.service.UserService;
import com.gmail.petrikov05.app.service.exception.AnonymousUserException;
import com.gmail.petrikov05.app.service.model.user.PasswordDTO;
import com.gmail.petrikov05.app.service.model.user.UpdateUserProfileDTO;
import com.gmail.petrikov05.app.service.model.user.UserProfileDTO;
import com.gmail.petrikov05.app.web.validator.PasswordValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final PasswordValidator passwordValidator;

    public ProfileController(
            UserService userService,
            PasswordValidator passwordValidator
    ) {
        this.userService = userService;
        this.passwordValidator = passwordValidator;
    }

    @GetMapping
    public String showProfilePage(
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        try {
            UserProfileDTO user = userService.getUserProfile();
            model.addAttribute("user", user);
            return "profile";
        } catch (AnonymousUserException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/update")
    public String showUpdateProfilePage(
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        try {
            UserProfileDTO user = userService.getUserProfile();
            model.addAttribute("user", user);
            return "profile_update";
        } catch (AnonymousUserException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/update")
    public String updateProfile(
            @ModelAttribute(name = "user") @Valid UpdateUserProfileDTO updateUserProfileDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "profile_update";
        }
        try {
            UserProfileDTO user = userService.updateProfile(updateUserProfileDTO);
            model.addAttribute("user", user);
            model.addAttribute("message", "User profile updated");
            return "profile";
        } catch (AnonymousUserException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/changePass")
    public String showChangePasswordPage(
            Model model
    ) {
        model.addAttribute("password", new PasswordDTO());
        return "profile_change_pass";
    }

    @PostMapping("/changePass")
    public String changePassword(
            @ModelAttribute(name = "password") @Valid PasswordDTO passwordDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        passwordValidator.validate(passwordDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("password", passwordDTO);
            return "profile_change_pass";
        }
        try {
            UserProfileDTO user = userService.changePassword(passwordDTO.getNewPassword());
            model.addAttribute("message", "The password changed");
            model.addAttribute("user", user);
            return "profile";
        } catch (AnonymousUserException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

}
