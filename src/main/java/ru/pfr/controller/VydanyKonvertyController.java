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
import ru.pfr.model.umikbd.User;
import ru.pfr.model.umikbd.Vidanykonv;
import ru.pfr.service.bdumik.LogiService;
import ru.pfr.service.bdumik.VidanykonvService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/umik/main/vidanykonv")
public class VydanyKonvertyController {

    @Autowired
    private VidanykonvService vidanykonvService;

    @Autowired
    private LogiService logiService;

    @GetMapping
    public String vidanykonv(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "vidanykonv"));

        List<Vidanykonv> vidanykonvs = vidanykonvService.findAll();

        model.addAttribute("vidanykonvs", vidanykonvs);
        model.addAttribute("user", user);
        return "vidanykonv";
    }

    @GetMapping("/add")
    public String vidanykonvadd(
            @RequestParam String vidanykonv,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "vidanykonvadd param=" +
                " vidanykonv = " + vidanykonv
        ));

        try {
            Vidanykonv vidanykonv1 = new Vidanykonv(vidanykonv);
            vidanykonvService.save(vidanykonv1);

            List<Vidanykonv> vidanykonvs = vidanykonvService.findAll();
            model.addAttribute("vidanykonvs", vidanykonvs);
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
        return "fragment/vidanykonvfrag :: tablevk";
    }

    @GetMapping("/del")
    public String vidanykonvdel(
            @RequestParam Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "vidanykonvdel param=" +
                " id = " + id
        ));

        try {
            vidanykonvService.delete(id);
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
        return "redirect:/umik/main/vidanykonv";
    }

}
