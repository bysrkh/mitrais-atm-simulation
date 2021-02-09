package com.github.bysrkh.mitraisatmsimulation.controller;

import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String showLogin(Account account) {

        return "login/loginForm";
    }
}
