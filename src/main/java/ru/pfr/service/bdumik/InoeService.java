package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Inoe;
import ru.pfr.repo.umikbd.InoeRepository;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class InoeService {

    @Autowired
    InoeRepository inoeRepository;

    public Inoe findById(Long id) {
        return inoeRepository.findById(id).get();
    }

    public List<Inoe> findAll() {
        return inoeRepository.findAll();
    }

    public List<Inoe> findAllTekMounth() {
        Date date1 = new Date(); //текущий месяц
        Date date2 = new Date();
        LocalDate first = LocalDate.now().withDayOfMonth(1);
        LocalDate last = first.plusMonths(1);
        //LocalDate last = first.plusMonths(1).minusDays(1);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            date1 = new SimpleDateFormat("dd.MM.yyyy").parse(first.format(formatter));
            date2 = new SimpleDateFormat("dd.MM.yyyy").parse(last.format(formatter));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return inoeRepository.findAllDateOrderBy(date1, date2);
    }

    @Transactional
    public void save(Inoe bolgaria) {
        inoeRepository.save(bolgaria);
    }

    @Transactional
    public void delete(Long id) {
        inoeRepository.deleteById(id);
    }

    public List<Inoe> findAllD(Date d1, Date d2) {
        return inoeRepository.findAllD(d1, d2);
    }

}
