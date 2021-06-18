package ru.pfr.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pfr.model.umikbd.Adminparam;
import ru.pfr.model.umikbd.Logi;
import ru.pfr.model.umikbd.User;
import ru.pfr.service.bdumik.AdminparamService;
import ru.pfr.service.bdumik.LogiService;
import ru.pfr.service.bdumik.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/umik")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    LogiService logiService;

    @Autowired
    AdminparamService adminparamService;

    @GetMapping("/admin")
    public String adminstart(
            @AuthenticationPrincipal User user,
            Model model) {


        model.addAttribute("user", user);

        Adminparam adminparam = adminparamService.findByAdminparam();
        model.addAttribute("adminparam", adminparam);

        List<User> logerrs = userService.findAll();////////////////////
        model.addAttribute("logerrs", logerrs);

        logiService.save(new Logi(new Date(),user.getLogin(),"Страница администратора"));
        return "admin";
    }

    @GetMapping("/adminupdate")
    public String adminupdate(
            @RequestParam Long kolpopitok,
            @RequestParam Long koefpopitok,
            @RequestParam Long block,
            @AuthenticationPrincipal User user,
            Model model) {

        model.addAttribute("user", user);

        Adminparam adminparam = new Adminparam(kolpopitok, koefpopitok, block);
        adminparamService.save(adminparam);

        model.addAttribute("adminparam", adminparam);
        logiService.save(new Logi(new Date(),user.getLogin(),"Изменение параметров в adminupdate"));
        return "admin";
    }

    @GetMapping("/juraudit")
    public String juraudit(@AuthenticationPrincipal User user,
                           Model model) {

        model.addAttribute("user", user);

        Iterable<Logi> logi = logiService.findAll();
        model.addAttribute("logi", logi);
        logiService.save(new Logi(new Date(),user.getLogin(),"Вход в журнал"));
        return "juraudit";
    }

    @GetMapping("/juraudit/tables")
    public String table(@RequestParam String d1,
                        @RequestParam String d2,
                        @RequestParam String login,
                        @RequestParam String type,
                        @RequestParam String text,
                        @AuthenticationPrincipal User user,
                        Model model) {

        Date date1=null;
        Date date2=null;

        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(d1);
            date2 = new SimpleDateFormat("yyyy-MM-dd").parse(d2);
        } catch (Exception e) {
            date1=null;
            date2=null;
        }
        model.addAttribute("user", user);

        Long type2=null;
        try {
            type2=Long.valueOf(type);
        } catch (Exception e)
        {}
        if(login.equals(""))login=null;
        if(text.equals(""))text=null;
        Iterable<Logi> logi;
        if(date1==null || date2==null){
            if(login==null && type2==null && text==null){
                logi = logiService.findAll();
            }else{
                logi = logiService.findByDateBetween(
                        login,
                        type2,
                        text
                );}
        }else{
            logi = logiService.findByDateBetween(date1, date2, login, type2, text);
        }

        //Iterable<Logi> logi = logiService.findByDateBetween(date1, date2, login, Long.valueOf(type));
        model.addAttribute("logi", logi);
        logiService.save(new Logi(new Date(),user.getLogin(),"Фильтр журнала"));
        return "fragmentadmin/adminfragment :: tables";
    }

    @GetMapping("/juraudit/clear")
    public String clear(@AuthenticationPrincipal User user,
                        Model model) {

        logiService.clear();
        logiService.save(new Logi(new Date(),user.getLogin(),"Очистка журнала"));
        Iterable<Logi> logi = logiService.findAll();
        model.addAttribute("logi", logi);

        return "fragmentadmin/adminfragment :: tables";
    }



    @GetMapping("/vihod/logout")
    public void logout (@AuthenticationPrincipal User user,
                        Model model) {
        logiService.save(new Logi(new Date(),user.getLogin(),"Выход"));

    }

    @GetMapping("/admin/clear")
    public String admintwoclear(@RequestParam Long id,
                                @AuthenticationPrincipal User user,
                                Model model) {

        User logerr = userService.findById(id);
        logerr.setActive(0l);
        logerr.setDate(new Date());
        userService.save(logerr);

        List<User> logerrs = userService.findAll();
        model.addAttribute("logerrs", logerrs);

        logiService.save(new Logi(new Date(),user.getLogin(),"Снять блокировку пользователя "+logerr.getLogin()));

        return "fragmentadmin/adminfragment :: blocks";
    }


    @GetMapping("/admin/zablock")
    public String admintwozablock(@RequestParam Long id,
                                  @AuthenticationPrincipal User user,
                                  Model model) {

        User logerr = userService.findById(id);
        logerr.setActive(1000l);
        logerr.setDate(new Date());
        userService.save(logerr);

        List<User> logerrs = userService.findAll();
        model.addAttribute("logerrs", logerrs);

        logiService.save(new Logi(new Date(),user.getLogin(),"Заблокировать пользователя "+logerr.getLogin()));

        return "fragmentadmin/adminfragment :: blocks";
    }

}
