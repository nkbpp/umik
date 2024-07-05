package ru.pfr.controller;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/umik/main")
public class AvansController {

    @Autowired
    private ShablonService shablonService;

    @Autowired
    private PrihodService prihodService;

    @Autowired
    private OtchmarkandkonvService otchmarkandkonvService;

    @Autowired
    private PrihodmarkiService prihodmarkiService;

    @Autowired
    private OtchmarkService otchmarkService;

    @Autowired
    private LogiService logiService;

    @GetMapping("/avans")
    public String avans(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "avans"));

        model.addAttribute("user", user);
        return "avans";
    }

    @GetMapping("/avans/link")
    public String avanslink(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model) {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "Ссылка на авансовый отчет avanslink param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));

        LocalDateTime date1 = DateUtils.parseIsoToDate(dat1);
        LocalDateTime date2 = DateUtils.parseIsoToDate(dat1);

        model.addAttribute("date1", DateUtils.formatIsoToString(date1));
        model.addAttribute("date2", DateUtils.formatIsoToString(date2));

        model.addAttribute("date11", DateUtils.formatToString(date1));
        model.addAttribute("date22", DateUtils.formatToString(date2));


        model.addAttribute("user", user);
        return "fragment/avansfrag :: link";
    }

    @GetMapping("/avans/pechatdocx")
    public @ResponseBody
    byte[] avanspechatdocx(
            @RequestParam(value = "date1", defaultValue = "") String dat1,
            @RequestParam(value = "date2", defaultValue = "") String dat2,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            HttpServletResponse resp,
            Model model) throws IOException {
        User user = userPrincipal.getUser();
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(), "Печать отчета avanspechatdocx param=" +
                " date1 = " + dat1 +
                " date2 = " + dat2
        ));


        LocalDateTime date1 = DateUtils.parseIsoToDate(dat1);
        LocalDateTime date2 = DateUtils.parseIsoToDate(dat2);
        String mounth;
        String god;
        String day;
        String lastDay;

        String[] subStr;
        String delimeter = "-"; // Разделитель
        subStr = dat2.split(delimeter);
        String[] moumths = {"января", "февраля", "марта", "апреля", "мая", "июня",
                "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        mounth = moumths[Integer.parseInt(subStr[1]) - 1];
        god = subStr[0].substring(2, 4);
        day = subStr[2];
        lastDay = subStr[2] + "." + subStr[1] + "." + subStr[0];


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
            double prihodm = !prihodmarkis.isEmpty() ? prihodmarkis.get(0).getPrice() : 0D;
            s1 += otchmark.getOstatok() + otchmark.getRashod1() + otchmark.getRashod2() - prihodm;
            s2 += prihodm;
            r3 += otchmark.getRashod1() + otchmark.getRashod2();
            s3 += otchmark.getRashod1() + otchmark.getRashod2();
            s4 += otchmark.getOstatok();

            Shablon shablon = shablonService.findById(5L);

            InputStream inputStream = new ByteArrayInputStream(shablon.getDokument());
            // формируем из файла экземпляр HSSFWorkbook
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            // выбираем первый лист для обработки
            // нумерация начинается с 0
            XSSFSheet sheet = workbook.getSheet("list1");
            XSSFRow my_row = sheet.getRow(23);
            XSSFCell myCell = my_row.getCell(23);
            myCell.setCellValue(MyNumbers.okrug(s1));//остаток

            my_row = sheet.getRow(26);
            myCell = my_row.getCell(23);
            myCell.setCellValue(MyNumbers.okrug(s2));//приход

            my_row = sheet.getRow(32);
            myCell = my_row.getCell(23);
            myCell.setCellValue(MyNumbers.okrug(s3));//расход

            my_row = sheet.getRow(33);
            myCell = my_row.getCell(23);
            myCell.setCellValue(MyNumbers.okrug(s4));//расход

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
            myCell.setCellValue(MyNumbers.okrug(r1));//Конверт с литерой А

            my_row = sheet.getRow(6);
            myCell = my_row.getCell(48);
            if (myCell == null) myCell = my_row.createCell(48);
            myCell.setCellValue(MyNumbers.okrug(r2));//Конверт с литерой Д

            my_row = sheet.getRow(7);
            myCell = my_row.getCell(48);
            if (myCell == null) myCell = my_row.createCell(48);
            myCell.setCellValue(MyNumbers.okrug(r3));//Марки

            my_row = sheet.getRow(25);
            myCell = my_row.getCell(48);
            if (myCell == null) myCell = my_row.createCell(48);
            myCell.setCellValue(MyNumbers.okrug(s3));//Израсходовано, всего

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
}
