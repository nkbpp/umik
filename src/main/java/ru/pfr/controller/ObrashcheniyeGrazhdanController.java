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
import ru.pfr.model.umikbd.*;
import ru.pfr.service.bdumik.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Обращение граждан
 */
@Controller
@RequestMapping("/umik/main/obragraj")
public class ObrashcheniyeGrazhdanController {

    @Autowired
    private PEDobragrajService peDobragrajService;

    @Autowired
    private LogiService logiService;

    @Autowired
    private SpravkonvService spravkonvService;

    @Autowired
    private PEDdeloproizvodstvoService peDdeloproizvodstvoService;

    @Autowired
    private VidDostService vidDostService;

    @GetMapping
    public String obragraj(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        List<Spravkonv> spravkonvs = spravkonvService.findAll();
        model.addAttribute("spravkonvs", spravkonvs);
        List<VidDost> vidDosts = vidDostService.findAll();
        model.addAttribute("viddost_ruki", vidDosts);
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "obragraj"));

        List<PEDobragraj> peDobragrajs = peDobragrajService.findAllTekMounth();
        model.addAttribute("peDdeloproizvodstvos", peDobragrajs);

        model.addAttribute("user", user);
        return "obragraj";
    }

    @GetMapping("/add_ruki")
    public String obragrajadd_ruki(
            @RequestParam Integer reg_number,
            @RequestParam String reg_date,
            @RequestParam String reg_pref,
            @RequestParam String reg_postf,
            @RequestParam Long viddost,
            @RequestParam String text_org,
            @RequestParam Long type,
            @RequestParam String sum,
            @RequestParam String text_fio,
            @RequestParam String addr_list,
            @RequestParam Integer kol_vo,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "obragraj param=" +
                " reg_pref = " + reg_pref +
                " reg_number = " + reg_number +
                " reg_postf = " + reg_postf +
                " reg_date = " + reg_date +
                " viddost = " + viddost +
                " text_org = " + text_org +
                " type = " + type +
                " sum = " + sum +
                " kol_vo = " + kol_vo
        ));

        try {
            Spravkonv spravkonv = spravkonvService.findById(type);

            LocalDateTime date = DateUtils.parseToDate(reg_date);

            PEDobragraj peDobragraj = new PEDobragraj(
                    null,
                    reg_pref,
                    reg_number.toString(),
                    reg_postf,
                    date,
                    null,//vidDost.getId(),/*peDdeloproizvodstvoService.findNameId(name),*/
                    null,//vidDost.getName(),
                    text_org,
                    text_fio,
                    addr_list,
                    spravkonv,
                    Double.valueOf(sum), kol_vo
            );
            peDobragrajService.save(peDobragraj);
        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("errtext", "Не смог добавить(, возможно вы не все ввели или ключевые поля в базе отсутствуют");
            model.addAttribute("err", e);
            return "fragment/err :: error";
        }

        List<PEDdeloproizvodstvo> peDobragrajs = peDdeloproizvodstvoService.findAllTekMounth();
        model.addAttribute("peDdeloproizvodstvos", peDobragrajs);
        model.addAttribute("user", user);
        return "fragment/oblagrajfrag :: tabledelo";
    }

    @GetMapping("/del")
    public String obragrajdel(
            @RequestParam Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "obragrajdel param=" +
                " id = " + id
        ));

        try {
            peDobragrajService.delete(id);
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
        return "redirect:/umik/main/obragraj";
    }

}
