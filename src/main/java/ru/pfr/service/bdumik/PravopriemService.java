package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Pravopriem;
import ru.pfr.repo.umikbd.PravopriemRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class PravopriemService {

    @Autowired
    PravopriemRepository pravopriemRepository;

    public Pravopriem findById(Long id) {
        return pravopriemRepository.findById(id).get();
    }

    public List<Pravopriem> findAll() {
        return pravopriemRepository.findAll();
    }

    public Long getMaxCena_Sell() {
        return pravopriemRepository.getMaxCena_Sell();
    }

    public List<Pravopriem> findAllD(Date d1, Date d2) {
        return pravopriemRepository.findAllD(d1, d2);
    }

    public List<Pravopriem> findAllTekMounth() {
        Date date1 = new Date(); //текущий месяц
        Date date2 = new Date();
        LocalDate first = LocalDate.now().withDayOfMonth(1);
        LocalDate last = first.plusMonths(1).minusDays(1);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            date1 = new SimpleDateFormat("dd.MM.yyyy").parse(first.format(formatter));
            date2 = new SimpleDateFormat("dd.MM.yyyy").parse(last.format(formatter));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return pravopriemRepository.findAllDateOrderBy(date1, date2);
    }

    @Transactional
    public void save(Pravopriem pravopriem) {
        pravopriemRepository.save(pravopriem);
    }

    @Transactional
    public void delete(Long id) {
        pravopriemRepository.deleteById(id);
    }

}
