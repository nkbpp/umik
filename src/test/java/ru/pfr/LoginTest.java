package ru.pfr;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class) //junit
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Ignore
    public void contextLoad() throws Exception{
        this.mockMvc.perform(get("/umik/login"))
                .andDo(print())//выводить результат в консоль
                .andExpect(status().isOk())//статус 200 http
                .andExpect(content().string(containsString("Логин")));//Содержится строка
    }

    @Test
    @Ignore
    public void accesDeniedTest() throws Exception{
        this.mockMvc.perform(get("/umik/logi")) //нет авторизации или нет страницы
                .andDo(print())//выводить результат в консоль
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/umik/login"));
    }

    @Test
    @Ignore
    public void correctLoginTest() throws Exception{
        this.mockMvc.perform(formLogin().user("041-0831").password("db2admin"))
                .andDo(print())//выводить результат в консоль
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/umik/main"));
    }

    @Test
    @Ignore
    public void badCredentials() throws Exception{
        this.mockMvc.perform(post("/umik/login").param("login", "041-0831"))
                .andDo(print())//выводить результат в консоль
                .andExpect(status().isForbidden());//403

    }

}
