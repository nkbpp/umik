package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.PEDobragraj;
import ru.pfr.repo.umikbd.PEDobragrajRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PEDobragrajService {

    @Autowired
    PEDobragrajRepository peDobragrajRepository;

    public PEDobragraj findById(Long id) {
        return peDobragrajRepository.findById(id).orElse(null);
    }

    public List<PEDobragraj> findAll() {
        return peDobragrajRepository.findAll();
    }

    public List<PEDobragraj> findAllByRegDateBetween(LocalDateTime d1, LocalDateTime d2) {
        return peDobragrajRepository.findAllByRegDateBetween(d1, d2);
    }

    public List<PEDobragraj> findAllTekMounth() {
        LocalDateTime date1 = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime date2 = date1.plusMonths(1).minusDays(1); // Last day of current month

        return peDobragrajRepository.findAllDateOrderBy(date1, date2);
    }

    @Transactional
    public void save(PEDobragraj peDobragraj) {
        peDobragrajRepository.save(peDobragraj);
    }

    @Transactional
    public void delete(Long id) {
        peDobragrajRepository.deleteById(id);
    }

}
