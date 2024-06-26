package ru.pfr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pfr.global.DateUtils;
import ru.pfr.model.umikbd.Logi;
import ru.pfr.model.umikbd.Prihodmarki;
import ru.pfr.model.umikbd.User;
import ru.pfr.service.bdumik.LogiService;
import ru.pfr.service.bdumik.PrihodmarkiService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/umik/main/prihodmarki")
public class PrihodMarkiController {

    @Autowired
    private PrihodmarkiService prihodmarkiService;

    @Autowired
    private LogiService logiService;

    @GetMapping
    public String prihodmarki(
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "prihodmarki"));

        List<Prihodmarki> prihods = prihodmarkiService.findAll();

        model.addAttribute("prihods", prihods);
        model.addAttribute("user", user);
        return "prihodmarki";
    }

    @GetMapping("/add")
    public String prihodmarkiadd(
            @RequestParam String price,
            @RequestParam String date,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "prihodmarkiadd param=" +
                " price = " + price +
                " date = " + date
        ));

        LocalDateTime date1 = DateUtils.parseIsoToDate(date);

        Prihodmarki prihodmarki = new Prihodmarki(Double.valueOf(price), date1);
        prihodmarkiService.save(prihodmarki);

        List<Prihodmarki> prihods = prihodmarkiService.findAll();
        model.addAttribute("prihods", prihods);

        model.addAttribute("user", user);
        return "fragment/prihodmarkifrag :: tablekonv";
    }

    @GetMapping("/del")
    public String prihodmarkidel(
            @RequestParam Long id,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "prihodmarkidel param=" +
                " id = " + id
        ));

        try {
            prihodmarkiService.delete(id);
        } catch (DataAccessResourceFailureException e) {
            model.addAttribute("user", user);
            model.addAttribute("errtext", "Отсутствует соединение с базой!");
            model.addAttribute("err", e);
            return "fragment/err :: error";
        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("errtext", "Непредвиденная ошибка!");
            model.addAttribute("err", e);
            return "fragment/err :: error";
        }
        model.addAttribute("user", user);
        return "redirect:/umik/main/prihodmarki";
    }

}
