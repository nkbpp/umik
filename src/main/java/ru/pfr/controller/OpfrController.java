package ru.pfr.controller;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.pfr.global.MyNumbers;
import ru.pfr.model.rsdoc_pfr.Deloproizvodstvo;
import ru.pfr.model.rsdoc_pfr.Oblagraj;
import ru.pfr.model.rsdoc_pfr.Sendtype;
import ru.pfr.model.umikbd.*;
import ru.pfr.service.bdumik.*;
/*import ru.pfr.service.rsdoc_pfr.DeloproizvodstvoService;
import ru.pfr.service.rsdoc_pfr.OblagrajService;
import ru.pfr.service.rsdoc_pfr.SendtypeService;*/

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/umik/main")
public class OpfrController {

/*    @Autowired
    DeloproizvodstvoService deloproizvodstvoService;
    @Autowired
    SendtypeService sendtypeService;
    @Autowired
    OblagrajService oblagrajService;*/

    @Autowired
    PEDdeloproizvodstvoService peDdeloproizvodstvoService;

    @Autowired
    PEDobragrajService peDobragrajService;

    @Autowired
    SpravkonvService spravkonvService;

    @Autowired
    BolgariaService bolgariaService;

    @Autowired
    Reestr1VievService reestr1VievService;

    @Autowired
    PravopriemService pravopriemService;

    @Autowired
    ShablonService shablonService;

    @Autowired
    VidanykonvService vidanykonvService;

    @Autowired
    InoeService inoeService;

    @Autowired
    PrihodService prihodService;

    @Autowired
    OtchraschodkonvService otchraschodkonvService;

    @Autowired
    OtchmarkandkonvService otchmarkandkonvService;

    @Autowired
    PrihodmarkiService prihodmarkiService;

    @Autowired
    OtchmarkService otchmarkService;

    @Autowired
    LogiService logiService;

    @Autowired
    VidDostService vidDostService;

