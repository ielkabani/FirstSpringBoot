package com.elkabani.firstspringboot.controllers;

import com.elkabani.firstspringboot.mappers.UserMapper;
import com.elkabani.firstspringboot.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
