package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pfr.model.umikbd.VidDost;
import ru.pfr.repo.umikbd.VidDostRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class VidDostService {

    @Autowired
    VidDostRepository vidDostRepository;

    public VidDost findById(Long id) {
        return vidDostRepository.findById(id).get();
    }

    public List<VidDost> findAll() {
        return vidDostRepository.findAll();
    }

    @Transactional
    public void save(VidDost spravkonv) {
        vidDostRepository.save(spravkonv);
    }

    @Transactional
    public void delete(Long id) {
        vidDostRepository.deleteById(id);
    }

}
