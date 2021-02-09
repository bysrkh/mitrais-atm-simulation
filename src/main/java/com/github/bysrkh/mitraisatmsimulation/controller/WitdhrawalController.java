package com.github.bysrkh.mitraisatmsimulation.controller;

import com.github.bysrkh.mitraisatmsimulation.dto.MenuDto;
import com.github.bysrkh.mitraisatmsimulation.service.WithdrawalService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/Withdraw")
@Controller
public class WitdhrawalController {
    private WithdrawalService withdrawalService;

    @GetMapping
    public String chooseWithdraw(@ModelAttribute("menu") MenuDto menu, RedirectAttributes model) {
        final String navigateTo = withdrawalService.withdrawFixedBalance(menu);

        model.addAttribute("random", navigateTo);
        return "redirect:/" + navigateTo;
    }
}
