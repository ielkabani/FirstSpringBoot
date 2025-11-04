package com.elkabani.firstspringboot.controllers;

import com.elkabani.firstspringboot.dtos.RegisterUserRequest;
import com.elkabani.firstspringboot.dtos.UpdateUserRequest;
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

    // Show edit user form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found!");
            return "redirect:/ui/users";
        }
        var updateRequest = new UpdateUserRequest();
        updateRequest.setName(user.getName());
        updateRequest.setEmail(user.getEmail());
        model.addAttribute("user", updateRequest);
        model.addAttribute("userId", id);
        model.addAttribute("isEdit", true);
        return "users/form";
    }

    // Handle update user form submission
    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UpdateUserRequest request,
                             RedirectAttributes redirectAttributes) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found!");
            return "redirect:/ui/users";
        }
        userMapper.update(request, user);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
        return "redirect:/ui/users/" + id;
    }

    // Handle delete user
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found!");
            return "redirect:/ui/users";
        }
        userRepository.delete(user);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        return "redirect:/ui/users";
    }
}
