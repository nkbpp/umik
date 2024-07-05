package ru.pfr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pfr.configuration.UserPrincipal;
import ru.pfr.global.DateUtils;
import ru.pfr.model.umikbd.Logi;
import ru.pfr.model.umikbd.User;
import ru.pfr.service.bdumik.LogiService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/umik/main")
public class OpfrController {

    @Autowired
    private LogiService logiService;

    @GetMapping()
    public String startupfr(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "Главная страница startupfr"));

        model.addAttribute("user", user);
        return "main";
    }


    @GetMapping("/avansotch")
    public String avansotch(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "Вход в авансовый отчет avansotch"));

        model.addAttribute("user", user);
        return "avansotch";
    }


    @GetMapping("/konv")
    public String konv(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "konv"));

        model.addAttribute("user", user);
        return "konv";
    }


    @GetMapping("/history")
    public String history(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "history param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        LocalDateTime date1;
        LocalDateTime date2;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (!(dat1.isEmpty() && dat2.isEmpty())) {
            // Если входные даты не пустые, преобразуем их в LocalDateTime
            date1 = LocalDate.parse(dat1, formatter).atStartOfDay();
            date2 = LocalDate.parse(dat2, formatter).atTime(23, 59, 59);
        } else {
            // Если входные даты пустые, используем текущий месяц
            date1 = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            date2 = date1.plusMonths(1).minusSeconds(1);
        }

        model.addAttribute("date1", DateUtils.formatIsoToString(date1));
        model.addAttribute("date2", DateUtils.formatIsoToString(date2));

        model.addAttribute("dat1", DateUtils.formatToString(date1));
        model.addAttribute("dat2", DateUtils.formatToString(date2));

        model.addAttribute("user", user);
        return "history";
    }


}