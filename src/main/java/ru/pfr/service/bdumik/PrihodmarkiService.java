package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.pfr.model.umikbd.Prihodmarki;
import ru.pfr.repo.umikbd.PrihodmarkiRepository;

import java.util.Date;
import java.util.List;

@Service
public class PrihodmarkiService {

    @Autowired
    PrihodmarkiRepository prihodmarkiRepository;

    public Prihodmarki findById(Long id) {
        return prihodmarkiRepository.findById(id).get();
    }

    public List<Prihodmarki> findAll() {
        return prihodmarkiRepository.findAll();
    }


    public List<Prihodmarki> findAllDat(Date d1, Date d2) {
        return prihodmarkiRepository.findAllDat(d1, d2);
    }

    @Transactional
    public void save(Prihodmarki prihod) {
        prihodmarkiRepository.save(prihod);
    }

    @Transactional
    public void delete(Long id) {
        prihodmarkiRepository.deleteById(id);
    }

}
