package com.gmail.petrikov05.app.web.controler;

import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.validation.Valid;

import com.gmail.petrikov05.app.service.UserService;
import com.gmail.petrikov05.app.service.exception.AdministratorChangingException;
import com.gmail.petrikov05.app.service.exception.UserExistenceException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.user.AddUserDTO;
import com.gmail.petrikov05.app.service.model.user.UserDTO;
import com.gmail.petrikov05.app.service.model.user.UserRoleDTOEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public UserController(UserService userService) {this.userService = userService;}

    @GetMapping
    public String showUsersPage(
            @RequestParam(defaultValue = "1") Integer page,
            Model model
    ) {
        PaginationWithEntitiesDTO<UserDTO> paginationWithEntitiesDTO = userService.getUsersByPage(page);
        List<UserDTO> users = paginationWithEntitiesDTO.getEntities();
        int pages = paginationWithEntitiesDTO.getPages();
        model.addAttribute("users", users);
        model.addAttribute("page", page);
        model.addAttribute("pages", pages);
        logger.trace("show users page");
        return "users";
    }

    @PostMapping("/delete")
    public String deleteUsers(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "id", defaultValue = "") List<Long> ids,
            RedirectAttributes redirect
    ) {
        if (ids.isEmpty()) {
            redirect.addFlashAttribute("message", "didn't check");
        } else {
            try {
                List<String> emails = userService.deleteUsers(ids);
                String strEmails = getStrEmails(emails);
                logger.trace("delete users");
                redirect.addFlashAttribute("message", strEmails + "deleted");
            } catch (UserExistenceException e) {
                logger.warn(e + "deleting users failed");
                redirect.addFlashAttribute("message", e.getMessage() + " : deleting users failed");
            } catch (AdministratorChangingException e) {
                logger.warn(e + "deleting users failed");
                redirect.addFlashAttribute("message", e.getMessage());
            }
        }
        return "redirect:/users?page=" + page;
    }

    @GetMapping("/{id}/updateRole")
    public String updateRole(
            @PathVariable Long id,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "role") UserRoleDTOEnum role,
            RedirectAttributes redirect
    ) {
        try {
            UserDTO user = userService.updateRole(id, role);
            redirect.addFlashAttribute(
                    "message",
                    "user (" + user.getEmail() + ") updated. Enter role: " + user.getRole());
        } catch (UserExistenceException e) {
            logger.warn(e.getMessage());
            redirect.addFlashAttribute("message", "user not found. try again");
        } catch (AdministratorChangingException e) {
            logger.warn(e.getMessage());
            redirect.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users?page=" + page;
    }

    @GetMapping("/{id}/changePass")
    public String changePassword(
            @PathVariable Long id,
            @RequestParam(name = "page") Integer page,
            RedirectAttributes redirect
    ) {
        try {
            UserDTO user = userService.changePassword(id);
            redirect.addFlashAttribute("message", "A password changed. A new password sent on the email: " + user.getEmail());
        } catch (UserExistenceException e) {
            logger.warn(e.getMessage());
            redirect.addFlashAttribute("message", "A password didn't change.");
        } catch (AdministratorChangingException e) {
            logger.warn(e.getMessage());
            redirect.addFlashAttribute("message", "A password didn't change super administrator.");
        }
        return "redirect:/users?page=" + page;
    }

    @GetMapping("/add")
    public String showAddUser(
            Model model
    ) {
        model.addAttribute("user", new AddUserDTO());
        model.addAttribute("roles", UserRoleDTOEnum.values());
        return "user_add";
    }

    @PostMapping("/add")
    public String addUser(
            @ModelAttribute(name = "user") @Valid AddUserDTO user,
            BindingResult bindingResult,
            RedirectAttributes redirect,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("model", user);
            model.addAttribute("roles", UserRoleDTOEnum.values());
            return "user_add";
        }
        try {
            UserDTO addedUser = userService.addUser(user);
            redirect.addFlashAttribute("message", "User with email (" + addedUser.getEmail() + ") added");
            return "redirect:/users";
        } catch (UserExistenceException e) {
            logger.info("user with this email (" + user.getEmail() + ") exists");
            model.addAttribute("user", user);
            model.addAttribute("error", "user with email (" + user.getEmail() + ") exists");
            model.addAttribute("roles", UserRoleDTOEnum.values());
            return "user_add";
        }
    }

    private String getStrEmails(List<String> emails) {
        StringBuilder string = new StringBuilder();
        for (String email : emails) {
            string.append(email).append(" ");
        }
        return string.toString();
    }

}
