package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Spravkonv;
import ru.pfr.model.umikbd.Vidanykonv;
import ru.pfr.repo.umikbd.SpravkonvRepository;
import ru.pfr.repo.umikbd.VidanykonvRepository;

import java.util.List;

@Service
public class VidanykonvService {

    @Autowired
    VidanykonvRepository vidanykonvRepository;

    public Vidanykonv findById(Long id) {
        return vidanykonvRepository.findById(id).get();
    }

    public List<Vidanykonv> findAll() {
        return vidanykonvRepository.findAll();
    }

    @Transactional
    public void save(Vidanykonv vidanykonv) {
        vidanykonvRepository.save(vidanykonv);
    }

    @Transactional
    public void delete(Long id) {
        vidanykonvRepository.deleteById(id);
    }
}
