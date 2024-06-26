package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.PEDdeloproizvodstvo;
import ru.pfr.repo.umikbd.PEDdeloproizvodstvoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PEDdeloproizvodstvoService {

    @Autowired
    PEDdeloproizvodstvoRepository peDdeloproizvodstvoRepository;

    public PEDdeloproizvodstvo findById(Long id) {
        return peDdeloproizvodstvoRepository.findById(id).orElse(null);
    }

    public List<PEDdeloproizvodstvo> findAll() {
        return peDdeloproizvodstvoRepository.findAll();
    }

    public Long findNameId(String name) {
        List<PEDdeloproizvodstvo> peDdeloproizvodstvos = peDdeloproizvodstvoRepository.findByName(name);
        return peDdeloproizvodstvos.isEmpty() || peDdeloproizvodstvos.size() > 0 ? peDdeloproizvodstvoRepository.findByName(name).get(0).getId_name() : null;
    }

    public List<PEDdeloproizvodstvo> findAllDate(LocalDateTime d1, LocalDateTime d2) {
        return peDdeloproizvodstvoRepository.findAllDate(d1, d2);
    }

    public List<PEDdeloproizvodstvo> findAllTekMounth() {
        LocalDateTime date1 = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime date2 = date1.plusMonths(1).minusDays(1); // Last day of current month

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
