package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Prihod;
import ru.pfr.repo.umikbd.PrihodRepository;

import java.util.Date;
import java.util.List;

@Service
public class PrihodService {

    @Autowired
    PrihodRepository prihodRepository;

    public Prihod findById(Long id) {
        return prihodRepository.findById(id).get();
    }

    public List<Prihod> findAll() {
        return prihodRepository.findAll();
    }


    public List<Prihod> findAllTypeD() {
        return prihodRepository.findAllTypeD();
    }

    public Prihod findTypeDLast() {
        return prihodRepository.findTypeDLast();
    }

    public List<Prihod> findAllD(Date d1, Date d2) {
        return prihodRepository.findAllD(d1, d2);
    }

    @Transactional
    public void save(Prihod prihod) {
        prihodRepository.save(prihod);
    }

    public boolean findbyDatId(Date d1, Date d2, Long i) {
        List<Prihod> p = prihodRepository.findbyDatId(d1, d2, i);
        return p.isEmpty();
    }

    @Transactional
    public void delete(Long id) {
        prihodRepository.deleteById(id);
    }

}
