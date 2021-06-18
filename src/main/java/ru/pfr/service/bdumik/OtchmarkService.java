package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Otchmark;
import ru.pfr.repo.umikbd.OtchmarkRepository;

import java.util.Date;
import java.util.List;

@Service
public class OtchmarkService {

    @Autowired
    OtchmarkRepository otchmarkRepository;

    public Otchmark findById(Long id) {
        return otchmarkRepository.findById(id).get();
    }

    public List<Otchmark> findAll() {
        return otchmarkRepository.findAll();
    }

    @Transactional
    public void save(Otchmark otchraschodkonv) {
        otchmarkRepository.save(otchraschodkonv);
    }

    @Transactional
    public void delete(Long id) {
        otchmarkRepository.deleteById(id);
    }

    public List<Otchmark> findAllD(Date d1, Date d2) {
        return otchmarkRepository.findAllDat(d1, d2);
    }


    @Transactional
    public void Del(Date d1, Date d2) {
        List<Otchmark> otchraschodkonvs = otchmarkRepository.findAllDat(d1, d2);
        for (Otchmark o:
                otchraschodkonvs) {
            otchmarkRepository.delete(o);
        }
    }

}
