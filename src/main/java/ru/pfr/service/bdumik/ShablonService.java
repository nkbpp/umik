package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Shablon;
import ru.pfr.repo.umikbd.ShablonRepository;

import java.util.List;

@Service
public class ShablonService {
    @Autowired
    private ShablonRepository shablonRepository;

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
