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
import ru.pfr.model.umikbd.*;
import ru.pfr.service.bdumik.InoeService;
import ru.pfr.service.bdumik.LogiService;
import ru.pfr.service.bdumik.SpravkonvService;
import ru.pfr.service.bdumik.VidanykonvService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Иное
 */
@Controller
@RequestMapping("/umik/main/inoe")
public class InoeController {

    @Autowired
    private InoeService inoeService;

    @Autowired
    private LogiService logiService;

    @Autowired
    private SpravkonvService spravkonvService;

    @Autowired
    private VidanykonvService vidanykonvService;

    @GetMapping
    public String inoe(
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "inoe"));

        List<Vidanykonv> vidanykonvs = vidanykonvService.findAll();
        model.addAttribute("vidanykonvs", vidanykonvs);

        List<Spravkonv> spravkonvs = spravkonvService.findAll();
        model.addAttribute("spravkonvs", spravkonvs);

        List<Inoe> inoes = inoeService.getAllRecordsForCurrentMonth();
        model.addAttribute("inoes", inoes);

        model.addAttribute("user", user);
        return "inoe";
    }

    @GetMapping("/add")
    public String inoeadd(
            @RequestParam String reg_date,
            @RequestParam Long typevk,
            @RequestParam Long type,
            @RequestParam Integer kol_vo,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "inoeadd param=" +
                " reg_date = " + reg_date +
                " typevk = " + typevk +
                " type = " + type +
                " kol_vo = " + kol_vo
        ));

        try {

            Spravkonv spravkonv = spravkonvService.findById(type);

            Vidanykonv vidanykonv = vidanykonvService.findById(typevk);

            Inoe inoe = new Inoe(
                    DateUtils.parseIsoToDate(reg_date),
                    vidanykonv,
                    spravkonv,
                    kol_vo
            );
            inoeService.save(inoe);

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
        return "redirect:/umik/main/inoe";
    }

    @GetMapping("/del")
    public String inoedel(
            @RequestParam Long id,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "inoedel param=" +
                " id = " + id
        ));

        try {
            inoeService.delete(id);
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
        return "redirect:/umik/main/inoe";
    }

}
