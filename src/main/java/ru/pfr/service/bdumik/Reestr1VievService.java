package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Reestr1Viev;
import ru.pfr.repo.umikbd.Reestr1VievRepository;


import java.util.Date;
import java.util.List;

@Service
public class Reestr1VievService {

    @Autowired
    Reestr1VievRepository reestr1VievRepository;

    public List<Reestr1Viev> findAll() {
        return reestr1VievRepository.findAll();
    }

    public List<Reestr1Viev> findAllD(Date d1, Date d2) {
        return reestr1VievRepository.findAllD(d1, d2);
    }

    public Reestr1Viev findAllI(Date d1, Date d2) {
        return reestr1VievRepository.findAllI(d1, d2).get(0);
    }



}
