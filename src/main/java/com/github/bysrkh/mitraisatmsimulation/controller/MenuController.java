package com.github.bysrkh.mitraisatmsimulation.controller;

import com.github.bysrkh.mitraisatmsimulation.domain.Menu;
import com.github.bysrkh.mitraisatmsimulation.dto.MenuDto;
import com.github.bysrkh.mitraisatmsimulation.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_WITHDRAWAL;

@Controller
@RequestMapping("/menu")
public class MenuController {
    private MenuService menuService;

    @ModelAttribute("transactionOptions")
    public Map<Integer, String> getTransactionOptions() {

        return new Menu().getTransactionOption();
    }

    @ModelAttribute("withdrawalOptions")
    public Map<Integer, String> getWithdrawalOptions() {

        return new Menu().getWithdrawalOption();
    }

    @GetMapping("/transaction")
    public String showTransactionForm(@ModelAttribute("menu") MenuDto menu) {
        menu.setTransactionOption(TO_WITHDRAWAL);

        System.out.println("Rizka Julia");

        return "menu/transactionMenuForm";
    }

    @GetMapping("/Withdraw")
    public String showWithdrawalForm(@ModelAttribute("menu") MenuDto menu) {

        return "menu/witdhrawalMenuForm";
    }


    @PostMapping("/Withdraw")
    public String selectWitdhrawalForm(@ModelAttribute("menu") MenuDto menu, RedirectAttributes model) {
        final String choosenTransaction = menuService.chooseWithdrawOption(menu);
        model.addAttribute(menu.getWithdrawalOption());

        return "redirect:/Withdraw";
    }


    @PostMapping("/transaction")
    public String selectTransactionForm(@ModelAttribute("menu") MenuDto menu) {
        final String choosenTransaction = menuService.choosenTransactionOption(menu);

        return "redirect:/menu/" + choosenTransaction;
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }
}
