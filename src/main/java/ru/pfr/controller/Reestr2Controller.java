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
import ru.pfr.model.umikbd.Logi;
import ru.pfr.model.umikbd.Pravopriem;
import ru.pfr.model.umikbd.Shablon;
import ru.pfr.model.umikbd.User;
import ru.pfr.service.bdumik.LogiService;
import ru.pfr.service.bdumik.PravopriemService;
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
@RequestMapping("/umik/main/reestr2")
public class Reestr2Controller {

    @Autowired
    private LogiService logiService;

    @Autowired
    private PravopriemService pravopriemService;

    @Autowired
    private ShablonService shablonService;

    @GetMapping
    public String reestr2(
            @RequestParam(value = "dat1", defaultValue = "") String dat1,
            @RequestParam(value = "dat2", defaultValue = "") String dat2,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "reestr2 param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        LocalDateTime date1;
        LocalDateTime date2;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (!(dat1.isEmpty() && dat2.isEmpty())) {
            // Если входные даты не пустые, преобразуем их в LocalDateTime
            date1 = LocalDate.parse(dat1, formatter).atStartOfDay();
            date2 = LocalDate.parse(dat2, formatter).atTime(23, 59, 59);
        } else {
            // Если входные даты пустые, используем текущий месяц
            date1 = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            date2 = date1.plusMonths(1).minusSeconds(1);
        }

        List<Pravopriem> pravopriems = pravopriemService.findAllD(date1, date2);

        double so = 0, m = 0, k = 0, mzp = 0;

        for (Pravopriem r : pravopriems) {
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
        model.addAttribute("date1", DateUtils.formatIsoToString(date1));
        model.addAttribute("date2", DateUtils.formatIsoToString(date2));
        model.addAttribute("user", user);
        return "reestr2";
    }

    @GetMapping("/pechatdocx")
    public @ResponseBody
    byte[] reestr2pechatdocx(
            @RequestParam(value = "dat1", defaultValue = "") String dat1,
            @RequestParam(value = "dat2", defaultValue = "") String dat2,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            HttpServletResponse resp,
            Model model) throws IOException {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "reestr2pechatdocx param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        LocalDateTime date1;
        LocalDateTime date2;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (!(dat1.isEmpty() && dat2.isEmpty())) {
            // Если входные даты не пустые, преобразуем их в LocalDateTime
            date1 = LocalDate.parse(dat1, formatter).atStartOfDay();
            date2 = LocalDate.parse(dat2, formatter).atTime(23, 59, 59);
        } else {
            // Если входные даты пустые, используем текущий месяц
            date1 = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            date2 = date1.plusMonths(1).minusSeconds(1);
        }

        List<Pravopriem> pravopriems = pravopriemService.findAllD(date1, date2);

        double so = 0, m = 0, k = 0, mzp = 0;

        for (Pravopriem r : pravopriems) {
            mzp += r.getMarki_k_zak_pis();
            k += r.getKonvert_d();
            m += r.get_sumk();
            so += r.get_sumob();
        }

        InputStream in = null;
        try {
            Shablon shablon = shablonService.findById(2L);

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
}
