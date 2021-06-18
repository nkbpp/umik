package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pfr.model.umikbd.Rayon;
import ru.pfr.repo.umikbd.RayonRepository;


import java.util.List;
import java.util.Optional;

@Service
public class RayonService {
    @Autowired
    private RayonRepository rayonRepository;

    public void save(Rayon rayon) {
        rayonRepository.save(rayon);
    }

    public List<Rayon> findAll() {
        return rayonRepository.findAll();
    }

    public List<Rayon> findAllUpfr(String p1, String p2) {
        return rayonRepository.findByKodNotAndKodNot(p1, p2);
    }

    public Optional<Rayon> findById(Long id) {
        return rayonRepository.findById(id);
    }

    public Optional<Rayon> findByKod(String kod) {
        return rayonRepository.findByKod(kod);
    }
}
