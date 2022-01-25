package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.PEDdeloproizvodstvo;
import ru.pfr.repo.umikbd.PEDdeloproizvodstvoRepository;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class PEDdeloproizvodstvoService {

    @Autowired
    PEDdeloproizvodstvoRepository peDdeloproizvodstvoRepository;

    public PEDdeloproizvodstvo findById(Long id) {
        return peDdeloproizvodstvoRepository.findById(id).get();
    }

    public List<PEDdeloproizvodstvo> findAll() {
        return peDdeloproizvodstvoRepository.findAll();
    }

    public Long findNameId(String name) {
        List<PEDdeloproizvodstvo> peDdeloproizvodstvos = peDdeloproizvodstvoRepository.findByName(name);
        return peDdeloproizvodstvos.isEmpty() || peDdeloproizvodstvos.size()>0? peDdeloproizvodstvoRepository.findByName(name).get(0).getId_name():null;
    }

    public List<PEDdeloproizvodstvo> findAllDate(Date d1, Date d2) {
        return peDdeloproizvodstvoRepository.findAllDate(d1, d2);
    }

    public List<PEDdeloproizvodstvo> findAllTekMounth() {
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

        return peDdeloproizvodstvoRepository.findAllDateOrderBy(date1, date2);
    }

    @Transactional
    public void save(PEDdeloproizvodstvo peDdeloproizvodstvo) {
        peDdeloproizvodstvoRepository.save(peDdeloproizvodstvo);
    }

    @Transactional
    public void delete(Long id) {
        peDdeloproizvodstvoRepository.deleteById(id);
    }

}
