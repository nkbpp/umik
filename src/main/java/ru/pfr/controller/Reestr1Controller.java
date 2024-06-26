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
import ru.pfr.global.DateUtils;
import ru.pfr.model.umikbd.Logi;
import ru.pfr.model.umikbd.Reestr1Viev;
import ru.pfr.model.umikbd.Shablon;
import ru.pfr.model.umikbd.User;
import ru.pfr.service.bdumik.LogiService;
import ru.pfr.service.bdumik.Reestr1VievService;
import ru.pfr.service.bdumik.ShablonService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/umik/main/reestr1")
public class Reestr1Controller {

    @Autowired
    private LogiService logiService;

    @Autowired
    private ShablonService shablonService;

    @Autowired
    private Reestr1VievService reestr1VievService;

    @GetMapping
    public String reestr1(
            @RequestParam(value = "dat1", defaultValue = "") String dat1,
            @RequestParam(value = "dat2", defaultValue = "") String dat2,
            @AuthenticationPrincipal User user,
            Model model) {

        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "reestr1 param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        LocalDateTime date1;
        LocalDateTime date2;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (!(dat1.equals("") && dat2.equals(""))) {
            // Если входные даты не пустые, преобразуем их в LocalDateTime
            date1 = LocalDate.parse(dat1, formatter).atStartOfDay();
            date2 = LocalDate.parse(dat2, formatter).atTime(23, 59, 59);
        } else {
            // Если входные даты пустые, используем текущий месяц
            date1 = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            date2 = date1.plusMonths(1).minusSeconds(1);
        }

        List<Reestr1Viev> reestr1s = reestr1VievService.findAllD(date1, date2);
        model.addAttribute("reestr1s", reestr1s);

        Reestr1Viev reestr1i = reestr1VievService.findAllI(date1, date2);

        model.addAttribute("reestr1i", reestr1i);

        model.addAttribute("date1", DateUtils.formatIsoToString(date1));
        model.addAttribute("date2", DateUtils.formatIsoToString(date2));
        model.addAttribute("user", user);
        return "reestr1";
    }

    @GetMapping("/pechatdocx")
    public @ResponseBody
    byte[] reestr1pechatdocx(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal User user,
            HttpServletResponse resp,
            Model model) throws IOException {

        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "reestr1pechatdocx param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        LocalDateTime date1;
        LocalDateTime date2;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (!(dat1.equals("") && dat2.equals(""))) {
            // Если входные даты не пустые, преобразуем их в LocalDateTime
            date1 = LocalDate.parse(dat1, formatter).atStartOfDay();
            date2 = LocalDate.parse(dat2, formatter).atTime(23, 59, 59);
        } else {
            // Если входные даты пустые, используем текущий месяц
            date1 = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            date2 = date1.plusMonths(1).minusSeconds(1);
        }

        List<Reestr1Viev> reestr1s = reestr1VievService.findAllD(date1, date2);

        InputStream in = null;
        try {
            Shablon shablon = shablonService.findById(1l);

            InputStream inputStream = new ByteArrayInputStream(shablon.getDokument());

            XWPFDocument docxFile = null;
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

            int fontSize = 11;
            XWPFParagraph paragraph;
            XWPFRun run;

            for (Reestr1Viev reestr1Viev : reestr1s) {
                XWPFTableRow tableRowTwo = T.createRow();

                String st;
                try {
                    for (int i = 0; i < 11; i++) { // i < 9
                        paragraph = document.createParagraph();
                        paragraph.setAlignment(ParagraphAlignment.CENTER);
                        run = paragraph.createRun();
                        run.setFontSize(fontSize);
                        run.setBold(false);

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
                } catch (Exception e) {
                    System.out.println(e);
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
            System.out.println(e);
        }

        return IOUtils.toByteArray(in);
    }
}
