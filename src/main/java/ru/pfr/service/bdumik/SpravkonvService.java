package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Spravkonv;
import ru.pfr.repo.umikbd.SpravkonvRepository;

import java.util.List;

@Service
public class SpravkonvService {

    @Autowired
    SpravkonvRepository spravkonvRepository;

    public Spravkonv findById(Long id) {
        return spravkonvRepository.findById(id).get();
    }

    public List<Spravkonv> findAll() {
        return spravkonvRepository.findAll();
    }

    @Transactional
    public void save(Spravkonv spravkonv) {
        spravkonvRepository.save(spravkonv);
    }

    @Transactional
    public void delete(Long id) {
        spravkonvRepository.deleteById(id);
    }
}
