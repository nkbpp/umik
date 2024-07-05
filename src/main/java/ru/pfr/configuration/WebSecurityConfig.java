package ru.pfr.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.pfr.service.bdumik.UserService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()//включить авторизацию
                .antMatchers("**/*.html").permitAll()
                .antMatchers("**/*.css").permitAll()
                .antMatchers("**/*.js").permitAll()
                .anyRequest().authenticated()//для остальных авторизация
                .and()
                .formLogin()
                .loginPage("/umik/login")//маппинг логина
                .permitAll()//разрешаем пользоваться всем
                .and()
                .logout()//включаем логаут
                .permitAll();//разрешаем пользоваться всем
    }

}
