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
import ru.pfr.global.DateUtils;
import ru.pfr.model.umikbd.Bolgaria;
import ru.pfr.model.umikbd.Logi;
import ru.pfr.model.umikbd.Spravkonv;
import ru.pfr.model.umikbd.User;
import ru.pfr.service.bdumik.BolgariaService;
import ru.pfr.service.bdumik.LogiService;
import ru.pfr.service.bdumik.SpravkonvService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/umik/main/bolgaria")
public class BolgariaController {

    @Autowired
    private LogiService logiService;

    @Autowired
    private BolgariaService bolgariaService;

    @Autowired
    private SpravkonvService spravkonvService;

    @GetMapping
    public String bolgaria(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "bolgaria"));

        List<Spravkonv> spravkonvs = spravkonvService.findAll();
        model.addAttribute("spravkonvs", spravkonvs);

        List<Bolgaria> bolgarias = bolgariaService.getAllRecordsForCurrentMonth();
        model.addAttribute("bolgarias", bolgarias);

        model.addAttribute("user", user);
        return "bolgaria";
    }

    @GetMapping("/add")
    public String bolgariaadd(
            @RequestParam String reg_number,
            @RequestParam String reg_date,
            @RequestParam String text_org,
            @RequestParam Long id_name,
            @RequestParam String name,
            @RequestParam Long type,
            @RequestParam String sum,
            @RequestParam Integer kol_vo,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "bolgariaadd param=" +
                " reg_number = " + reg_number +
                " reg_date = " + reg_date +
                " text_org = " + text_org +
                " id_name = " + id_name +
                " name = " + name +
                " type = " + type +
                " sum = " + sum +
                " kol_vo = " + kol_vo
        ));

        try {
            Spravkonv spravkonv = spravkonvService.findById(type);

            Bolgaria bolgaria = new Bolgaria(
                    reg_number,
                    DateUtils.parseIsoToDate(reg_date),
                    id_name,
                    name,
                    text_org,
                    spravkonv,
                    Double.valueOf(sum),
                    kol_vo
            );
            System.out.println();
            bolgariaService.save(bolgaria);

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
        return "redirect:/umik/main/bolgaria";
    }

    @GetMapping("/del")
    public String bolgariadel(
            @RequestParam Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "bolgariadel param=" +
                " id = " + id
        ));

        try {
            bolgariaService.delete(id);
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
        return "redirect:/umik/main/bolgaria";
    }
}
