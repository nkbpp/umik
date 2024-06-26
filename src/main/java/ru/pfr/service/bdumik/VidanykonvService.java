package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pfr.model.umikbd.Vidanykonv;
import ru.pfr.repo.umikbd.VidanykonvRepository;

import java.util.List;

@Service
public class VidanykonvService {

    @Autowired
    private VidanykonvRepository vidanykonvRepository;

    public Vidanykonv findById(Long id) {
        return vidanykonvRepository.findById(id).get();
    }

    public List<Vidanykonv> findAll() {
        return vidanykonvRepository.findAll();
    }

    public void save(Vidanykonv vidanykonv) {
        vidanykonvRepository.save(vidanykonv);
    }

    public void delete(Long id) {
        vidanykonvRepository.deleteById(id);
    }
}
