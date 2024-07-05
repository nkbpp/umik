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
@RequestMapping("/umik/main/otchmarkandkonv")
public class OtchetMarkiKonvertyController {

    @Autowired
    private LogiService logiService;

    @Autowired
    private Reestr1VievService reestr1VievService;

    @Autowired
    private PravopriemService pravopriemService;

    @Autowired
    private OtchmarkandkonvService otchmarkandkonvService;

    @Autowired
    private PrihodmarkiService prihodmarkiService;

    @Autowired
    private OtchmarkService otchmarkService;

    @Autowired
    private PrihodService prihodService;

    @Autowired
    private ShablonService shablonService;

    @GetMapping
    public String otchmarkandkonv(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "otchmarkandkonv"));

        model.addAttribute("user", user);
        return "otchmarkandkonv";
    }

    @GetMapping("/form")
    public String otchmarkandkonvform(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "otchmarkandkonvform param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime date1 = LocalDate.parse(dat1, formatter).atStartOfDay();
        LocalDateTime date2 = LocalDate.parse(dat2, formatter).atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59);
        LocalDateTime date1minusMonths = date1.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime date2minusMonths = date1minusMonths.plusMonths(1).minusSeconds(1);

        //здесь суммы конвертов за месяц 110x120
        Reestr1Itog reestr1i = reestr1VievService.findAllI(reestr1VievService.findAllD(date1, date2));
        model.addAttribute("reestr1i", reestr1i);

        List<Pravopriem> pravopriems = pravopriemService.findAllD(date1, date2);

        int k = 0, mzp = 0;

        for (Pravopriem r : pravopriems) {
            mzp += r.getMarki_k_zak_pis();
            k += r.getKonvert_d();
        }

        model.addAttribute("pravopriems", pravopriems);
        model.addAttribute("k", (double) k);
        model.addAttribute("r2", (double) mzp);
        model.addAttribute("r1", reestr1i.getSum());
        model.addAttribute("ob", MyNumbers.okrug((double) mzp + reestr1i.getSum()));

        List<Prihodmarki> prihodmarkis = prihodmarkiService.findAllDat(date1, date2);
        List<Otchmark> otchmark = otchmarkService.findAllD(date1minusMonths, date2minusMonths);

        model.addAttribute("ostat", MyNumbers.okrug(!otchmark.isEmpty() ? otchmark.get(0).getOstatok() : 0));
        model.addAttribute("prihodm", MyNumbers.okrug(!prihodmarkis.isEmpty() ? prihodmarkis.get(0).getPrice() : 0));

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

    @GetMapping("/link")
    @Transactional
    public String otchmarkandkonvlink(
            @RequestParam(value = "date", defaultValue = "") String date,
            @RequestParam(value = "otD", defaultValue = "") String otD,
            @RequestParam(value = "pD", defaultValue = "") String pD,
            @RequestParam(value = "ot110x220", defaultValue = "") String ot110x220,
            @RequestParam(value = "p110x220", defaultValue = "") String p110x220,
            @RequestParam(value = "reestr1m", defaultValue = "") String reestr1m,
            @RequestParam(value = "reestr2m", defaultValue = "") String reestr2m,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "otchmarkandkonvlink param=" +
                " date = " + date +
                " otD = " + otD +
                " pD " + pD +
                " ot110x220 = " + ot110x220 +
                " p110x220 = " + p110x220 +
                " reestr1m = " + reestr1m +
                " reestr2m = " + reestr2m
        ));

        LocalDateTime date1 = DateUtils.parseIsoToDate(date);

        LocalDateTime date1Begin = date1.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime date2End = date1Begin.plusMonths(1).minusSeconds(1);
        LocalDateTime date1BeginMinusMonths = date1Begin.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime date2EndMinusMonths = date1BeginMinusMonths.plusMonths(1).minusSeconds(1);

        otchmarkandkonvService.Del(date1Begin, date2End);//Удаляем старое
        otchmarkService.Del(date1Begin, date2End);//Удаляем старое

        //Работа с марками
        List<Prihodmarki> prihodmarkis = prihodmarkiService.findAllDat(date1Begin, date2End);
        Double prihodm = !prihodmarkis.isEmpty() ? prihodmarkis.get(0).getPrice() : 0D;

        List<Otchmark> otchmarks = otchmarkService.findAllD(date1BeginMinusMonths, date2EndMinusMonths);
        Double ostatm = !otchmarks.isEmpty() ? otchmarks.get(0).getOstatok() : 0;

        Otchmark otchmark = new Otchmark(
                date1,
                Double.valueOf(reestr1m),
                Double.valueOf(reestr2m),
                ostatm + prihodm - (Double.parseDouble(reestr1m) + Double.parseDouble(reestr2m))
        );
        otchmarkService.save(otchmark);

        String delimeter1 = "!"; // Разделитель
        String delimeter2 = ";"; // Разделитель

        String[] otD1 = otD.split(delimeter1);
        String[] pD1 = pD.split(delimeter1);
        String[] ot110x2201 = ot110x220.split(delimeter1);
        String[] p110x2201 = p110x220.split(delimeter1);

        if (ot110x2201.length > 0 && !ot110x2201[0].isEmpty()) {
            for (String ot :
                    ot110x2201) {
                String[] sm = ot.split(delimeter2);
                Otchmarkandkonv otchmarkandkonv = otchmarkandkonvService.findById(Long.valueOf(sm[0]));
                Otchmarkandkonv otchmarkandkonvNEW = new Otchmarkandkonv(
                        otchmarkandkonv.getPrihod(),
                        date1,
                        Integer.valueOf(sm[1]),
                        otchmarkandkonv.getOstatok() - Integer.parseInt(sm[1])
                );
                otchmarkandkonvService.save(otchmarkandkonvNEW);
            }
        }

        if (p110x2201.length > 0 && !p110x2201[0].isEmpty()) {
            for (String ot :
                    p110x2201) {
                String[] sm = ot.split(delimeter2);
                Prihod prihod = prihodService.findById(Long.valueOf(sm[0]));
                Otchmarkandkonv otchmarkandkonvNEW = new Otchmarkandkonv(
                        prihod,
                        date1,
                        Integer.valueOf(sm[1]),
                        prihod.getKol_vo() - Integer.parseInt(sm[1])
                );
                otchmarkandkonvService.save(otchmarkandkonvNEW);
            }
        }

        if (otD1.length > 0 && !otD1[0].isEmpty()) {
            for (String ot :
                    otD1) {
                String[] sm = ot.split(delimeter2);
                Otchmarkandkonv otchmarkandkonv = otchmarkandkonvService.findById(Long.valueOf(sm[0]));
                Otchmarkandkonv otchmarkandkonvNEW = new Otchmarkandkonv(
                        otchmarkandkonv.getPrihod(),
                        date1,
                        Integer.valueOf(sm[1]),
                        otchmarkandkonv.getOstatok() - Integer.parseInt(sm[1])
                );
                otchmarkandkonvService.save(otchmarkandkonvNEW);
            }
        }

        if (pD1.length > 0 && !pD1[0].isEmpty()) {
            for (String ot :
                    pD1) {
                String[] sm = ot.split(delimeter2);
                Prihod prihod = prihodService.findById(Long.valueOf(sm[0]));
                Otchmarkandkonv otchmarkandkonvNEW = new Otchmarkandkonv(
                        prihod,
                        date1,
                        Integer.valueOf(sm[1]),
                        prihod.getKol_vo() - Integer.parseInt(sm[1])
                );
                otchmarkandkonvService.save(otchmarkandkonvNEW);
            }
        }

        model.addAttribute("date1", DateUtils.formatIsoToString(date1Begin));
        model.addAttribute("date2", DateUtils.formatIsoToString(date2End));

        model.addAttribute("user", user);
        return "fragment/otchmarkandkonvfrag :: link";
    }

    @GetMapping("/pechatdocx")
    public @ResponseBody
    byte[] otchmarkandkonvpechatdocx(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            HttpServletResponse resp
    ) throws IOException {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "otchmarkandkonvpechatdocx param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        LocalDateTime date1 = DateUtils.parseIsoToDate(dat1);
        LocalDateTime date2 = DateUtils.parseIsoToDate(dat2);

        List<Otchmarkandkonv> otchmarkandkonvs = otchmarkandkonvService.findAllD(date1, date2);

        InputStream in = null;
        try {
            Shablon shablon = shablonService.findById(4L);

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
                            st = MyNumbers.okrug(otchmarkandkonv.getPrihod().getPrice());
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
                                st = MyNumbers.okrug(otchmarkandkonv.getPrihod().getPrice() * bilo);
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
                                st = MyNumbers.okrug(otchmarkandkonv.getPrihod().getPrice() * bilo);
                                s2 += otchmarkandkonv.getPrihod().getPrice() * bilo;
                            } else
                                st = "";
                            break;
                        case 6:
                            if (otchmarkandkonv.getReestr1() != 0) {
                                st = String.valueOf(otchmarkandkonv.getReestr1());
                            } else
                                st = "";
                            break;
                        case 7:
                            if (otchmarkandkonv.getReestr1() != 0) {
                                st = MyNumbers.okrug(otchmarkandkonv.getPrihod().getPrice() * (otchmarkandkonv.getReestr1()));
                                s3 += otchmarkandkonv.getPrihod().getPrice() * (otchmarkandkonv.getReestr1());
                            } else
                                st = "";
                            break;
                        case 8:
                            st = String.valueOf(bilo - otchmarkandkonv.getReestr1());
                            break;
                        default:
                            st = MyNumbers.okrug(
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
            double prihodm = !prihodmarkis.isEmpty() ? prihodmarkis.get(0).getPrice() : 0D;

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
                        st = MyNumbers.okrug(otchmark.getOstatok() + otchmark.getRashod1() + otchmark.getRashod2() - prihodm);
                        s1 += otchmark.getOstatok() + otchmark.getRashod1() + otchmark.getRashod2() - prihodm;
                        break;
                    case 5:
                        st = prihodm == 0 ? "" : MyNumbers.okrug(prihodm);
                        s2 += prihodm;
                        break;
                    case 7:
                        st = MyNumbers.okrug(otchmark.getRashod1() + otchmark.getRashod2());
                        s3 += otchmark.getRashod1() + otchmark.getRashod2();
                        break;
                    case 9:
                        st = MyNumbers.okrug(otchmark.getOstatok());
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
                        st = MyNumbers.okrug(otchmark.getRashod1());
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
                        st = MyNumbers.okrug(otchmark.getRashod2());
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
                        st = MyNumbers.okrug(s1);
                        break;
                    case 5:
                        st = MyNumbers.okrug(s2);
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
            String headerValue = String.format("attachment; filename=\"%s\"", "otm.docx");
            resp.setHeader(headerKey, headerValue);
            resp.setContentLength(b.size());
            resp.getOutputStream().write(b.toByteArray());

            in = new ByteArrayInputStream(b.toByteArray());
        } catch (Exception e) {
        }

        return IOUtils.toByteArray(in);
    }

}
