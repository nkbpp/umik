package ru.pfr.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pfr.configuration.UserPrincipal;
import ru.pfr.global.DateUtils;
import ru.pfr.model.umikbd.Adminparam;
import ru.pfr.model.umikbd.Logi;
import ru.pfr.model.umikbd.User;
import ru.pfr.service.bdumik.AdminparamService;
import ru.pfr.service.bdumik.LogiService;
import ru.pfr.service.bdumik.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/umik")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private LogiService logiService;

    @Autowired
    private AdminparamService adminparamService;

    @GetMapping("/admin")
    public String adminstart(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model
    ) {

        User user = userPrincipal.getUser();
        model.addAttribute("user", user);

        Adminparam adminparam = adminparamService.findByAdminparam();
        model.addAttribute("adminparam", adminparam);

        List<User> logerrs = userService.findAll();
        model.addAttribute("logerrs", logerrs);

        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "Страница администратора"));
        return "admin";
    }

    @GetMapping("/adminupdate")
    public String adminupdate(
            @RequestParam Long kolpopitok,
            @RequestParam Long koefpopitok,
            @RequestParam Long block,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model
    ) {
        User user = userPrincipal.getUser();
        model.addAttribute("user", user);

        Adminparam adminparam = new Adminparam(kolpopitok, koefpopitok, block);
        adminparamService.save(adminparam);

        model.addAttribute("adminparam", adminparam);
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "Изменение параметров в adminupdate"));
        return "admin";
    }

    @GetMapping("/juraudit")
    public String juraudit(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model
    ) {
        User user = userPrincipal.getUser();
        model.addAttribute("user", user);

        Iterable<Logi> logi = logiService.findAll();
        model.addAttribute("logi", logi);
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "Вход в журнал"));
        return "juraudit";
    }

    @GetMapping("/juraudit/tables")
    public String table(
            @RequestParam String d1,
            @RequestParam String d2,
            @RequestParam String login,
            @RequestParam String type,
            @RequestParam String text,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model
    ) {
        User user = userPrincipal.getUser();
        LocalDateTime date1 = null;
        LocalDateTime date2 = null;

        // Парсинг дат
        try {
            date1 = DateUtils.parseIsoToDate(d1);
            date2 = DateUtils.parseIsoToDate(d2);
        } catch (Exception e) {
            // Логирование ошибки парсинга
            System.err.println("Ошибка парсинга дат: " + e.getMessage());
        }

        model.addAttribute("user", user);

        Long type2 = null;
        try {
            type2 = Long.valueOf(type);
        } catch (NumberFormatException e) {
            // Логирование ошибки преобразования типа
            System.err.println("Ошибка преобразования типа: " + e.getMessage());
        }

        // Проверка на пустые значения
        login = login.isEmpty() ? null : login;
        text = text.isEmpty() ? null : text;

        List<Logi> logi;

        // Условие поиска логов
        if (date1 == null || date2 == null) {
            logi = (login == null && type2 == null && text == null)
                    ? logiService.findAll()
                    : logiService.findByDateBetween(login, type2, text);
        } else {
            logi = logiService.findByDateBetween(date1, date2, login, type2, text);
        }

        // Добавление логов и сохранение действия
        model.addAttribute("logi", logi);
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "Фильтр журнала"));

        return "fragmentadmin/adminfragment :: tables";
    }

    @GetMapping("/juraudit/clear")
    public String clear(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model
    ) {
        User user = userPrincipal.getUser();
        logiService.clear();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "Очистка журнала"));
        Iterable<Logi> logi = logiService.findAll();
        model.addAttribute("logi", logi);

        return "fragmentadmin/adminfragment :: tables";
    }


    @GetMapping("/vihod/logout")
    public void logout(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "Выход"));

    }

    @GetMapping("/admin/clear")
    public String admintwoclear(
            @RequestParam Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model
    ) {
        User user = userPrincipal.getUser();
        User logerr = userService.findById(id);
        logerr.setActive(0L);
        logerr.setDate(LocalDateTime.now());
        userService.save(logerr);

        List<User> logerrs = userService.findAll();
        model.addAttribute("logerrs", logerrs);

        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "Снять блокировку пользователя " + logerr.getLogin()));

        return "fragmentadmin/adminfragment :: blocks";
    }


    @GetMapping("/admin/zablock")
    public String admintwozablock(
            @RequestParam Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model
    ) {
        User user = userPrincipal.getUser();
        User logerr = userService.findById(id);
        logerr.setActive(1000L);
        logerr.setDate(LocalDateTime.now());
        userService.save(logerr);

        List<User> logerrs = userService.findAll();
        model.addAttribute("logerrs", logerrs);

        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "Заблокировать пользователя " + logerr.getLogin()));

        return "fragmentadmin/adminfragment :: blocks";
    }

}
