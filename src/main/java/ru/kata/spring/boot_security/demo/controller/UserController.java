package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    private final RoleService roleService;


    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("admin")
    public String infoAdmin(ModelMap modelMap) {
        modelMap.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("user")
    public String infoUser(@AuthenticationPrincipal User user, ModelMap modelMap) {
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("roles", user.getRoles());
        return "user";
    }

    @GetMapping(value = "admin/user/add")
    public String addUser(ModelMap model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "new_user";
    }

    @PostMapping(value = "admin/user/add")
    public String addUser(@ModelAttribute User user,
                          @RequestParam(value = "roless") String[] roles) {
        Set<Role> rolesSet = new HashSet<>();
        for (String role : roles) {
            rolesSet.add(roleService.getRole(role));
        }
        user.setRoles(rolesSet);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "admin/user/infoUser/{id}")
    public String showUser(@PathVariable("id") long id, ModelMap model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", user.getRoles());
        return "show_user";
    }

    @GetMapping(value = "admin/user/updateUser/{id}")
    public String editUser(@PathVariable("id") long id, ModelMap model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "edit_user";
    }

    @PostMapping(value = "admin/user/updateUser/{id}")
    public String editUser(@ModelAttribute User user, @RequestParam(value = "roless") String[] roles) {
        Set<Role> rolesSet = new HashSet<>();
        for (String role : roles) {
            rolesSet.add(roleService.getRole(role));
        }
        user.setRoles(rolesSet);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "admin/user/removeUser/{id}")
    public String removeUser(@PathVariable("id") long id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(){
        return "login";
    }

}
