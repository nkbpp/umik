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
import ru.pfr.model.umikbd.PEDdeloproizvodstvo;
import ru.pfr.model.umikbd.Spravkonv;
import ru.pfr.model.umikbd.User;
import ru.pfr.service.bdumik.LogiService;
import ru.pfr.service.bdumik.PEDdeloproizvodstvoService;
import ru.pfr.service.bdumik.SpravkonvService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Делопроизводство
 */
@Controller
@RequestMapping("/umik/main/deloproiz")
public class DeloproizvodstvoController {

    @Autowired
    private PEDdeloproizvodstvoService peDdeloproizvodstvoService;

    @Autowired
    private SpravkonvService spravkonvService;

    @Autowired
    private LogiService logiService;

    @GetMapping
    public String deloproiz(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        List<Spravkonv> spravkonvs = spravkonvService.findAll();
        model.addAttribute("spravkonvs", spravkonvs);

        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "deloproiz"));

        List<PEDdeloproizvodstvo> peDdeloproizvodstvos = peDdeloproizvodstvoService.findAllTekMounth();
        model.addAttribute("peDdeloproizvodstvos", peDdeloproizvodstvos);

        model.addAttribute("user", user);
        return "deloproiz";
    }

    @GetMapping("/ruki")
    public String deloproizfind_ruki(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        try {
            List<Spravkonv> spravkonvs = spravkonvService.findAll();
            model.addAttribute("spravkonvs", spravkonvs);
        } catch (DataAccessResourceFailureException e) {
            return "fragment/err :: error";
        } catch (Exception e) {
        }
        model.addAttribute("user", user);
        return "fragment/deloproizfrag :: rukivvod";
    }

    @GetMapping("/deloproiz/add_ruki")
    public String deloproizadd_ruki(
            @RequestParam Integer reg_number,
            @RequestParam String reg_date,
            @RequestParam String reg_pref,
            @RequestParam String reg_postf,
            @RequestParam Long viddost,
            @RequestParam String text_org,
            @RequestParam Long type,
            @RequestParam String sum,
            @RequestParam Integer kol_vo,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "deloproizadd param=" +
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

            PEDdeloproizvodstvo peDdeloproizvodstvo = new PEDdeloproizvodstvo(
                    null,
                    reg_pref,
                    reg_number.toString(),
                    reg_postf,
                    date,
                    null,
                    null,
                    text_org, spravkonv,
                    Double.valueOf(sum), kol_vo
            );
            peDdeloproizvodstvoService.save(peDdeloproizvodstvo);
        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("errtext", "Не смог добавить(, возможно вы не все ввели или ключевые поля в базе отсутствуют");
            model.addAttribute("err", e);
            return "fragment/err :: error";
        }

        List<PEDdeloproizvodstvo> peDobragrajs = peDdeloproizvodstvoService.findAllTekMounth();
        model.addAttribute("peDdeloproizvodstvos", peDobragrajs);
        model.addAttribute("user", user);
        return "fragment/deloproizfrag :: tabledelo";
    }

    @GetMapping("/del")
    public String deloproizdel(
            @RequestParam Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "deloproizdel param=" +
                " id = " + id
        ));

        try {
            peDdeloproizvodstvoService.delete(id);
        } catch (DataAccessResourceFailureException e) {
            model.addAttribute("user", user);
            model.addAttribute("errtext", "Отсутствует соединение с базой!");
            model.addAttribute("err", e);
            return "fragment/err :: error";
        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("err", e);
            return "fragment/err :: error";
        }
        model.addAttribute("user", user);
        return "redirect:/umik/main/deloproiz";
    }

}
