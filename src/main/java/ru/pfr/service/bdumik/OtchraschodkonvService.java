package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Otchraschodkonv;
import ru.pfr.repo.umikbd.OtchraschodkonvRepository;

import java.util.Date;
import java.util.List;

@Service
public class OtchraschodkonvService {

    @Autowired
    OtchraschodkonvRepository otchraschodkonvRepository;

    public Otchraschodkonv findById(Long id) {
        return otchraschodkonvRepository.findById(id).get();
    }

    public List<Otchraschodkonv> findAll() {
        return otchraschodkonvRepository.findAll();
    }

    @Transactional
    public void save(Otchraschodkonv otchraschodkonv) {
        otchraschodkonvRepository.save(otchraschodkonv);
    }

    @Transactional
    public void delete(Long id) {
        otchraschodkonvRepository.deleteById(id);
    }

    public List<Otchraschodkonv> findAllD(Date d1, Date d2) {
        return otchraschodkonvRepository.findAllD(d1, d2);
    }

    @Transactional
    public void Del(Date d1, Date d2) {
        List<Otchraschodkonv> otchraschodkonvs = otchraschodkonvRepository.findAllD(d1, d2);
        for (Otchraschodkonv o:
                otchraschodkonvs) {
            otchraschodkonvRepository.delete(o);
        }
    }

/*    public List<Otchraschodkonv> findAllDT(Date d1, Date d2, int i) {
        return otchraschodkonvRepository.findAllD(d1, d2, i);
    }*/

}
