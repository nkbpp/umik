package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Otchmarkandkonv;
import ru.pfr.repo.umikbd.OtchmarkandkonvRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OtchmarkandkonvService {

    @Autowired
    private OtchmarkandkonvRepository otchmarkandkonvRepository;

    public Otchmarkandkonv findById(Long id) {
        return otchmarkandkonvRepository.findById(id).orElse(null);
    }

    public List<Otchmarkandkonv> findAll() {
        return otchmarkandkonvRepository.findAll();
    }

    @Transactional
    public void save(Otchmarkandkonv otchmarkandkonv) {
        otchmarkandkonvRepository.save(otchmarkandkonv);
    }

    @Transactional
    public void delete(Long id) {
        otchmarkandkonvRepository.deleteById(id);
    }

    public List<Otchmarkandkonv> findAllD(LocalDateTime d1, LocalDateTime d2) {
        return otchmarkandkonvRepository.findAllD(d1, d2);
    }

    public List<Otchmarkandkonv> findAllDatOnlyTypeD(LocalDateTime d1, LocalDateTime d2) {
        List<Otchmarkandkonv> otchmarkandkonvs = new ArrayList<>();
        for (Otchmarkandkonv o : otchmarkandkonvRepository.findAllD(d1, d2)) {
            if (o.getPrihod().getSpravkonv().isD() && o.getOstatok() != 0)
                otchmarkandkonvs.add(o);
        }
        return otchmarkandkonvs;
    }

    public List<Otchmarkandkonv> findAllDatOnlyType110x120(LocalDateTime d1, LocalDateTime d2) {
        List<Otchmarkandkonv> otchmarkandkonvs = new ArrayList<>();
        for (Otchmarkandkonv o : otchmarkandkonvRepository.findAllD(d1, d2)) {
            if (o.getPrihod().getSpravkonv().is110x220() && o.getOstatok() != 0)
                otchmarkandkonvs.add(o);
        }
        return otchmarkandkonvs;
    }

    @Transactional
    public void Del(LocalDateTime d1, LocalDateTime d2) {
        List<Otchmarkandkonv> otchmarkandkonvs = otchmarkandkonvRepository.findAllD(d1, d2);
        for (Otchmarkandkonv o : otchmarkandkonvs) {
            otchmarkandkonvRepository.delete(o);
        }
    }
}
