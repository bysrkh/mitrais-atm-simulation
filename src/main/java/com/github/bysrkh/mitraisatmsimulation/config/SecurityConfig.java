package com.github.bysrkh.mitraisatmsimulation.config;

import com.github.bysrkh.mitraisatmsimulation.repository.AccountRepository;
import com.github.bysrkh.mitraisatmsimulation.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private AccountRepository accountRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .usernameParameter("accountNumber").passwordParameter("pin")
                .loginPage("/login")
                .defaultSuccessUrl("/menu/transaction")
                .and()
                .rememberMe().tokenValiditySeconds(600)
                .and()
                .authorizeRequests()
                .antMatchers("/menu/**").hasAnyRole("USER")
                .antMatchers("/transaction/**").hasAnyRole("USER")
                .antMatchers("/Withdraw/**").hasAnyRole("USER")
                .anyRequest().permitAll()
                .and()
                .logout().logoutUrl("/logout")
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsServiceImpl(accountRepository));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    public void setUserRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
