package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Rayon;
import ru.pfr.model.umikbd.Shablon;
import ru.pfr.repo.umikbd.RayonRepository;
import ru.pfr.repo.umikbd.ShablonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShablonService {
    @Autowired
    ShablonRepository shablonRepository;

    @Transactional
    public void save(Shablon dokument) {
        shablonRepository.save(dokument);
    }

    public List<Shablon> findAll() {
        return shablonRepository.findAll();
    }

    public Shablon findById(Long id) {
        return shablonRepository.findById(id).get();
    }

}