    @GetMapping()
    public String startupfr(
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "Главная страница startupfr"));

        //List<PEDdeloproizvodstvo> peDdeloproizvodstvos = peDdeloproizvodstvoService.;

        model.addAttribute("user", user);
        return "main";
    }

    @GetMapping("/deloproiz")
    public String deloproiz(
            @AuthenticationPrincipal User user,
            Model model) {
        List<VidDost> vidDosts = vidDostService.findAll();
        model.addAttribute("viddost_ruki", vidDosts);
        List<Spravkonv> spravkonvs = spravkonvService.findAll();
        model.addAttribute("spravkonvs", spravkonvs);

        logiService.save(new Logi(new Date(), user.getLogin(), "deloproiz"));

        List<PEDdeloproizvodstvo> peDdeloproizvodstvos = peDdeloproizvodstvoService.findAllTekMounth();
        model.addAttribute("peDdeloproizvodstvos", peDdeloproizvodstvos);

        model.addAttribute("user", user);
        return "deloproiz";
    }

    @GetMapping("/deloproiz/ruki")
    public String deloproizfind_ruki(
            @AuthenticationPrincipal User user,
            Model model) {

        try {
            List<VidDost> vidDosts = vidDostService.findAll();
            model.addAttribute("viddost_ruki", vidDosts);
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
            //@RequestParam String name,
            @RequestParam Long viddost,
            @RequestParam String text_org,
            @RequestParam Long type,
            @RequestParam String sum,
            @RequestParam Integer kol_vo,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "deloproizadd param=" +
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

            VidDost vidDost = vidDostService.findById(viddost);

            Date date = dateddMMyyyy(reg_date);

            LocalDate first = new java.sql.Date(date.getTime()).toLocalDate();
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


            PEDdeloproizvodstvo peDdeloproizvodstvo = new PEDdeloproizvodstvo(
                    null, reg_pref, reg_number.toString(),
                    reg_postf, date, vidDost.getId(),/*peDdeloproizvodstvoService.findNameId(name),*/
                    vidDost.getName(), text_org, spravkonv, Double.valueOf(sum), kol_vo
            );
            System.out.println("");
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

    @GetMapping("/deloproiz/del")
    public String deloproizdel(
            @RequestParam Long id,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "deloproizdel param=" +
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

    @GetMapping("/obragraj")
    public String obragraj(
            @AuthenticationPrincipal User user,
            Model model) {
        List<VidDost> vidDosts = vidDostService.findAll();
        model.addAttribute("viddost_ruki", vidDosts);
        List<Spravkonv> spravkonvs = spravkonvService.findAll();
        model.addAttribute("spravkonvs", spravkonvs);
        logiService.save(new Logi(new Date(), user.getLogin(), "obragraj"));

        List<PEDobragraj> peDobragrajs = peDobragrajService.findAllTekMounth();
        model.addAttribute("peDdeloproizvodstvos", peDobragrajs);

        model.addAttribute("user", user);
        return "obragraj";
    }

    @GetMapping("/obragraj/add_ruki")
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
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "obragraj param=" +
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

            VidDost vidDost = vidDostService.findById(viddost);

            Date date = dateddMMyyyy(reg_date);

            PEDobragraj peDobragraj = new PEDobragraj(
                    null, reg_pref, reg_number.toString(),
                    reg_postf, date, vidDost.getId(),/*peDdeloproizvodstvoService.findNameId(name),*/
                    vidDost.getName(), text_org,
                    text_fio, addr_list,
                    spravkonv, Double.valueOf(sum), kol_vo
            );
            System.out.println("");
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

    @GetMapping("/obragraj/del")
    public String obragrajdel(
            @RequestParam Long id,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "obragrajdel param=" +
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

    @GetMapping("/inoe")
    public String inoe(
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "inoe"));

        List<Vidanykonv> vidanykonvs = vidanykonvService.findAll();
        model.addAttribute("vidanykonvs", vidanykonvs);

        List<Spravkonv> spravkonvs = spravkonvService.findAll();
        model.addAttribute("spravkonvs", spravkonvs);

        List<Inoe> inoes = inoeService.findAllTekMounth();
        model.addAttribute("inoes", inoes);

        model.addAttribute("user", user);
        return "inoe";
    }

    @GetMapping("/inoe/add")
    public String inoeadd(
            @RequestParam String reg_date,
            @RequestParam Long typevk,
            @RequestParam Long type,
            @RequestParam Integer kol_vo,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "inoeadd param=" +
                " reg_date = " + reg_date +
                " typevk = " + typevk +
                " type = " + type +
                " kol_vo = " + kol_vo
        ));

        try {

            Spravkonv spravkonv = spravkonvService.findById(type);

            Vidanykonv vidanykonv = vidanykonvService.findById(typevk);

            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(reg_date);

            Inoe inoe = new Inoe(
                    date, vidanykonv, spravkonv, kol_vo
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

    @GetMapping("/inoe/del")
    public String inoedel(
            @RequestParam Long id,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "inoedel param=" +
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

    @GetMapping("/pravopriem")
    public String pravopriem(
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "pravopriem"));

        List<Pravopriem> pravopriems = pravopriemService.findAllTekMounth();
        model.addAttribute("pravopriems", pravopriems);
//

        YearMonth month = YearMonth.now();
        String firstDay = month.atDay(1).toString(),
        endDay = month.atEndOfMonth().toString();

        Date date1 = new Date();
        Date date2 = new Date();
        Date date1minusMonths = new Date();
        Date date2minusMonths = new Date();
        try {
            //date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date1);
            //date2 = new SimpleDateFormat("yyyy-MM-dd").parse(date1);

            LocalDateTime first = date1.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime().minusMonths(1);
            LocalDateTime end = first.plusMonths(1);

            date1minusMonths = new SimpleDateFormat("yyyy-MM-dd").parse(first.toString());
            date2minusMonths = new SimpleDateFormat("yyyy-MM-dd").parse(end.toString());

            Long datenow = date2minusMonths.getTime() - 60000l; //23часа 59минут
            date2minusMonths = new Date(datenow);
        } catch (Exception e) {
        }

        //todo поработать с датами?
        Long datenow = date2.getTime() + 86340000l; //23часа 59минут
        date2 = new Date(datenow);

        List<Otchmarkandkonv> otchmarkandkonvD = otchmarkandkonvService.findAllDatOnlyTypeD(date1minusMonths, date2minusMonths);
        List<Prihod> prihodsD = new ArrayList<>();
        otchmarkandkonvD.forEach(otchmarkandkonv -> {
            prihodsD.add(otchmarkandkonv.getPrihod());
        });
        if(otchmarkandkonvD.size()==0){
            prihodsD.add(prihodService.findTypeDLast());
        }



        model.addAttribute("prihodsD", prihodsD);

        model.addAttribute("user", user);

        model.addAttribute("max", pravopriemService.getMaxCena_Sell());

        return "pravopriem";
    }

    @GetMapping("/pravopriem/add")
    public String pravopriemadd(
            @RequestParam String date,
            @RequestParam Long kol_d,
            @RequestParam String id_prihod,
            @RequestParam String cena_sell,
            @RequestParam String sum_mark,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "pravopriemadd param=" +
                " date = " + date +
                " kol_d = " + kol_d +
                " id_prihod = " + id_prihod +
                " cena_sell = " + cena_sell +
                " sum_mark = " + sum_mark
        ));

        try {

            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);

            Prihod p = prihodService.findById(Long.valueOf(id_prihod));

            Pravopriem pravopriem = new Pravopriem(
                    date1, kol_d, p, Double.valueOf(cena_sell), Double.valueOf(sum_mark)
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

    @GetMapping("/pravopriem/del")
    public String pravopriemdel(
            @RequestParam Long id,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "pravopriemdel param=" +
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


    @GetMapping("/bolgaria")
    public String bolgaria(
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "bolgaria"));

        //List<Sendtype> sendtypes = sendtypeService.findAllS();
        //model.addAttribute("sendtypes", sendtypes);

        List<VidDost> vidDosts = vidDostService.findAll();
        model.addAttribute("viddost_ruki", vidDosts);

        List<Spravkonv> spravkonvs = spravkonvService.findAll();
        model.addAttribute("spravkonvs", spravkonvs);

        List<Bolgaria> bolgarias = bolgariaService.findAllTekMounth();
        model.addAttribute("bolgarias", bolgarias);

        model.addAttribute("user", user);
        return "bolgaria";
    }

    @GetMapping("/bolgaria/add")
    public String bolgariaadd(
            @RequestParam String reg_number,
            @RequestParam String reg_date,
            @RequestParam String text_org,
            @RequestParam Long id_name,
            @RequestParam String name,
            @RequestParam Long type,
            @RequestParam String sum,
            @RequestParam Integer kol_vo,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "bolgariaadd param=" +
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

            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(reg_date);

            Bolgaria bolgaria = new Bolgaria(
                    reg_number, date,
                    id_name, name, text_org, spravkonv, Double.valueOf(sum), kol_vo
            );
            System.out.println("");
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

    @GetMapping("/bolgaria/del")
    public String bolgariadel(
            @RequestParam Long id,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "bolgariadel param=" +
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

    @GetMapping("/reestr2")
    public String reestr2(
            @RequestParam(value = "dat1", defaultValue = "") String dat1,
            @RequestParam(value = "dat2", defaultValue = "") String dat2,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "reestr2 param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        Date date1 = new Date();
        Date date2 = new Date();
        if (!(dat1.equals("") && dat2.equals(""))) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date1 = dateyyyyMMdd(dat1);
            date2 = dateyyyyMMdd(dat2);
            Long datenow = date2.getTime() + 86340000l; //23часа 59минут
            date2 = new Date(datenow);
        } else {
            LocalDateTime first = LocalDateTime.now().withDayOfMonth(1);
            LocalDateTime last = first.plusMonths(1);
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            try {
                date1 = new SimpleDateFormat("dd.MM.yyyy").parse(first.format(formatter));
                date2 = new SimpleDateFormat("dd.MM.yyyy").parse(last.format(formatter));
                Long datenow = date2.getTime() - 60000l; //23часа 59минут
                date2 = new Date(datenow);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<Pravopriem> pravopriems = pravopriemService.findAllD(date1, date2);

        double so = 0, m = 0, k = 0, mzp = 0;

        for (int i = 0; i < pravopriems.size(); i++) {
            Pravopriem r = pravopriems.get(i);
            mzp += r.getMarki_k_zak_pis();
            k += r.getKonvert_d();
            m += r.get_sumk();
            so += r.get_sumob();
        }

        model.addAttribute("pravopriems", pravopriems);
        model.addAttribute("so", so);
        model.addAttribute("m", m);
        model.addAttribute("k", k);
        model.addAttribute("mzp", mzp);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("date1", dateFormat1.format(date1));
        model.addAttribute("date2", dateFormat1.format(date2));
        model.addAttribute("user", user);
        return "reestr2";
    }

    @GetMapping("/reestr2/pechatdocx")
    public @ResponseBody
    byte[] reestr2pechatdocx(
            @RequestParam(value = "dat1", defaultValue = "") String dat1,
            @RequestParam(value = "dat2", defaultValue = "") String dat2,
            @AuthenticationPrincipal User user,
            HttpServletResponse resp,
            Model model) throws IOException {

        logiService.save(new Logi(new Date(), user.getLogin(), "reestr2pechatdocx param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        Date date1 = new Date();
        Date date2 = new Date();
        if (!(dat1.equals("") && dat2.equals(""))) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date1 = dateyyyyMMdd(dat1);
            date2 = dateyyyyMMdd(dat2);
            Long datenow = date2.getTime() + 86340000l; //23часа 59минут
            date2 = new Date(datenow);
        } else {
            LocalDateTime first = LocalDateTime.now().withDayOfMonth(1);
            LocalDateTime last = first.plusMonths(1);
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            try {
                date1 = new SimpleDateFormat("dd.MM.yyyy").parse(first.format(formatter));
                date2 = new SimpleDateFormat("dd.MM.yyyy").parse(last.format(formatter));
                Long datenow = date2.getTime() - 60000l; //23часа 59минут
                date2 = new Date(datenow);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        List<Pravopriem> pravopriems = pravopriemService.findAllD(date1, date2);

        double so = 0, m = 0, k = 0, mzp = 0;

        for (int i = 0; i < pravopriems.size(); i++) {
            Pravopriem r = pravopriems.get(i);
            mzp += r.getMarki_k_zak_pis();
            k += r.getKonvert_d();
            m += r.get_sumk();
            so += r.get_sumob();
        }

        InputStream in = null;
        try {
            Shablon shablon = shablonService.findById(2l);

            InputStream inputStream = new ByteArrayInputStream(shablon.getDokument());

            XWPFDocument docxFile = null;
            docxFile = new XWPFDocument(inputStream);
            // открываем файл и считываем его содержимое в объект XWPFDocument

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            for (XWPFParagraph p : docxFile.getParagraphs()) {
                List<XWPFRun> runs = p.getRuns();
                if (runs != null) {
                    for (XWPFRun r : runs) {
                        String text = r.getText(0);
                        if (text != null && text.contains("$")) {
                            text = text.replace("$1", dateFormat.format(date1));
                            text = text.replace("$2", dateFormat.format(date2));
                            r.setText(text, 0);
                        }
                    }
                }
            }

            //находим таблицу
            XWPFTable T = null;
            for (XWPFTable tbl : docxFile.getTables()) {
                T = tbl;
                break;
            }

            //пришлось создать новый документ для параграфов
            XWPFDocument document = new XWPFDocument();

            int fontSize = 11;
            XWPFParagraph paragraph;
            XWPFRun run;

            int j = 2;
            for (Pravopriem pravopriem : pravopriems) {


                XWPFTableRow tableRowTwo = T.createRow();

                for (int i = 0; i < 6; i++) {
                    paragraph = document.createParagraph();
                    paragraph.setAlignment(ParagraphAlignment.CENTER);
                    run = paragraph.createRun();
                    run.setFontSize(fontSize);
                    run.setBold(false);
                    String st;
                    switch (i) {
                        case 0:
                            st = String.valueOf(i + 1);
                            break;
                        case 1:
                            st = pravopriem.get_datestr();
                            break;
                        case 2:
                            st = pravopriem.get_sumob().toString();
                            break;
                        case 3:
                            st = pravopriem.get_sumk().toString();
                            break;
                        case 4:
                            st = pravopriem.getKonvert_d().toString();
                            break;
                        default:
                            st = pravopriem.getMarki_k_zak_pis().toString();
                            break;
                    }
                    run.setText(st);

                    if (i < tableRowTwo.getTableICells().size()) {
                        tableRowTwo.getCell(i).setParagraph(paragraph);
                    } else tableRowTwo.createCell().setParagraph(paragraph);
                }
            }

            XWPFTableRow tableRowTwo = T.createRow();

            for (int i = 0; i < 1; i++) {
                paragraph = document.createParagraph();
                run = paragraph.createRun();
                run.setText("");
                tableRowTwo.getCell(i).setParagraph(paragraph);
            }

            for (int i = 1; i < 6; i++) {
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                run = paragraph.createRun();
                run.setFontSize(fontSize);
                run.setBold(false);
                String st;
                switch (i) {
                    case 1:
                        st = "ИТОГО";
                        break;
                    case 2:
                        st = String.valueOf(so);
                        break;
                    case 3:
                        st = String.valueOf(m);
                        break;
                    case 4:
                        st = String.valueOf(k);
                        break;
                    default:
                        st = String.valueOf(mzp);
                        break;
                }
                run.setText(st);
                if (i < tableRowTwo.getTableICells().size()) {
                    tableRowTwo.getCell(i).setParagraph(paragraph);
                } else tableRowTwo.createCell().setParagraph(paragraph);
            }

            ByteArrayOutputStream b = new ByteArrayOutputStream();
            docxFile.write(b);

            resp.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", "reestr2.docx");
            resp.setHeader(headerKey, headerValue);
            resp.setContentLength(b.size());
            resp.getOutputStream().write(b.toByteArray());

            in = new ByteArrayInputStream(b.toByteArray());
        } catch (Exception e) {
        }

        return IOUtils.toByteArray(in);
    }

    @GetMapping("/reestr1")
    public String reestr1(
            @RequestParam(value = "dat1", defaultValue = "") String dat1,
            @RequestParam(value = "dat2", defaultValue = "") String dat2,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "reestr1 param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        Date date1 = new Date();
        Date date2 = new Date();
        if (!(dat1.equals("") && dat2.equals(""))) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date1 = dateyyyyMMdd(dat1);
            date2 = dateyyyyMMdd(dat2);
            Long datenow = date2.getTime() + 86340000l; //23часа 59минут
            date2 = new Date(datenow);
        } else {
            LocalDateTime first = LocalDateTime.now().withDayOfMonth(1);
            LocalDateTime last = first.plusMonths(1);
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            try {
                date1 = new SimpleDateFormat("dd.MM.yyyy").parse(first.format(formatter));
                date2 = new SimpleDateFormat("dd.MM.yyyy").parse(last.format(formatter));
                Long datenow = date2.getTime() - 60000l; //23часа 59минут
                date2 = new Date(datenow);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<Reestr1Viev> reestr1s = reestr1VievService.findAllD(date1, date2);
        model.addAttribute("reestr1s", reestr1s);

        Reestr1Viev reestr1i = reestr1VievService.findAllI(date1, date2);

        model.addAttribute("reestr1i", reestr1i);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("date1", dateFormat1.format(date1));
        model.addAttribute("date2", dateFormat1.format(date2));
        model.addAttribute("user", user);
        return "reestr1";
    }

    @GetMapping("/reestr1/pechatdocx")
    public @ResponseBody
    byte[] reestr1pechatdocx(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal User user,
            HttpServletResponse resp,
            Model model) throws IOException {

        logiService.save(new Logi(new Date(), user.getLogin(), "reestr1pechatdocx param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        Date date1 = new Date();
        Date date2 = new Date();
        if (!(dat1.equals("") && dat2.equals(""))) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date1 = dateyyyyMMdd(dat1);
            date2 = dateyyyyMMdd(dat2);
            Long datenow = date2.getTime() + 86340000l; //23часа 59минут
            date2 = new Date(datenow);
        } else {
            LocalDateTime first = LocalDateTime.now().withDayOfMonth(1);
            LocalDateTime last = first.plusMonths(1);
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            try {
                date1 = new SimpleDateFormat("dd.MM.yyyy").parse(first.format(formatter));
                date2 = new SimpleDateFormat("dd.MM.yyyy").parse(last.format(formatter));
                Long datenow = date2.getTime() - 60000l; //23часа 59минут
                date2 = new Date(datenow);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<Reestr1Viev> reestr1s = reestr1VievService.findAllD(date1, date2);

        InputStream in = null;
        try {
            Shablon shablon = shablonService.findById(1l);

            InputStream inputStream = new ByteArrayInputStream(shablon.getDokument());

            XWPFDocument docxFile = null;
            docxFile = new XWPFDocument(inputStream);
            // открываем файл и считываем его содержимое в объект XWPFDocument

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            for (XWPFParagraph p : docxFile.getParagraphs()) {
                List<XWPFRun> runs = p.getRuns();
                if (runs != null) {
                    for (XWPFRun r : runs) {
                        String text = r.getText(0);
                        if (text != null && text.contains("$")) {
                            text = text.replace("$1", dateFormat.format(date1));
                            text = text.replace("$2", dateFormat.format(date2));
                            r.setText(text, 0);
                        }
                    }
                }
            }

            //находим таблицу
            XWPFTable T = null;
            for (XWPFTable tbl : docxFile.getTables()) {
                T = tbl;
                break;
            }

            //пришлось создать новый документ для параграфов
            XWPFDocument document = new XWPFDocument();

            int fontSize = 11;
            XWPFParagraph paragraph;
            XWPFRun run;

            for (Reestr1Viev reestr1Viev : reestr1s) {
                XWPFTableRow tableRowTwo = T.createRow();

                for (int i = 0; i < 11; i++) { // i < 9
                    paragraph = document.createParagraph();
                    paragraph.setAlignment(ParagraphAlignment.CENTER);
                    run = paragraph.createRun();
                    run.setFontSize(fontSize);
                    run.setBold(false);
                    String st;
                    switch (i) {
                        case 0:
                            st = reestr1Viev.getReg_datestr();
                            break;
                        case 1:
                            st = reestr1Viev.getReg_number();
                            break;
                        case 2:
                            st = reestr1Viev.getAddr();
                            break;
                        case 3:
                            st = reestr1Viev.getName();
                            break;
                        case 4:
                            st = reestr1Viev.getSpravkonv().isA4() ? reestr1Viev.getKol_vo().toString() : "";
                            break;
                        case 5:
                            st = reestr1Viev.getSpravkonv().isC5() ? reestr1Viev.getKol_vo().toString() : "";
                            break;
                        case 6:
                            st = reestr1Viev.getSpravkonv().is110x220() ? reestr1Viev.getKol_vo().toString() : "";
                            break;
                        case 7:
                            st = reestr1Viev.getSpravkonv().ispoly() ? reestr1Viev.getKol_vo().toString() : "";
                            break;

                            //
                        case 8:
                            st = reestr1Viev.getSpravkonv().is11() ? reestr1Viev.getKol_vo().toString() : "";
                            break;
                        case 9:
                            st = reestr1Viev.getSpravkonv().is14() ? reestr1Viev.getKol_vo().toString() : "";
                            break;
                            //

                        default:
                            st = reestr1Viev.getSum();
                            break;
                    }
                    run.setText(st);
                    tableRowTwo.getCell(i).setParagraph(paragraph);
                }
            }

            XWPFTableRow tableRowTwo = T.createRow();

            for (int i = 0; i < 3; i++) {
                paragraph = document.createParagraph();
                run = paragraph.createRun();
                run.setText("");
                tableRowTwo.getCell(i).setParagraph(paragraph);
            }

            Reestr1Viev reestr1i = reestr1VievService.findAllI(date1, date2);

            for (int i = 3; i < 11; i++) { // i < 9
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                run = paragraph.createRun();
                run.setFontSize(fontSize);
                run.setBold(false);
                String st;
                switch (i) {
                    case 3:
                        st = "ИТОГО по РЕЕСТРУ";
                        break;
                    case 4:
                        st = String.valueOf(reestr1i.getId_konv1());
                        break;
                    case 5:
                        st = String.valueOf(reestr1i.getId_konv4());
                        break;
                    case 6:
                        st = String.valueOf(reestr1i.getId_konv5());
                        break;
                    case 7:
                        st = String.valueOf(reestr1i.getId_konv6());
                        break;

                        //
                    case 8:
                        st = String.valueOf(reestr1i.getId_konv11());
                        break;
                    case 9:
                        st = String.valueOf(reestr1i.getId_konv14());
                        break;
                        //

                    default:
                        st = String.valueOf(reestr1i.getSum());
                        break;
                }
                run.setText(st);
                tableRowTwo.getCell(i).setParagraph(paragraph);
            }

            tableRowTwo = T.createRow();

            for (int i = 0; i < 4; i++) {
                paragraph = document.createParagraph();
                run = paragraph.createRun();
                run.setText("");
                tableRowTwo.getCell(i).setParagraph(paragraph);
            }

            for (int i = 4; i < 11; i++) { //i < 9
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                run = paragraph.createRun();
                run.setFontSize(fontSize);
                run.setBold(false);
                String st;
                switch (i) {
                    case 4:
                        st = "Конверт А4";
                        break;
                    case 5:
                        st = "Конверт С5";
                        break;
                    case 6:
                        st = "Конверт 110x220";
                        break;
                    case 7:
                        st = "Полиэтиленовый конверт";
                        break;

                        //
                    case 8:
                        st = "Конверт 110*220 лит А";
                        break;
                    case 9:
                        st = "Конверт с литер. Д";
                        break;
                        //

                    default:
                        st = "Марки";
                        break;
                }
                run.setText(st);
                tableRowTwo.getCell(i).setParagraph(paragraph);
            }

            ByteArrayOutputStream b = new ByteArrayOutputStream();
            docxFile.write(b);

            resp.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", "reestr1.docx");
            resp.setHeader(headerKey, headerValue);
            resp.setContentLength(b.size());
            resp.getOutputStream().write(b.toByteArray());

            in = new ByteArrayInputStream(b.toByteArray());
        } catch (Exception e) {
        }

        return IOUtils.toByteArray(in);
    }

    @GetMapping("/otchpokon")
    public String otchpokon(
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "otchpokon"));

        model.addAttribute("user", user);
        return "otchpokon";
    }

    @GetMapping("/otchpokon/form")
    public String otchpokonform(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "otchpokonform param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        Date date1 = new Date();
        Date date2 = new Date();
        Date date1minusMonths = new Date();
        Date date2minusMonths = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dat1);
            date2 = new SimpleDateFormat("yyyy-MM-dd").parse(dat2);

            LocalDateTime first = date1.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime().minusMonths(1);
            LocalDateTime end = first.plusMonths(1);

            date1minusMonths = new SimpleDateFormat("yyyy-MM-dd").parse(first.toString());
            date2minusMonths = new SimpleDateFormat("yyyy-MM-dd").parse(end.toString());

            Long datenow = date2minusMonths.getTime() - 60000l; //23часа 59минут
            date2minusMonths = new Date(datenow);
        } catch (Exception e) {
        }

        Long datenow = date2.getTime() + 86340000l; //23часа 59минут
        date2 = new Date(datenow);

        List<Reestr1Viev> reestr1s = reestr1VievService.findAllD(date1, date2);
        model.addAttribute("reestr1s", reestr1s);
        //здесь суммы конвертов за месяц
        Reestr1Viev reestr1i = reestr1VievService.findAllI(date1, date2);
        model.addAttribute("reestr1i", reestr1i);//итого

        List<Otchraschodkonv> otchraschodkonv = otchraschodkonvService.findAllD(date1minusMonths, date2minusMonths);

        List<Otchraschodkonv> otchraschodkonv1 = new ArrayList<>();
        List<Otchraschodkonv> otchraschodkonv4 = new ArrayList<>();
//        List<Otchraschodkonv> otchraschodkonv5 = new ArrayList<>();
        List<Otchraschodkonv> otchraschodkonv6 = new ArrayList<>();

        List<Otchraschodkonv> otchraschodkonv7 = new ArrayList<>();
        List<Otchraschodkonv> otchraschodkonv22 = new ArrayList<>();//110х220 чистый

        for (Otchraschodkonv o :
                otchraschodkonv) {
            if (o.getPrihod().getSpravkonv().getId() == 1 && o.getOstatok() != 0) {
                otchraschodkonv1.add(o);
            }
            if (o.getPrihod().getSpravkonv().getId() == 4 && o.getOstatok() != 0) {
                otchraschodkonv4.add(o);
            }
            /*if (o.getPrihod().getSpravkonv().getId() == 6) {
                otchraschodkonv6.add(o);
            }*/
            if (o.getPrihod().getSpravkonv().getId() == 6 && o.getOstatok() != 0) {
                otchraschodkonv6.add(o);
            }

            if (o.getPrihod().getSpravkonv().getId() == 7 && o.getOstatok() != 0) {
                otchraschodkonv7.add(o);
            }

            if (o.getPrihod().getSpravkonv().getId() == 22 && o.getOstatok() != 0) {
                otchraschodkonv22.add(o);
            }

        }
        model.addAttribute("otchraschodkonv1", otchraschodkonv1);
        model.addAttribute("otchraschodkonv4", otchraschodkonv4);
//        model.addAttribute("otchraschodkonv5", otchraschodkonv5);
        model.addAttribute("otchraschodkonv6", otchraschodkonv6);
        model.addAttribute("otchraschodkonv7", otchraschodkonv7);
        model.addAttribute("otchraschodkonv22", otchraschodkonv22);

        List<Prihod> prihods1 = new ArrayList<>();
        List<Prihod> prihods4 = new ArrayList<>();
//        List<Prihod> prihods5 = new ArrayList<>();
        List<Prihod> prihods6 = new ArrayList<>();
        List<Prihod> prihods7 = new ArrayList<>();
        List<Prihod> prihods22 = new ArrayList<>();
        List<Prihod> prihods = prihodService.findAllD(date1, date2);
        for (Prihod p :
                prihods) {
            if (p.getSpravkonv().getId() == 1) {
                prihods1.add(p);
            }
            if (p.getSpravkonv().getId() == 4) {
                prihods4.add(p);
            }
/*            if (p.getSpravkonv().getId() == 5) {
                prihods5.add(p);
            }*/
            if (p.getSpravkonv().getId() == 6) {
                prihods6.add(p);
            }

            if (p.getSpravkonv().getId() == 7) {
                prihods7.add(p);
            }

            if (p.getSpravkonv().getId() == 22) {
                prihods22.add(p);
            }
        }

        model.addAttribute("prihods1", prihods1);
        model.addAttribute("prihods4", prihods4);
//        model.addAttribute("prihods5", prihods5);
        model.addAttribute("prihods6", prihods6);
        model.addAttribute("prihods7", prihods7);
        model.addAttribute("prihods22", prihods22);


        model.addAttribute("user", user);
        return "fragment/otchpokonfrag :: form";
    }


    @GetMapping("/otchpokon/link")
    @Transactional
    public String otchpokonlink(
            @RequestParam(value = "date", defaultValue = "") String date,
            @RequestParam(value = "ota4", defaultValue = "") String ota4,
            @RequestParam(value = "pa4", defaultValue = "") String pa4,
            @RequestParam(value = "otc5", defaultValue = "") String otc5,
            @RequestParam(value = "pc5", defaultValue = "") String pc5,
/*            @RequestParam(value = "ot110x220", defaultValue = "") String ot110x220,
            @RequestParam(value = "p110x220", defaultValue = "") String p110x220,*/
            @RequestParam(value = "otpk", defaultValue = "") String otpk,
            @RequestParam(value = "ppk", defaultValue = "") String ppk,
            @RequestParam(value = "otte", defaultValue = "") String otte,
            @RequestParam(value = "pte", defaultValue = "") String pte,

            @RequestParam(value = "ot110x220clear", defaultValue = "") String ot110x220clear,
            @RequestParam(value = "p110x220clear", defaultValue = "") String p110x220clear,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "otchpokonlink param=" +
                " date = " + date +
                " ota4 = " + ota4 +
                " pa4 " + pa4 +
                " otc5 = " + otc5 +
                " pc5 = " + pc5 +
/*                " ot110x220 = " + ot110x220 +
                " p110x220 = " + p110x220 +*/
                " otpk = " + otpk +
                " ppk = " + ppk +
                " otte = " + otte +
                " pte = " + pte +
                " ot110x220clear = " + ot110x220clear +
                " p110x220clear = " + p110x220clear
        ));

        Date date1 = new Date();

        Date date1Begin = new Date();
        Date date2End = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            String[] subStr;
            String delimeter = "-"; // Разделитель
            subStr = date.split(delimeter);
            LocalDate first = LocalDate.of(Integer.valueOf(subStr[0]), Integer.valueOf(subStr[1]), 1);
            LocalDate end = first.plusMonths(1);
            //LocalDate end = first.plusMonths(1).minusDays(1);

            date1Begin = new SimpleDateFormat("yyyy-MM-dd").parse(first.toString());
            date2End = new SimpleDateFormat("yyyy-MM-dd").parse(end.toString());
        } catch (Exception e) {
        }

        //todo поработать с датами
        //Long datenow = date2.getTime() + 86340000l; //23часа 59минут
        //date2 = new Date(datenow);

        //Удаляем старое
        otchraschodkonvService.Del(date1Begin, date2End);

        String delimeter1 = "!"; // Разделитель
        String delimeter2 = ";"; // Разделитель

        String[] ota41 = ota4.split(delimeter1);
        String[] pa41 = pa4.split(delimeter1);
        String[] otc51 = otc5.split(delimeter1);
        String[] pc51 = pc5.split(delimeter1);
/*        String[] ot110x2201 = ot110x220.split(delimeter1);
        String[] p110x2201 = p110x220.split(delimeter1);*/
        String[] otpk1 = otpk.split(delimeter1);
        String[] ppk1 = ppk.split(delimeter1);

        String[] otte1 = otte.split(delimeter1);
        String[] pte1 = pte.split(delimeter1);

        String[] ot110x220clear1 = ot110x220clear.split(delimeter1);
        String[] p110x220clear1 = p110x220clear.split(delimeter1);



        if (otpk1.length > 0 && !otpk1[0].equals("")) {
            for (String ot :
                    otpk1) {
                String[] sm = ot.split(delimeter2);
                Otchraschodkonv otchraschodkonv = otchraschodkonvService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        otchraschodkonv.getPrihod(),
                        date1,
                        Integer.valueOf(sm[1]),

                        otchraschodkonv.getOstatok() - Integer.valueOf(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (ppk1.length > 0 && !ppk1[0].equals("")) {
            for (String ot :
                    ppk1) {
                String[] sm = ot.split(delimeter2);
                Prihod prihod = prihodService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        prihod,
                        date1,
                        Integer.valueOf(sm[1]),
                        prihod.getKol_vo() - Integer.valueOf(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////// otchpokon
/*        if (ot110x2201.length > 0 && !ot110x2201[0].equals("")) {
            for (String ot :
                    ot110x2201) {
                String[] sm = ot.split(delimeter2);
                Otchraschodkonv otchraschodkonv = otchraschodkonvService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        otchraschodkonv.getPrihod(),
                        date1,
                        Integer.valueOf(sm[1]),
                        otchraschodkonv.getOstatok() - Integer.valueOf(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (p110x2201.length > 0 && !p110x2201[0].equals("")) {
            for (String ot :
                    p110x2201) {
                String[] sm = ot.split(delimeter2);
                Prihod prihod = prihodService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        prihod,
                        date1,
                        Integer.valueOf(sm[1]),
                        prihod.getKol_vo() - Integer.valueOf(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }*/

        if (otc51.length > 0 && !otc51[0].equals("")) {
            for (String ot :
                    otc51) {
                String[] sm = ot.split(delimeter2);
                Otchraschodkonv otchraschodkonv = otchraschodkonvService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        otchraschodkonv.getPrihod(),
                        date1,
                        Integer.valueOf(sm[1]),
                        otchraschodkonv.getOstatok() - Integer.valueOf(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (pc51.length > 0 && !pc51[0].equals("")) {
            for (String ot :
                    pc51) {
                String[] sm = ot.split(delimeter2);
                Prihod prihod = prihodService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        prihod,
                        date1,
                        Integer.valueOf(sm[1]),
                        prihod.getKol_vo() - Integer.valueOf(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (ota41.length > 0 && !ota41[0].equals("")) {
            for (String ot :
                    ota41) {
                String[] sm = ot.split(delimeter2);
                Otchraschodkonv otchraschodkonv = otchraschodkonvService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        otchraschodkonv.getPrihod(),
                        date1,
                        Integer.valueOf(sm[1]),
                        otchraschodkonv.getOstatok() - Integer.valueOf(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (pa41.length > 0 && !pa41[0].equals("")) {
            for (String ot :
                    pa41) {
                String[] sm = ot.split(delimeter2);
                Prihod prihod = prihodService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        prihod,
                        date1,
                        Integer.valueOf(sm[1]),
                        prihod.getKol_vo() - Integer.valueOf(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        //--otte1
        if (otte1.length > 0 && !otte1[0].equals("")) {
            for (String ot :
                    otte1) {
                String[] sm = ot.split(delimeter2);
                Otchraschodkonv otchraschodkonv = otchraschodkonvService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        otchraschodkonv.getPrihod(),
                        date1,
                        Integer.valueOf(sm[1]),

                        otchraschodkonv.getOstatok() - Integer.valueOf(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (pte1.length > 0 && !pte1[0].equals("")) {
            for (String ot :
                    pte1) {
                String[] sm = ot.split(delimeter2);
                Prihod prihod = prihodService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        prihod,
                        date1,
                        Integer.valueOf(sm[1]),
                        prihod.getKol_vo() - Integer.valueOf(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        //
        if (ot110x220clear1.length > 0 && !ot110x220clear1[0].equals("")) {
            for (String ot :
                    ot110x220clear1) {
                String[] sm = ot.split(delimeter2);
                Otchraschodkonv otchraschodkonv = otchraschodkonvService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        otchraschodkonv.getPrihod(),
                        date1,
                        Integer.valueOf(sm[1]),
                        otchraschodkonv.getOstatok() - Integer.valueOf(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (p110x220clear1.length > 0 && !p110x220clear1[0].equals("")) {
            for (String ot :
                    p110x220clear1) {
                String[] sm = ot.split(delimeter2);
                Prihod prihod = prihodService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        prihod,
                        date1,
                        Integer.valueOf(sm[1]),
                        prihod.getKol_vo() - Integer.valueOf(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("date1", dateFormat1.format(date1Begin));
        model.addAttribute("date2", dateFormat1.format(date2End));

        model.addAttribute("user", user);
        return "fragment/otchpokonfrag :: link"; //TODO дошли до сюда
    }

    @GetMapping("/otchpokon/pechatdocx")
    public @ResponseBody
    byte[] otchpokonpechatdocx(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal User user,
            HttpServletResponse resp,
            Model model) throws IOException {

        logiService.save(new Logi(new Date(), user.getLogin(), "otchpokonpechatdocx param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        Date date1 = new Date();
        Date date2 = new Date();

        date1 = dateyyyyMMdd(dat1);
        date2 = dateyyyyMMdd(dat2);

        //todo поработать с датами
        //Long datenow = date2.getTime() + 86340000l; //23часа 59минут
        //date2 = new Date(datenow);

        List<Otchraschodkonv> otchraschodkonvs = otchraschodkonvService.findAllD(date1, date2);

        InputStream in = null;
        try {
            Shablon shablon = shablonService.findById(3l);

            InputStream inputStream = new ByteArrayInputStream(shablon.getDokument());

            XWPFDocument docxFile = null;
            docxFile = new XWPFDocument(inputStream);
            // открываем файл и считываем его содержимое в объект XWPFDocument

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            for (XWPFParagraph p : docxFile.getParagraphs()) {
                List<XWPFRun> runs = p.getRuns();
                if (runs != null) {
                    for (XWPFRun r : runs) {
                        String text = r.getText(0);
                        if (text != null && text.contains("$")) {
                            text = text.replace("$1", dateFormat.format(date1));
                            text = text.replace("$2", dateFormat.format(date2));
                            r.setText(text, 0);
                        }
                    }
                }
            }

            //находим таблицу
            XWPFTable T = null;
            for (XWPFTable tbl : docxFile.getTables()) {
                T = tbl;
                break;
            }

            //пришлось создать новый документ для параграфов
            XWPFDocument document = new XWPFDocument();

            int fontSize = 13;
            XWPFParagraph paragraph;
            XWPFRun run;

            int ras = 0;     //для итоговых сумм
            double s1 = 0;
            double s2 = 0;
            double s3 = 0;
            double s4 = 0;
            XWPFTableRow tableRowTwo;
            for (Otchraschodkonv otchraschodkonv : otchraschodkonvs) {
                tableRowTwo = T.createRow();

                int bilo = 0;

                boolean b = prihodService.findbyDatId(
                        date1, date2, otchraschodkonv.getPrihod().getId());

                for (int i = 0; i < 10; i++) {
                    paragraph = document.createParagraph();
                    paragraph.setAlignment(ParagraphAlignment.CENTER);
                    run = paragraph.createRun();
                    run.setFontSize(fontSize);
                    run.setBold(false);
                    String st;

                    switch (i) {
                        case 0:
                            st = otchraschodkonv.getPrihod().getFullName();
                            break;
                        case 1:

                            st = okrug(otchraschodkonv.getPrihod().getPrice());
                            break;
                        case 2:
                            if (b) {
                                bilo = otchraschodkonv.getOstatok() + otchraschodkonv.getReestr1();
                                st = String.valueOf(bilo);
                            } else
                                st = "";
                            break;
                        case 3:
                            if (b) {
                                st = okrug(otchraschodkonv.getPrihod().getPrice() * bilo);
                                s1 += otchraschodkonv.getPrihod().getPrice() * bilo;
                            } else
                                st = "";
                            break;
                        case 4:
                            if (!b) { //  false = приход
                                bilo = otchraschodkonv.getPrihod().getKol_vo();
                                st = String.valueOf(bilo);
                            } else
                                st = "";
                            break;
                        case 5:
                            if (!b) {
                                st = okrug(otchraschodkonv.getPrihod().getPrice() * bilo);
                                s2 += otchraschodkonv.getPrihod().getPrice() * bilo;
                            } else
                                st = "";
                            break;
                        case 6:
                            if (otchraschodkonv.getRashod() != 0) {
                                st = String.valueOf(otchraschodkonv.getRashod());
                                ras += otchraschodkonv.getRashod();
                            } else
                                st = "";
                            break;
                        case 7:
                            if (otchraschodkonv.getRashod() != 0) {
                                st = okrug(otchraschodkonv.getPrihod().getPrice() * (otchraschodkonv.getRashod()));
                                s3 += otchraschodkonv.getPrihod().getPrice() * (otchraschodkonv.getRashod());
                            } else
                                st = "";
                            break;
                        case 8:
                            st = String.valueOf(bilo - otchraschodkonv.getRashod());
                            break;
                        default:

                            st = okrug(
                                    (bilo - otchraschodkonv.getRashod()) * otchraschodkonv.getPrihod().getPrice());
                            s4 += (bilo - otchraschodkonv.getRashod()) * otchraschodkonv.getPrihod().getPrice();

                            break;
                    }

                    run.setText(st);

                    if (i < tableRowTwo.getTableICells().size()) {
                        tableRowTwo.getCell(i).setParagraph(paragraph);
                    } else tableRowTwo.createCell().setParagraph(paragraph);
                }

                if (otchraschodkonv.getReestr1() != 0/* && otchraschodkonv.getReestr2()!=0*/) {
                    tableRowTwo = T.createRow();
                    for (int i = 0; i < 10; i++) {
                        paragraph = document.createParagraph();
                        paragraph.setAlignment(ParagraphAlignment.CENTER);
                        run = paragraph.createRun();
                        run.setFontSize(fontSize);
                        run.setBold(false);
                        String st;
                        switch (i) {
                            case 0:
                                st = "Реестр №1";
                                break;
                            case 6:
                                st = String.valueOf(otchraschodkonv.getReestr1());
                                break;
                            default:
                                st = "";
                                break;
                        }
                        run.setText(st);

                        if (i < tableRowTwo.getTableICells().size()) {
                            tableRowTwo.getCell(i).setParagraph(paragraph);
                        } else tableRowTwo.createCell().setParagraph(paragraph);
                    }
                }
            }

            tableRowTwo = T.createRow();
            for (int i = 0; i < 10; i++) {
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                run = paragraph.createRun();
                run.setFontSize(fontSize);
                run.setBold(false);
                String st;
                switch (i) {
                    case 0:
                        st = "ИТОГО";
                        break;
                    case 3:
                        st = okrug(s1);
                        break;
                    case 5:
                        st = okrug(s2);
                        break;
                    case 6:
                        st = String.valueOf(ras);
                        break;
                    case 7:
                        st = okrug(s3);
                        break;
                    case 9:
                        st = okrug(s4);
                        break;
                    default:
                        st = "";
                        break;
                }
                run.setText(st);

                if (i < tableRowTwo.getTableICells().size()) {
                    tableRowTwo.getCell(i).setParagraph(paragraph);
                } else tableRowTwo.createCell().setParagraph(paragraph);
            }

            ByteArrayOutputStream b = new ByteArrayOutputStream();
            docxFile.write(b);

            resp.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", "ot.docx");
            resp.setHeader(headerKey, headerValue);
            resp.setContentLength(b.size());
            resp.getOutputStream().write(b.toByteArray());

            in = new ByteArrayInputStream(b.toByteArray());
        } catch (Exception e) {
            logiService.save(new Logi(new Date(), user.getLogin(), "ERROR otchpokonpechatdocx param=" +
                    " date1 = " + dat1 +
                    " date2 = " + dat2
            ));
        }

        return IOUtils.toByteArray(in);
    }

    @GetMapping("/otchmarkandkonv")
    public String otchmarkandkonv(
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "otchmarkandkonv"));

        model.addAttribute("user", user);
        return "otchmarkandkonv";
    }

    @GetMapping("/otchmarkandkonv/form")
    public String otchmarkandkonvform(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "otchmarkandkonvform param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        Date date1 = new Date();
        Date date2 = new Date();
        Date date1minusMonths = new Date();
        Date date2minusMonths = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dat1);
            date2 = new SimpleDateFormat("yyyy-MM-dd").parse(dat2);

            LocalDateTime first = date1.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime().minusMonths(1);
            LocalDateTime end = first.plusMonths(1);

            date1minusMonths = new SimpleDateFormat("yyyy-MM-dd").parse(first.toString());
            date2minusMonths = new SimpleDateFormat("yyyy-MM-dd").parse(end.toString());

            Long datenow = date2minusMonths.getTime() - 60000l; //23часа 59минут
            date2minusMonths = new Date(datenow);
        } catch (Exception e) {
        }

        //поработать с датами
        Long datenow = date2.getTime() + 86340000l; //23часа 59минут
        date2 = new Date(datenow);

        //здесь суммы конвертов за месяц 110x120
        Reestr1Viev reestr1i = reestr1VievService.findAllI(date1, date2);
        model.addAttribute("reestr1i", reestr1i);

        List<Pravopriem> pravopriems = pravopriemService.findAllD(date1, date2);

        int k = 0, mzp = 0;

        for (int i = 0; i < pravopriems.size(); i++) {
            Pravopriem r = pravopriems.get(i);
            mzp += r.getMarki_k_zak_pis();
            k += r.getKonvert_d();
        }

        model.addAttribute("pravopriems", pravopriems);
/*        model.addAttribute("so", so);
        model.addAttribute("m", m);*/
        model.addAttribute("k", Double.valueOf(k));
        model.addAttribute("r2", Double.valueOf(mzp));
        model.addAttribute("r1", Double.valueOf(reestr1i.getSum()));
        model.addAttribute("ob", MyNumbers.okrug(Double.valueOf(mzp) + Double.valueOf(reestr1i.getSum())));

        List<Prihodmarki> prihodmarkis = prihodmarkiService.findAllDat(date1, date2);
        List<Otchmark> otchmark = otchmarkService.findAllD(date1minusMonths, date2minusMonths);

        model.addAttribute("ostat", MyNumbers.okrug(otchmark.size() != 0 ? Double.valueOf(otchmark.get(0).getOstatok()) : 0));
        model.addAttribute("prihodm", MyNumbers.okrug(prihodmarkis.size() != 0 ? prihodmarkis.get(0).getPrice() : 0));

        List<Otchmarkandkonv> otchmarkandkonvD = otchmarkandkonvService.findAllDatOnlyTypeD(date1minusMonths, date2minusMonths);
        List<Otchmarkandkonv> otchmarkandkonv110x120 = otchmarkandkonvService.findAllDatOnlyType110x120(date1minusMonths, date2minusMonths);

        model.addAttribute("otchmarkandkonvD", otchmarkandkonvD);
        model.addAttribute("otchmarkandkonv110x120", otchmarkandkonv110x120);

        List<Prihod> prihods3 = new ArrayList<>();
        List<Prihod> prihods5 = new ArrayList<>();
        List<Prihod> prihods = prihodService.findAllD(date1, date2);
        for (Prihod p :
                prihods) {
            if (p.getSpravkonv().getId() == 3) {
                prihods3.add(p);
            }
            if (p.getSpravkonv().getId() == 5) {
                prihods5.add(p);
            }
        }

        model.addAttribute("prihods3", prihods3);
        model.addAttribute("prihods5", prihods5);

        model.addAttribute("user", user);
        return "fragment/otchmarkandkonvfrag :: form";
    }

    @GetMapping("/otchmarkandkonv/link")
    @Transactional
    public String otchmarkandkonvlink(
            @RequestParam(value = "date", defaultValue = "") String date,
            @RequestParam(value = "otD", defaultValue = "") String otD,
            @RequestParam(value = "pD", defaultValue = "") String pD,
            @RequestParam(value = "ot110x220", defaultValue = "") String ot110x220,
            @RequestParam(value = "p110x220", defaultValue = "") String p110x220,
            @RequestParam(value = "reestr1m", defaultValue = "") String reestr1m,
            @RequestParam(value = "reestr2m", defaultValue = "") String reestr2m,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "otchmarkandkonvlink param=" +
                " date = " + date +
                " otD = " + otD +
                " pD " + pD +
                " ot110x220 = " + ot110x220 +
                " p110x220 = " + p110x220 +
                " reestr1m = " + reestr1m +
                " reestr2m = " + reestr2m
        ));

        Date date1 = new Date();

        Date date1Begin = new Date();
        Date date2End = new Date();

        Date date1BeginMinusMonths = new Date();
        Date date2EndMinusMonths = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            String[] subStr;
            String delimeter = "-"; // Разделитель
            subStr = date.split(delimeter);
            LocalDate first = LocalDate.of(Integer.valueOf(subStr[0]), Integer.valueOf(subStr[1]), 1);
            LocalDate end = first.plusMonths(1);
            //LocalDate end = first.plusMonths(1).minusDays(1);

            date1Begin = new SimpleDateFormat("yyyy-MM-dd").parse(first.toString());
            date2End = new SimpleDateFormat("yyyy-MM-dd").parse(end.toString());

            date1BeginMinusMonths = new SimpleDateFormat("yyyy-MM-dd").parse(first.minusMonths(1).toString());
            date2EndMinusMonths = new SimpleDateFormat("yyyy-MM-dd").parse(end.minusMonths(1).toString());
        } catch (Exception e) {
        }

        //todo поработать с датами
        //Long datenow = date2.getTime() + 86340000l; //23часа 59минут
        //date2 = new Date(datenow);

        otchmarkandkonvService.Del(date1Begin, date2End);//Удаляем старое
        otchmarkService.Del(date1Begin, date2End);//Удаляем старое

        //Работа с марками
        List<Prihodmarki> prihodmarkis = prihodmarkiService.findAllDat(date1Begin, date2End);
        Double prihodm = prihodmarkis.size() != 0 ? prihodmarkis.get(0).getPrice() : 0D;

        List<Otchmark> otchmarks = otchmarkService.findAllD(date1BeginMinusMonths, date2EndMinusMonths);
        Double ostatm = otchmarks.size() != 0 ? otchmarks.get(0).getOstatok() : 0;

        Otchmark otchmark = new Otchmark(
                date1,
                Double.valueOf(reestr1m),
                Double.valueOf(reestr2m),
                Double.valueOf(ostatm + prihodm - (Double.valueOf(reestr1m) + Double.valueOf(reestr2m)))
        );
        otchmarkService.save(otchmark);
        //--------

        String delimeter1 = "!"; // Разделитель
        String delimeter2 = ";"; // Разделитель

        String[] otD1 = otD.split(delimeter1);
        String[] pD1 = pD.split(delimeter1);
        String[] ot110x2201 = ot110x220.split(delimeter1);
        String[] p110x2201 = p110x220.split(delimeter1);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if (ot110x2201.length > 0 && !ot110x2201[0].equals("")) {
            for (String ot :
                    ot110x2201) {
                String[] sm = ot.split(delimeter2);
                Otchmarkandkonv otchmarkandkonv = otchmarkandkonvService.findById(Long.valueOf(sm[0]));
                Otchmarkandkonv otchmarkandkonvNEW = new Otchmarkandkonv(
                        otchmarkandkonv.getPrihod(),
                        date1,
                        Integer.valueOf(sm[1]),
                        otchmarkandkonv.getOstatok() - Integer.valueOf(sm[1])
                );
                otchmarkandkonvService.save(otchmarkandkonvNEW);
            }
        }

        if (p110x2201.length > 0 && !p110x2201[0].equals("")) {
            for (String ot :
                    p110x2201) {
                String[] sm = ot.split(delimeter2);
                Prihod prihod = prihodService.findById(Long.valueOf(sm[0]));
                Otchmarkandkonv otchmarkandkonvNEW = new Otchmarkandkonv(
                        prihod,
                        date1,
                        Integer.valueOf(sm[1]),
                        prihod.getKol_vo() - Integer.valueOf(sm[1])
                );
                otchmarkandkonvService.save(otchmarkandkonvNEW);
            }
        }

        if (otD1.length > 0 && !otD1[0].equals("")) {
            for (String ot :
                    otD1) {
                String[] sm = ot.split(delimeter2);
                Otchmarkandkonv otchmarkandkonv = otchmarkandkonvService.findById(Long.valueOf(sm[0]));
                Otchmarkandkonv otchmarkandkonvNEW = new Otchmarkandkonv(
                        otchmarkandkonv.getPrihod(),
                        date1,
                        Integer.valueOf(sm[1]),
                        otchmarkandkonv.getOstatok() - Integer.valueOf(sm[1])
                );
                otchmarkandkonvService.save(otchmarkandkonvNEW);
            }
        }

        if (pD1.length > 0 && !pD1[0].equals("")) {
            for (String ot :
                    pD1) {
                String[] sm = ot.split(delimeter2);
                Prihod prihod = prihodService.findById(Long.valueOf(sm[0]));
                Otchmarkandkonv otchmarkandkonvNEW = new Otchmarkandkonv(
                        prihod,
                        date1,
                        Integer.valueOf(sm[1]),
                        prihod.getKol_vo() - Integer.valueOf(sm[1])
                );
                otchmarkandkonvService.save(otchmarkandkonvNEW);
            }
        }

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("date1", dateFormat1.format(date1Begin));
        model.addAttribute("date2", dateFormat1.format(date2End));

        model.addAttribute("user", user);
        return "fragment/otchmarkandkonvfrag :: link";
    }

    @GetMapping("/otchmarkandkonv/pechatdocx")
    public @ResponseBody
    byte[] otchmarkandkonvpechatdocx(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal User user,
            HttpServletResponse resp,
            Model model) throws IOException {

        logiService.save(new Logi(new Date(), user.getLogin(), "otchmarkandkonvpechatdocx param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        Date date1 = new Date();
        Date date2 = new Date();

        date1 = dateyyyyMMdd(dat1);
        date2 = dateyyyyMMdd(dat2);

        //todo поработать с датами
        //Long datenow = date2.getTime() + 86340000l; //23часа 59минут
        //date2 = new Date(datenow);

        List<Otchmarkandkonv> otchmarkandkonvs = otchmarkandkonvService.findAllD(date1, date2);

        InputStream in = null;
        try {
            Shablon shablon = shablonService.findById(4l);

            InputStream inputStream = new ByteArrayInputStream(shablon.getDokument());

            XWPFDocument docxFile = null;
            docxFile = new XWPFDocument(inputStream);
            // открываем файл и считываем его содержимое в объект XWPFDocument

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            for (XWPFParagraph p : docxFile.getParagraphs()) {
                List<XWPFRun> runs = p.getRuns();
                if (runs != null) {
                    for (XWPFRun r : runs) {
                        String text = r.getText(0);
                        if (text != null && text.contains("$")) {
                            text = text.replace("$1", dateFormat.format(date1));
                            text = text.replace("$2", dateFormat.format(date2));
                            r.setText(text, 0);
                        }
                    }
                }
            }

            //находим таблицу
            XWPFTable T = null;
            for (XWPFTable tbl : docxFile.getTables()) {
                T = tbl;
                break;
            }

            //пришлось создать новый документ для параграфов
            XWPFDocument document = new XWPFDocument();

            int fontSize = 13;
            XWPFParagraph paragraph;
            XWPFRun run;

            int ras = 0;
            double s1 = 0;
            double s2 = 0;
            double s3 = 0;
            double s4 = 0;
            XWPFTableRow tableRowTwo;
            for (Otchmarkandkonv otchmarkandkonv : otchmarkandkonvs) {
                tableRowTwo = T.createRow();

                int bilo = 0;

                boolean b = prihodService.findbyDatId(
                        date1, date2, otchmarkandkonv.getPrihod().getId());

                for (int i = 0; i < 10; i++) {
                    paragraph = document.createParagraph();
                    paragraph.setAlignment(ParagraphAlignment.CENTER);
                    run = paragraph.createRun();
                    run.setFontSize(fontSize);
                    run.setBold(false);

                    String st;

                    switch (i) {
                        case 0:
                            st = otchmarkandkonv.getPrihod().getFullName();
                            break;
                        case 1:
                            st = okrug(otchmarkandkonv.getPrihod().getPrice());
                            break;
                        case 2:
                            if (b) {
                                bilo = otchmarkandkonv.getOstatok() + otchmarkandkonv.getReestr1();
                                st = String.valueOf(bilo);
                            } else
                                st = "";
                            break;
                        case 3:
                            if (b) {
                                st = okrug(otchmarkandkonv.getPrihod().getPrice() * bilo);
                                s1 += otchmarkandkonv.getPrihod().getPrice() * bilo;
                            } else
                                st = "";
                            break;
                        case 4:
                            if (!b) {
                                bilo = otchmarkandkonv.getPrihod().getKol_vo();
                                st = String.valueOf(bilo);
                            } else
                                st = "";
                            break;
                        case 5:
                            if (!b) {
                                st = okrug(otchmarkandkonv.getPrihod().getPrice() * bilo);
                                s2 += otchmarkandkonv.getPrihod().getPrice() * bilo;
                            } else
                                st = "";
                            break;
                        case 6:
                            if (otchmarkandkonv.getReestr1() != 0) {
                                st = String.valueOf(otchmarkandkonv.getReestr1());
                                ras += otchmarkandkonv.getReestr1();
                            } else
                                st = "";
                            break;
                        case 7:
                            if (otchmarkandkonv.getReestr1() != 0) {
                                st = okrug(otchmarkandkonv.getPrihod().getPrice() * (otchmarkandkonv.getReestr1()));
                                s3 += otchmarkandkonv.getPrihod().getPrice() * (otchmarkandkonv.getReestr1());
                            } else
                                st = "";
                            break;
                        case 8:
                            st = String.valueOf(bilo - otchmarkandkonv.getReestr1());
                            break;
                        default:

                                st = okrug(
                                        (bilo - otchmarkandkonv.getReestr1()) * otchmarkandkonv.getPrihod().getPrice());
                                s4 += (bilo - otchmarkandkonv.getReestr1()) * otchmarkandkonv.getPrihod().getPrice();

                            break;
                    }

                    run.setText(st);

                    if (i < tableRowTwo.getTableICells().size()) {
                        tableRowTwo.getCell(i).setParagraph(paragraph);
                    } else tableRowTwo.createCell().setParagraph(paragraph);
                }

                if (otchmarkandkonv.getReestr1() != 0) {
                    tableRowTwo = T.createRow();
                    for (int i = 0; i < 10; i++) {
                        paragraph = document.createParagraph();
                        paragraph.setAlignment(ParagraphAlignment.CENTER);
                        run = paragraph.createRun();
                        run.setFontSize(fontSize);
                        run.setBold(false);
                        String st;
                        switch (i) {
                            case 0:
                                if (otchmarkandkonv.getPrihod().getSpravkonv().isD()) {
                                    st = "Реестр заказных писем";
                                } else
                                    st = "Реестр №1";
                                break;
                            case 6:
                                st = String.valueOf(otchmarkandkonv.getReestr1());
                                break;
                            default:
                                st = "";
                                break;
                        }
                        run.setText(st);

                        if (i < tableRowTwo.getTableICells().size()) {
                            tableRowTwo.getCell(i).setParagraph(paragraph);
                        } else tableRowTwo.createCell().setParagraph(paragraph);
                    }
                }
            }


            //Замутить марки!!!!!!!!!!!!
            Otchmark otchmark = otchmarkService.findAllD(date1, date2).get(0);

            List<Prihodmarki> prihodmarkis = prihodmarkiService.findAllDat(date1, date2);
            Double prihodm = prihodmarkis.size() != 0 ? prihodmarkis.get(0).getPrice() : 0D;

            tableRowTwo = T.createRow();
            for (int i = 0; i < 10; i++) {
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                run = paragraph.createRun();
                run.setFontSize(fontSize);
                run.setBold(false);
                String st;
                switch (i) {
                    case 0:
                        st = "Марки в том числе:";
                        break;
                    case 3:
                        st = okrug(otchmark.getOstatok() + otchmark.getRashod1() + otchmark.getRashod2() - prihodm);
                        s1 += otchmark.getOstatok() + otchmark.getRashod1() + otchmark.getRashod2() - prihodm;
                        break;
                    case 5:
                        st = prihodm == 0 ? "" : okrug(prihodm);
                        s2 += prihodm;
                        break;
                    case 7:
                        st = okrug(otchmark.getRashod1() + otchmark.getRashod2());
                        s3 += otchmark.getRashod1() + otchmark.getRashod2();
                        break;
                    case 9:
                        st = okrug(otchmark.getOstatok());
                        s4 += otchmark.getOstatok();
                        break;
                    default:
                        st = "";
                        break;
                }
                run.setText(st);

                if (i < tableRowTwo.getTableICells().size()) {
                    tableRowTwo.getCell(i).setParagraph(paragraph);
                } else tableRowTwo.createCell().setParagraph(paragraph);
            }

            tableRowTwo = T.createRow();
            for (int i = 0; i < 10; i++) {
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                run = paragraph.createRun();
                run.setFontSize(fontSize);
                run.setBold(false);
                String st;
                switch (i) {
                    case 0:
                        st = "Реестр №1";
                        break;
                    case 7:
                        st = okrug(otchmark.getRashod1());
                        break;
                    default:
                        st = "";
                        break;
                }
                run.setText(st);

                if (i < tableRowTwo.getTableICells().size()) {
                    tableRowTwo.getCell(i).setParagraph(paragraph);
                } else tableRowTwo.createCell().setParagraph(paragraph);
            }

            tableRowTwo = T.createRow();
            for (int i = 0; i < 10; i++) {
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                run = paragraph.createRun();
                run.setFontSize(fontSize);
                run.setBold(false);
                String st;
                switch (i) {
                    case 0:
                        st = "Реестр заказных писем";
                        break;
                    case 7:
                        st = okrug(otchmark.getRashod2());
                        break;
                    default:
                        st = "";
                        break;
                }
                run.setText(st);

                if (i < tableRowTwo.getTableICells().size()) {
                    tableRowTwo.getCell(i).setParagraph(paragraph);
                } else tableRowTwo.createCell().setParagraph(paragraph);
            }


            tableRowTwo = T.createRow();
            for (int i = 0; i < 10; i++) {
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                run = paragraph.createRun();
                run.setFontSize(fontSize);
                run.setBold(false);
                String st;
                switch (i) {
                    case 0:
                        st = "ИТОГО";
                        break;
                    case 3:
                        st = okrug(s1);
                        break;
                    case 5:
                        st = okrug(s2);
                        break;
                    case 7:
                        st = okrug(s3);
                        break;
                    case 9:
                        st = okrug(s4);
                        break;
                    default:
                        st = "";
                        break;
                }
                run.setText(st);

                if (i < tableRowTwo.getTableICells().size()) {
                    tableRowTwo.getCell(i).setParagraph(paragraph);
                } else tableRowTwo.createCell().setParagraph(paragraph);
            }

            ByteArrayOutputStream b = new ByteArrayOutputStream();
            docxFile.write(b);

            resp.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", "otm.docx");
            resp.setHeader(headerKey, headerValue);
            resp.setContentLength(b.size());
            resp.getOutputStream().write(b.toByteArray());

            in = new ByteArrayInputStream(b.toByteArray());
        } catch (Exception e) {
        }

        return IOUtils.toByteArray(in);
    }

    @GetMapping("/avansotch")
    public String avansotch(
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "Вход в авансовый отчет avansotch"));

        model.addAttribute("user", user);
        return "avansotch";
    }


    @GetMapping("/spravkonv")
    public String spravkonv(
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "spravkonv"));

        List<Spravkonv> spravkonvs = spravkonvService.findAll();

        model.addAttribute("spravkonvs", spravkonvs);
        model.addAttribute("user", user);
        return "spravkonv";
    }

    @GetMapping("/spravkonv/add")
    public String spravkonvadd(
            @RequestParam String typekonv,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "spravkonvadd param=" +
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

    @GetMapping("/spravkonv/del")
    public String spravkonvdel(
            @RequestParam Long id,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "spravkonvdel param=" +
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

    @GetMapping("/vidanykonv")
    public String vidanykonv(
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "vidanykonv"));

        List<Vidanykonv> vidanykonvs = vidanykonvService.findAll();

        model.addAttribute("vidanykonvs", vidanykonvs);
        model.addAttribute("user", user);
        return "vidanykonv";
    }

    @GetMapping("/vidanykonv/add")
    public String vidanykonvadd(
            @RequestParam String vidanykonv,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "vidanykonvadd param=" +
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

    @GetMapping("/vidanykonv/del")
    public String vidanykonvdel(
            @RequestParam Long id,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "vidanykonvdel param=" +
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

    @GetMapping("/prihod")
    public String prihod(
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "prihod"));

        List<Prihod> prihods = prihodService.findAll();
        List<Spravkonv> spravkonvs = spravkonvService.findAll();
        model.addAttribute("spravkonvs", spravkonvs);
        model.addAttribute("prihods", prihods);
        model.addAttribute("user", user);
        return "prihod";
    }

    @GetMapping("/prihod/add")
    public String prihodadd(
            @RequestParam String prefix,
            @RequestParam String index,
            @RequestParam String price,
            @RequestParam String kol_vo,
            @RequestParam Long id_konv,
            @RequestParam String date,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "prihodadd param=" +
                " prefix = " + prefix +
                " index = " + index +
                " price " + price +
                " kol_vo = " + kol_vo +
                " id_konv = " + id_konv +
                " date = " + date
        ));

        Spravkonv spravkonv = spravkonvService.findById(id_konv);
        Date date1 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
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

    @GetMapping("/prihod/del")
    public String prihoddel(
            @RequestParam Long id,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "prihoddel param=" +
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


    @GetMapping("/prihodmarki")
    public String prihodmarki(
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "prihodmarki"));

        List<Prihodmarki> prihods = prihodmarkiService.findAll();

        model.addAttribute("prihods", prihods);
        model.addAttribute("user", user);
        return "prihodmarki";
    }

    @GetMapping("/prihodmarki/add")
    public String prihodmarkiadd(
            @RequestParam String price,
            @RequestParam String date,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "prihodmarkiadd param=" +
                " price = " + price +
                " date = " + date
        ));

        Date date1 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("errtext", "Не удалось конвертировать формат!");
            return "fragment/err :: error";
        }

        Prihodmarki prihodmarki = new Prihodmarki(Double.valueOf(price), date1);
        prihodmarkiService.save(prihodmarki);

        List<Prihodmarki> prihods = prihodmarkiService.findAll();
        model.addAttribute("prihods", prihods);

        model.addAttribute("user", user);
        return "fragment/prihodmarkifrag :: tablekonv";
    }

    @GetMapping("/prihodmarki/del")
    public String prihodmarkidel(
            @RequestParam Long id,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "prihodmarkidel param=" +
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

    @GetMapping("/konv")
    public String konv(
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "konv"));

        model.addAttribute("user", user);
        return "konv";
    }


    @GetMapping("/avans")
    public String avans(
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "avans"));

        model.addAttribute("user", user);
        return "avans";
    }

    @GetMapping("/avans/link")
    public String avanslink(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "Ссылка на авансовый отчет avanslink param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        Date date1 = new Date();
        Date date2 = new Date();

        date1 = dateyyyyMMdd(dat1);
        date2 = dateyyyyMMdd(dat2);

        //todo поработать с датами
        //Long datenow = date2.getTime() + 86340000l; //23часа 59минут
        //date2 = new Date(datenow);

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("date1", dateFormat1.format(date1));
        model.addAttribute("date2", dateFormat1.format(date2));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        model.addAttribute("date11", dateFormat.format(date1));
        model.addAttribute("date22", dateFormat.format(date2));


        model.addAttribute("user", user);
        return "fragment/avansfrag :: link";
    }

    @GetMapping("/avans/pechatdocx")
    public @ResponseBody
    byte[] avanspechatdocx(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal User user,
            HttpServletResponse resp,
            Model model) throws IOException {

        logiService.save(new Logi(new Date(), user.getLogin(), "Печать отчета avanspechatdocx param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        //todo поработать с датами
        //Long datenow = date2.getTime() + 86340000l; //23часа 59минут
        //date2 = new Date(datenow);

        Date date1 = new Date();
        Date date2 = new Date();
        Date date1BeginMinusMonths = new Date();
        Date date2EndMinusMonths = new Date();
        date1 = dateyyyyMMdd(dat1);
        date2 = dateyyyyMMdd(dat2);
        String mounth = "";
        String god = "";
        String day = "";
        String lastDay = "";
        try {
            String[] subStr;
            String delimeter = "-"; // Разделитель
            subStr = dat2.split(delimeter);
            LocalDate first = LocalDate.of(Integer.valueOf(subStr[0]), Integer.valueOf(subStr[1]), 1);
            //LocalDate end = first.plusMonths(1).minusDays(1);
            LocalDate end = first.plusMonths(1);
            date1BeginMinusMonths = new SimpleDateFormat("yyyy-MM-dd").parse(first.minusMonths(1).toString());
            date2EndMinusMonths = new SimpleDateFormat("yyyy-MM-dd").parse(end.minusMonths(1).toString());
            String[] moumths = {"января", "февраля", "марта", "апреля", "мая", "июня",
                    "июля", "августа", "сентября", "октября", "ноября", "декабря"};
            mounth = moumths[Integer.valueOf(subStr[1]) - 1];
            god = subStr[0].substring(2, 4);
            day = subStr[2];
            lastDay = subStr[2] + "." + subStr[1] + "." + subStr[0];

        } catch (Exception e) {
        }

        InputStream in = null;
        try {

            List<Otchmarkandkonv> otchmarkandkonvs = otchmarkandkonvService.findAllD(date1, date2);
            double r1 = 0;
            double r2 = 0;
            double r3 = 0;
            double s1 = 0;
            double s2 = 0;
            double s3 = 0;
            double s4 = 0;
            for (Otchmarkandkonv otchmarkandkonv : otchmarkandkonvs) {
                boolean b = prihodService.findbyDatId(date1, date2, otchmarkandkonv.getPrihod().getId());
                if (b) {
                    s1 += otchmarkandkonv.getPrihod().getPrice() * (otchmarkandkonv.getOstatok() + otchmarkandkonv.getReestr1());
                } else {
                    s2 += otchmarkandkonv.getPrihod().getPrice() * (otchmarkandkonv.getOstatok() + otchmarkandkonv.getReestr1());
                }
                if (otchmarkandkonv.getPrihod().getSpravkonv().isD()) {
                    r2 += otchmarkandkonv.getPrihod().getPrice() * (otchmarkandkonv.getReestr1());
                } else {
                    r1 += otchmarkandkonv.getPrihod().getPrice() * (otchmarkandkonv.getReestr1());
                }
                s3 += otchmarkandkonv.getPrihod().getPrice() * (otchmarkandkonv.getReestr1());
                s4 += otchmarkandkonv.getOstatok() * otchmarkandkonv.getPrihod().getPrice();
            }

            //марки!!!!!!!!!!!!
            Otchmark otchmark = otchmarkService.findAllD(date1, date2).get(0);
            List<Prihodmarki> prihodmarkis = prihodmarkiService.findAllDat(date1, date2);
            Double prihodm = prihodmarkis.size() != 0 ? prihodmarkis.get(0).getPrice() : 0D;
            s1 += otchmark.getOstatok() + otchmark.getRashod1() + otchmark.getRashod2() - prihodm;
            s2 += prihodm;
            r3 += otchmark.getRashod1() + otchmark.getRashod2();
            s3 += otchmark.getRashod1() + otchmark.getRashod2();
            s4 += otchmark.getOstatok();

            Shablon shablon = shablonService.findById(5l);

            InputStream inputStream = new ByteArrayInputStream(shablon.getDokument());
            // формируем из файла экземпляр HSSFWorkbook
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            // выбираем первый лист для обработки
            // нумерация начинается с 0
            XSSFSheet sheet = workbook.getSheet("list1");
            XSSFRow my_row = sheet.getRow(23);
            XSSFCell myCell = my_row.getCell(23);
            myCell.setCellValue(okrug(s1));//остаток

            my_row = sheet.getRow(26);
            myCell = my_row.getCell(23);
            myCell.setCellValue(okrug(s2));//приход

            my_row = sheet.getRow(32);
            myCell = my_row.getCell(23);
            myCell.setCellValue(okrug(s3));//расход

            my_row = sheet.getRow(33);
            myCell = my_row.getCell(23);
            myCell.setCellValue(okrug(s4));//расход

            my_row = sheet.getRow(11);
            myCell = my_row.getCell(31);
            myCell.setCellValue(day);//дни

            my_row = sheet.getRow(11);
            myCell = my_row.getCell(36);
            myCell.setCellValue(mounth);//месяц

            my_row = sheet.getRow(11);
            myCell = my_row.getCell(54);
            myCell.setCellValue(god);//месяц

            my_row = sheet.getRow(7);
            myCell = my_row.getCell(58);
            myCell.setCellValue(day);//дни

            my_row = sheet.getRow(11);
            myCell = my_row.getCell(92);
            myCell.setCellValue(lastDay);//последний день

            my_row = sheet.getRow(7);
            myCell = my_row.getCell(63);
            myCell.setCellValue(mounth);//месяц

            my_row = sheet.getRow(7);
            myCell = my_row.getCell(81);
            myCell.setCellValue(god);//месяц

            sheet = workbook.getSheet("list2");
            my_row = sheet.getRow(5);
            myCell = my_row.getCell(48);
            if (myCell == null) myCell = my_row.createCell(48);
            myCell.setCellValue(okrug(r1));//Конверт с литерой А

            my_row = sheet.getRow(6);
            myCell = my_row.getCell(48);
            if (myCell == null) myCell = my_row.createCell(48);
            myCell.setCellValue(okrug(r2));//Конверт с литерой Д

            my_row = sheet.getRow(7);
            myCell = my_row.getCell(48);
            if (myCell == null) myCell = my_row.createCell(48);
            myCell.setCellValue(okrug(r3));//Марки

            my_row = sheet.getRow(25);
            myCell = my_row.getCell(48);
            if (myCell == null) myCell = my_row.createCell(48);
            myCell.setCellValue(okrug(s3));//Израсходовано, всего

            ByteArrayOutputStream b = new ByteArrayOutputStream();
            workbook.write(b);

            resp.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", "avans.xlsx");
            resp.setHeader(headerKey, headerValue);
            resp.setContentLength(b.size());
            resp.getOutputStream().write(b.toByteArray());

            in = new ByteArrayInputStream(b.toByteArray());
        } catch (Exception e) {
        }

        return IOUtils.toByteArray(in);
    }


    @GetMapping("/history")
    public String history(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(new Date(), user.getLogin(), "history param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        Date date1 = new Date();
        Date date2 = new Date();
        if (!(dat1.equals("") && dat2.equals(""))) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date1 = dateyyyyMMdd(dat1);
            date2 = dateyyyyMMdd(dat2);
            Long datenow = date2.getTime() + 86340000l; //23часа 59минут
            date2 = new Date(datenow);
        } else {
            LocalDateTime first = LocalDateTime.now().withDayOfMonth(1);
            LocalDateTime last = first.plusMonths(1);
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            try {
                date1 = new SimpleDateFormat("dd.MM.yyyy").parse(first.format(formatter));
                date2 = new SimpleDateFormat("dd.MM.yyyy").parse(last.format(formatter));
                Long datenow = date2.getTime() - 60000l; //23часа 59минут
                date2 = new Date(datenow);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        model.addAttribute("date1", dateFormat1.format(date1));
        model.addAttribute("date2", dateFormat1.format(date2));

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd.MM.yyyy");
        model.addAttribute("dat1", dateFormat2.format(date1));
        model.addAttribute("dat2", dateFormat2.format(date2));

        model.addAttribute("user", user);
        return "history";
    }


    Date dateddMMyyyy(String dat) {
        Date date = new Date();
        DateFormat format = null;
        try{
            format = new SimpleDateFormat("dd.MM.yyyy");
        }catch (Exception e){
            return dateyyyyMMdd(dat);
        }
        try {
            date = format.parse(dat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    Date dateyyyyMMdd(String dat) {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(dat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /*String okrug(Float f) {
        return f != null ? new DecimalFormat("#0.00").format(f) : null;
    }*/
    String okrug(Double f) {
        return f != null ? new DecimalFormat("#0.00").format(f) : null;
    }


//////////////////////////////////Functional-1


}