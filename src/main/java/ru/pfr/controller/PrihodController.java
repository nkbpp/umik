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
import ru.pfr.model.umikbd.Logi;
import ru.pfr.model.umikbd.Prihod;
import ru.pfr.model.umikbd.Spravkonv;
import ru.pfr.model.umikbd.User;
import ru.pfr.service.bdumik.LogiService;
import ru.pfr.service.bdumik.PrihodService;
import ru.pfr.service.bdumik.SpravkonvService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/umik/main/prihod")
public class PrihodController {

    @Autowired
    private PrihodService prihodService;

    @Autowired
    private LogiService logiService;

    @Autowired
    private SpravkonvService spravkonvService;

    @GetMapping
    public String prihod(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "prihod"));

        List<Prihod> prihods = prihodService.findAll();
        List<Spravkonv> spravkonvs = spravkonvService.findAll();
        model.addAttribute("spravkonvs", spravkonvs);
        model.addAttribute("prihods", prihods);
        model.addAttribute("user", user);
        return "prihod";
    }

    @GetMapping("/add")
    public String prihodadd(
            @RequestParam String prefix,
            @RequestParam String index,
            @RequestParam String price,
            @RequestParam String kol_vo,
            @RequestParam Long id_konv,
            @RequestParam String date,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "prihodadd param=" +
                " prefix = " + prefix +
                " index = " + index +
                " price " + price +
                " kol_vo = " + kol_vo +
                " id_konv = " + id_konv +
                " date = " + date
        ));

        Spravkonv spravkonv = spravkonvService.findById(id_konv);

        LocalDateTime date1;
        try {
            date1 = DateUtils.parseIsoToDate(date);
        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("errtext", "Не удалось конвертировать формат!");
            model.addAttribute("err", e);
            return "fragment/err :: error";
        }
        Prihod prihod = new Prihod(prefix, index, Integer.valueOf(kol_vo), Double.valueOf(price), spravkonv, date1);
        prihodService.save(prihod);

        List<Prihod> prihods = prihodService.findAll();
        model.addAttribute("prihods", prihods);

        List<Spravkonv> spravkonvs = spravkonvService.findAll();
        model.addAttribute("spravkonvs", spravkonvs);
        model.addAttribute("user", user);
        return "fragment/prihodfrag :: tablekonv";
    }

    @GetMapping("/del")
    public String prihoddel(
            @RequestParam Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "prihoddel param=" +
                " id = " + id
        ));

        try {
            prihodService.delete(id);
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
        return "redirect:/umik/main/prihod";
    }

}
