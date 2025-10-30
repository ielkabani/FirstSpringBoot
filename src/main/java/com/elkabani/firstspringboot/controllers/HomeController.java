package com.elkabani.firstspringboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String index() {
        return "redirect:/ui/users";
    }
   /* public String index(Model model){
        model.addAttribute("name", "Spring Boot User");
        return "index";
    }*/
}
