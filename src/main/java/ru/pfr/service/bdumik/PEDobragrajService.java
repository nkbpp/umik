package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.PEDobragraj;
import ru.pfr.repo.umikbd.PEDobragrajRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class PEDobragrajService {

    @Autowired
    PEDobragrajRepository peDobragrajRepository;

    public PEDobragraj findById(Long id) {
        return peDobragrajRepository.findById(id).get();
    }

    public List<PEDobragraj> findAll() {
        return peDobragrajRepository.findAll();
    }

    public List<PEDobragraj> findAllDate(Date d1, Date d2) {
        return peDobragrajRepository.findAllDate(d1, d2);
    }

    public List<PEDobragraj> findAllTekMounth() {
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

        return peDobragrajRepository.findAllDateOrderBy(date1, date2);
    }

    @Transactional
    public void save(PEDobragraj peDdeloproizvodstvo) {
        peDobragrajRepository.save(peDdeloproizvodstvo);
    }

    @Transactional
    public void delete(Long id) {
        peDobragrajRepository.deleteById(id);
    }

}
