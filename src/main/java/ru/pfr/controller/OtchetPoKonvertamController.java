package ru.pfr.controller;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.pfr.configuration.UserPrincipal;
import ru.pfr.global.DateUtils;
import ru.pfr.global.MyNumbers;
import ru.pfr.model.umikbd.*;
import ru.pfr.service.bdumik.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/umik/main/otchpokon")
public class OtchetPoKonvertamController {

    @Autowired
    private Reestr1VievService reestr1VievService;

    @Autowired
    private OtchraschodkonvService otchraschodkonvService;

    @Autowired
    private PrihodService prihodService;

    @Autowired
    private ShablonService shablonService;

    @Autowired
    private LogiService logiService;

    @GetMapping
    public String otchpokon(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "otchpokon"));

        model.addAttribute("user", user);
        return "otchpokon";
    }

    @GetMapping("/form")
    public String otchpokonform(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "otchpokonform param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime date1 = LocalDate.parse(dat1, formatter).atStartOfDay();
        LocalDateTime date2 = LocalDate.parse(dat2, formatter).atTime(23, 59, 59);

        // Вычисление дат за прошлый месяц
        LocalDateTime first = date1.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = first.plusMonths(1).minusSeconds(1);

        List<Reestr1Viev> reestr1s = reestr1VievService.findAllD(date1, date2);
        model.addAttribute("reestr1s", reestr1s);
        //здесь суммы конвертов за месяц
        Reestr1Itog reestr1i = reestr1VievService.findAllI(reestr1s);
        model.addAttribute("reestr1i", reestr1i);//итого

        List<Otchraschodkonv> otchraschodkonv = otchraschodkonvService.findAllD(first, end);

        List<Otchraschodkonv> otchraschodkonv1 = new ArrayList<>();
        List<Otchraschodkonv> otchraschodkonv4 = new ArrayList<>();
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
        model.addAttribute("otchraschodkonv6", otchraschodkonv6);
        model.addAttribute("otchraschodkonv7", otchraschodkonv7);
        model.addAttribute("otchraschodkonv22", otchraschodkonv22);

        List<Prihod> prihods1 = new ArrayList<>();
        List<Prihod> prihods4 = new ArrayList<>();
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
        model.addAttribute("prihods6", prihods6);
        model.addAttribute("prihods7", prihods7);
        model.addAttribute("prihods22", prihods22);
        model.addAttribute("user", user);
        return "fragment/otchpokonfrag :: form";
    }


    @GetMapping("/link")
    @Transactional
    public String otchpokonlink(
            @RequestParam(value = "date", defaultValue = "") String date,
            @RequestParam(value = "ota4", defaultValue = "") String ota4,
            @RequestParam(value = "pa4", defaultValue = "") String pa4,
            @RequestParam(value = "otc5", defaultValue = "") String otc5,
            @RequestParam(value = "pc5", defaultValue = "") String pc5,
            @RequestParam(value = "otpk", defaultValue = "") String otpk,
            @RequestParam(value = "ppk", defaultValue = "") String ppk,
            @RequestParam(value = "otte", defaultValue = "") String otte,
            @RequestParam(value = "pte", defaultValue = "") String pte,

            @RequestParam(value = "ot110x220clear", defaultValue = "") String ot110x220clear,
            @RequestParam(value = "p110x220clear", defaultValue = "") String p110x220clear,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "otchpokonlink param=" +
                " date = " + date +
                " ota4 = " + ota4 +
                " pa4 " + pa4 +
                " otc5 = " + otc5 +
                " pc5 = " + pc5 +
                " otpk = " + otpk +
                " ppk = " + ppk +
                " otte = " + otte +
                " pte = " + pte +
                " ot110x220clear = " + ot110x220clear +
                " p110x220clear = " + p110x220clear
        ));

        // Получаем текущую дату и время
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Получаем первый день прошлого месяца
        LocalDateTime date1Begin = currentDateTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        // Получаем последний день текущего месяца
        LocalDateTime date2End = currentDateTime.withDayOfMonth(currentDateTime.getMonth().maxLength()).withHour(23).withMinute(59).withSecond(59);

        //Удаляем старое
        otchraschodkonvService.Del(date1Begin, date2End);

        String delimeter1 = "!"; // Разделитель
        String delimeter2 = ";"; // Разделитель

        String[] ota41 = ota4.split(delimeter1);
        String[] pa41 = pa4.split(delimeter1);
        String[] otc51 = otc5.split(delimeter1);
        String[] pc51 = pc5.split(delimeter1);
        String[] otpk1 = otpk.split(delimeter1);
        String[] ppk1 = ppk.split(delimeter1);

        String[] otte1 = otte.split(delimeter1);
        String[] pte1 = pte.split(delimeter1);

        String[] ot110x220clear1 = ot110x220clear.split(delimeter1);
        String[] p110x220clear1 = p110x220clear.split(delimeter1);

        if (otpk1.length > 0 && !otpk1[0].isEmpty()) {
            for (String ot :
                    otpk1) {
                String[] sm = ot.split(delimeter2);
                Otchraschodkonv otchraschodkonv = otchraschodkonvService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        otchraschodkonv.getPrihod(),
                        currentDateTime,
                        Integer.valueOf(sm[1]),

                        otchraschodkonv.getOstatok() - Integer.parseInt(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (ppk1.length > 0 && !ppk1[0].isEmpty()) {
            for (String ot :
                    ppk1) {
                String[] sm = ot.split(delimeter2);
                Prihod prihod = prihodService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        prihod,
                        currentDateTime,
                        Integer.valueOf(sm[1]),
                        prihod.getKol_vo() - Integer.parseInt(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (otc51.length > 0 && !otc51[0].isEmpty()) {
            for (String ot :
                    otc51) {
                String[] sm = ot.split(delimeter2);
                Otchraschodkonv otchraschodkonv = otchraschodkonvService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        otchraschodkonv.getPrihod(),
                        currentDateTime,
                        Integer.valueOf(sm[1]),
                        otchraschodkonv.getOstatok() - Integer.parseInt(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (pc51.length > 0 && !pc51[0].isEmpty()) {
            for (String ot :
                    pc51) {
                String[] sm = ot.split(delimeter2);
                Prihod prihod = prihodService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        prihod,
                        currentDateTime,
                        Integer.valueOf(sm[1]),
                        prihod.getKol_vo() - Integer.parseInt(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (ota41.length > 0 && !ota41[0].isEmpty()) {
            for (String ot :
                    ota41) {
                String[] sm = ot.split(delimeter2);
                Otchraschodkonv otchraschodkonv = otchraschodkonvService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        otchraschodkonv.getPrihod(),
                        currentDateTime,
                        Integer.valueOf(sm[1]),
                        otchraschodkonv.getOstatok() - Integer.parseInt(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (pa41.length > 0 && !pa41[0].isEmpty()) {
            for (String ot :
                    pa41) {
                String[] sm = ot.split(delimeter2);
                Prihod prihod = prihodService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        prihod,
                        currentDateTime,
                        Integer.valueOf(sm[1]),
                        prihod.getKol_vo() - Integer.parseInt(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (otte1.length > 0 && !otte1[0].isEmpty()) {
            for (String ot :
                    otte1) {
                String[] sm = ot.split(delimeter2);
                Otchraschodkonv otchraschodkonv = otchraschodkonvService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        otchraschodkonv.getPrihod(),
                        currentDateTime,
                        Integer.valueOf(sm[1]),

                        otchraschodkonv.getOstatok() - Integer.parseInt(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (pte1.length > 0 && !pte1[0].isEmpty()) {
            for (String ot :
                    pte1) {
                String[] sm = ot.split(delimeter2);
                Prihod prihod = prihodService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        prihod,
                        currentDateTime,
                        Integer.valueOf(sm[1]),
                        prihod.getKol_vo() - Integer.parseInt(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (ot110x220clear1.length > 0 && !ot110x220clear1[0].isEmpty()) {
            for (String ot :
                    ot110x220clear1) {
                String[] sm = ot.split(delimeter2);
                Otchraschodkonv otchraschodkonv = otchraschodkonvService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        otchraschodkonv.getPrihod(),
                        currentDateTime,
                        Integer.valueOf(sm[1]),
                        otchraschodkonv.getOstatok() - Integer.parseInt(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        if (p110x220clear1.length > 0 && !p110x220clear1[0].isEmpty()) {
            for (String ot :
                    p110x220clear1) {
                String[] sm = ot.split(delimeter2);
                Prihod prihod = prihodService.findById(Long.valueOf(sm[0]));
                Otchraschodkonv otchraschodkonvNEW = new Otchraschodkonv(
                        prihod,
                        currentDateTime,
                        Integer.valueOf(sm[1]),
                        prihod.getKol_vo() - Integer.parseInt(sm[1])
                );
                otchraschodkonvService.save(otchraschodkonvNEW);
            }
        }

        model.addAttribute("date1", DateUtils.formatIsoToString(date1Begin));
        model.addAttribute("date2", DateUtils.formatIsoToString(date2End));

        model.addAttribute("user", user);
        return "fragment/otchpokonfrag :: link"; //TODO дошли до сюда
    }

    @GetMapping("/pechatdocx")
    public @ResponseBody
    byte[] otchpokonpechatdocx(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            HttpServletResponse resp
    ) throws IOException {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "otchpokonpechatdocx param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        LocalDateTime date1 = DateUtils.parseIsoToDate(dat1);
        LocalDateTime date2 = DateUtils.parseIsoToDate(dat2);

        List<Otchraschodkonv> otchraschodkonvs = otchraschodkonvService.findAllD(date1, date2);

        InputStream in = null;
        try {
            Shablon shablon = shablonService.findById(3L);

            InputStream inputStream = new ByteArrayInputStream(shablon.getDokument());

            XWPFDocument docxFile;
            docxFile = new XWPFDocument(inputStream);
            // открываем файл и считываем его содержимое в объект XWPFDocument

            for (XWPFParagraph p : docxFile.getParagraphs()) {
                List<XWPFRun> runs = p.getRuns();
                if (runs != null) {
                    for (XWPFRun r : runs) {
                        String text = r.getText(0);
                        if (text != null && text.contains("$")) {
                            text = text.replace("$1", DateUtils.formatToString(date1));
                            text = text.replace("$2", DateUtils.formatToString(date2));
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

                            st = MyNumbers.okrug(otchraschodkonv.getPrihod().getPrice());
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
                                st = MyNumbers.okrug(otchraschodkonv.getPrihod().getPrice() * bilo);
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
                                st = MyNumbers.okrug(otchraschodkonv.getPrihod().getPrice() * bilo);
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
                                st = MyNumbers.okrug(otchraschodkonv.getPrihod().getPrice() * (otchraschodkonv.getRashod()));
                                s3 += otchraschodkonv.getPrihod().getPrice() * (otchraschodkonv.getRashod());
                            } else
                                st = "";
                            break;
                        case 8:
                            st = String.valueOf(bilo - otchraschodkonv.getRashod());
                            break;
                        default:

                            st = MyNumbers.okrug(
                                    (bilo - otchraschodkonv.getRashod()) * otchraschodkonv.getPrihod().getPrice());
                            s4 += (bilo - otchraschodkonv.getRashod()) * otchraschodkonv.getPrihod().getPrice();

                            break;
                    }

                    run.setText(st);

                    if (i < tableRowTwo.getTableICells().size()) {
                        tableRowTwo.getCell(i).setParagraph(paragraph);
                    } else tableRowTwo.createCell().setParagraph(paragraph);
                }

                if (otchraschodkonv.getReestr1() != 0) {
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
                        st = MyNumbers.okrug(s1);
                        break;
                    case 5:
                        st = MyNumbers.okrug(s2);
                        break;
                    case 6:
                        st = String.valueOf(ras);
                        break;
                    case 7:
                        st = MyNumbers.okrug(s3);
                        break;
                    case 9:
                        st = MyNumbers.okrug(s4);
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
            logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "ERROR otchpokonpechatdocx param=" +
                    " date1 = " + dat1 +
                    " date2 = " + dat2
            ));
        }

        return IOUtils.toByteArray(in);
    }

}
