package ru.pfr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pfr.configuration.UserPrincipal;
import ru.pfr.model.umikbd.Logi;
import ru.pfr.model.umikbd.Spravkonv;
import ru.pfr.model.umikbd.User;
import ru.pfr.service.bdumik.LogiService;
import ru.pfr.service.bdumik.SpravkonvService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/umik/main/spravkonv")
public class SpravochnikKonvertovController {

    @Autowired
    private LogiService logiService;

    @Autowired
    private SpravkonvService spravkonvService;

    @GetMapping
    public String spravkonv(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "spravkonv"));

        List<Spravkonv> spravkonvs = spravkonvService.findAll();

        model.addAttribute("spravkonvs", spravkonvs);
        model.addAttribute("user", user);
        return "spravkonv";
    }

    @GetMapping("/add")
    public String spravkonvadd(
            @RequestParam String typekonv,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "spravkonvadd param=" +
                " typekonv = " + typekonv
        ));

        try {
            Spravkonv spravkonv = new Spravkonv(typekonv);
            spravkonvService.save(spravkonv);

            List<Spravkonv> spravkonvs = spravkonvService.findAll();
            model.addAttribute("spravkonvs", spravkonvs);
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
        return "fragment/spravkonvfrag :: tablekonv";
    }

    @GetMapping("/del")
    public String spravkonvdel(
            @RequestParam Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "spravkonvdel param=" +
                " id = " + id
        ));

        try {
            spravkonvService.delete(id);
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
        return "redirect:/umik/main/spravkonv";
    }

}
