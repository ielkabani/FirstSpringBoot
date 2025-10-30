package com.elkabani.firstspringboot.controllers;

import com.elkabani.firstspringboot.dtos.RegisterUserRequest;
import com.elkabani.firstspringboot.mappers.UserMapper;
import com.elkabani.firstspringboot.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
@RequestMapping("/ui/users")
public class UserUIController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public String listUsers(Model model, @RequestParam(defaultValue="name") String sort) {
        var users = userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto)  // this is a short for map(user -> userMapper.toDto(user))
                .toList();
        model.addAttribute("users", users);
        return "users/list";
    }
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new RegisterUserRequest());
        model.addAttribute("isEdit", false);
        return "users/form";
    }
    @PostMapping
    public String createUser(@ModelAttribute RegisterUserRequest request, RedirectAttributes redirectAttributes) {
        var user = userMapper.toEntity(request);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
        return "redirect:/ui/users";
    }

    @GetMapping("/{id}")
    public String viewUser(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found!");
            return "redirect:/ui/users";
        }
        model.addAttribute("user", userMapper.toDto(user));
        return "users/view";
    }
}
