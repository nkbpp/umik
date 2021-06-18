package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Otchmarkandkonv;
import ru.pfr.repo.umikbd.OtchmarkandkonvRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OtchmarkandkonvService {

    @Autowired
    OtchmarkandkonvRepository otchmarkandkonvRepository;

    public Otchmarkandkonv findById(Long id) {
        return otchmarkandkonvRepository.findById(id).get();
    }

    public List<Otchmarkandkonv> findAll() {
        return otchmarkandkonvRepository.findAll();
    }

    @Transactional
    public void save(Otchmarkandkonv otchraschodkonv) {
        otchmarkandkonvRepository.save(otchraschodkonv);
    }

    @Transactional
    public void delete(Long id) {
        otchmarkandkonvRepository.deleteById(id);
    }

    public List<Otchmarkandkonv> findAllD(Date d1, Date d2) {
        return otchmarkandkonvRepository.findAllD(d1, d2);
    }

    public List<Otchmarkandkonv> findAllDatOnlyTypeD(Date d1, Date d2) {
        List<Otchmarkandkonv> otchmarkandkonvs = new ArrayList<>();
        for (Otchmarkandkonv o:
        otchmarkandkonvRepository.findAllD(d1, d2)) {
            if(o.getPrihod().getSpravkonv().isD() && o.getOstatok() != 0)
            otchmarkandkonvs.add(o);
        }
        return otchmarkandkonvs;
    }

    public List<Otchmarkandkonv> findAllDatOnlyType110x120(Date d1, Date d2) {
        List<Otchmarkandkonv> otchmarkandkonvs = new ArrayList<>();
        for (Otchmarkandkonv o:
                otchmarkandkonvRepository.findAllD(d1, d2)) {
            if(o.getPrihod().getSpravkonv().is110x220() && o.getOstatok() != 0)
                otchmarkandkonvs.add(o);
        }
        return otchmarkandkonvs;
    }

    @Transactional
    public void Del(Date d1, Date d2) {
        List<Otchmarkandkonv> otchraschodkonvs = otchmarkandkonvRepository.findAllD(d1, d2);
        for (Otchmarkandkonv o:
                otchraschodkonvs) {
            otchmarkandkonvRepository.delete(o);
        }
    }

    /*  public List<Otchraschodkonv> findAllDT(Date d1, Date d2, int i) {
        return otchraschodkonvRepository.findAllD(d1, d2, i);
    }*/

}
