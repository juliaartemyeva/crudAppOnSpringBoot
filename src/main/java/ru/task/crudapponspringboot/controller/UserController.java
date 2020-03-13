package ru.task.crudapponspringboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.task.crudapponspringboot.model.Role;
import ru.task.crudapponspringboot.model.User;
import ru.task.crudapponspringboot.service.UserService;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ModelAndView getUserPage(Authentication authentication, ModelAndView model) {
        User user = userService.findUserByName(authentication.getName());
        model.addObject("user", user);
        model.setViewName("user-page");
        return model;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView allUsers(ModelAndView modelAndView) {
        List<User> allUser = userService.findAll();
        modelAndView.setViewName("admin-page");
        modelAndView.addObject("listUser", allUser);
        return modelAndView;
    }

    @GetMapping("/admin/new")
    public ModelAndView addUser(ModelAndView modelAndView) {
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("edit-page");
        return modelAndView;
    }
    @RequestMapping(value = "/admin/edit", method = RequestMethod.GET)
    public ModelAndView editPage(@RequestParam("id") Long id, ModelAndView modelAndView) {
        User user = userService.findById(id);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("edit-page");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/update", method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("user") User user, HttpServletRequest request) {
        Set<Role> roles = user.getRoles();
        String RoleUser = request.getParameter("user");
        String RoleAdmin = request.getParameter("admin");
        if (RoleUser != null) {
            roles.add(Role.USER);
        }
        if (RoleAdmin != null) {
            roles.add(Role.ADMIN);
        }
        user.setRoles(roles);
        userService.saveOrUpdate(user);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/delete", method = RequestMethod.GET)
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}