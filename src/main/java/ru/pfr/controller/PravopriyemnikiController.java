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
import ru.pfr.service.bdumik.LogiService;
import ru.pfr.service.bdumik.OtchmarkandkonvService;
import ru.pfr.service.bdumik.PravopriemService;
import ru.pfr.service.bdumik.PrihodService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Правоприемники
 */
@Controller
@RequestMapping("/umik/main/pravopriem")
public class PravopriyemnikiController {

    @Autowired
    private PravopriemService pravopriemService;

    @Autowired
    private LogiService logiService;

    @Autowired
    private PrihodService prihodService;

    @Autowired
    private OtchmarkandkonvService otchmarkandkonvService;

    @GetMapping
    public String pravopriem(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "pravopriem"));

        List<Pravopriem> pravopriems = pravopriemService.findAllTekMounth();
        model.addAttribute("pravopriems", pravopriems);

        // Получаем текущую дату и время
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Получаем дату и время прошлого месяца
        LocalDateTime lastMonth = currentDateTime.minusMonths(1);
        // Получаем первый день прошлого месяца
        LocalDateTime firstDayOfLastMonth = lastMonth.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        // Получаем последний день прошлого месяца
        LocalDateTime lastDayOfLastMonth = lastMonth.withDayOfMonth(lastMonth.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        List<Otchmarkandkonv> otchmarkandkonvD = otchmarkandkonvService.findAllDatOnlyTypeD(firstDayOfLastMonth, lastDayOfLastMonth);
        List<Prihod> prihodsD = new ArrayList<>();
        otchmarkandkonvD.forEach(otchmarkandkonv -> prihodsD.add(otchmarkandkonv.getPrihod()));
        if (otchmarkandkonvD.isEmpty()) {
            prihodsD.add(prihodService.findTypeDLast());
        }

        model.addAttribute("prihodsD", prihodsD);

        model.addAttribute("user", user);

        model.addAttribute("max", pravopriemService.getMaxCena_Sell());

        return "pravopriem";
    }

    @GetMapping("/add")
    public String pravopriemadd(
            @RequestParam String date,
            @RequestParam Long kol_d,
            @RequestParam String id_prihod,
            @RequestParam String cena_sell,
            @RequestParam String sum_mark,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "pravopriemadd param=" +
                " date = " + date +
                " kol_d = " + kol_d +
                " id_prihod = " + id_prihod +
                " cena_sell = " + cena_sell +
                " sum_mark = " + sum_mark
        ));

        try {
            Prihod p = prihodService.findById(Long.valueOf(id_prihod));

            Pravopriem pravopriem = new Pravopriem(
                    DateUtils.parseIsoToDate(date),
                    kol_d,
                    p,
                    Double.valueOf(cena_sell),
                    Double.valueOf(sum_mark)
            );
            pravopriemService.save(pravopriem);

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
        return "redirect:/umik/main/pravopriem";
    }

    @GetMapping("/del")
    public String pravopriemdel(
            @RequestParam Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "pravopriemdel param=" +
                " id = " + id
        ));

        try {
            pravopriemService.delete(id);
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
        return "redirect:/umik/main/pravopriem";
    }

}
