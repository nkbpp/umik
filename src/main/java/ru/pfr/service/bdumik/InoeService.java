package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Inoe;
import ru.pfr.repo.umikbd.InoeRepository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class InoeService {

    @Autowired
    private InoeRepository inoeRepository;

    public Inoe findById(Long id) {
        return inoeRepository.findById(id).get();
    }

    public List<Inoe> findAll() {
        return inoeRepository.findAll();
    }


    public List<Inoe> getAllRecordsForCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        return inoeRepository.findAllByRegDateBetween(startOfMonth, endOfMonth);
    }

    @Transactional
    public void save(Inoe bolgaria) {
        inoeRepository.save(bolgaria);
    }

    @Transactional
    public void delete(Long id) {
        inoeRepository.deleteById(id);
    }

    public List<Inoe> findAllByRegDateBetween(LocalDateTime d1, LocalDateTime d2) {
        return inoeRepository.findAllByRegDateBetween(d1, d2);
    }
}
