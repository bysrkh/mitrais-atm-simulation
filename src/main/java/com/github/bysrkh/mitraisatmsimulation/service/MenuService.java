package com.github.bysrkh.mitraisatmsimulation.service;

import com.github.bysrkh.mitraisatmsimulation.domain.Menu;
import com.github.bysrkh.mitraisatmsimulation.dto.MenuDto;
import org.springframework.stereotype.Service;

@Service
public class MenuService {
    private Menu menu = new Menu();

    public String choosenTransactionOption(MenuDto menuDto) {

        return menu.getTransactionOption().get(menuDto.getTransactionOption());
    }

    public String chooseWithdrawOption(MenuDto menuDto) {

        return menu.getWithdrawalOption().get(menuDto.getWithdrawalOption());
    }
}
