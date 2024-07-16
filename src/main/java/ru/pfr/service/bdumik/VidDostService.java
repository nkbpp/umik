package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pfr.model.umikbd.VidDost;
import ru.pfr.repo.umikbd.VidDostRepository;

import java.util.List;

@Service
public class VidDostService {

    @Autowired
    private VidDostRepository vidDostRepository;

    public List<VidDost> findAll() {
        return vidDostRepository.findAll();
    }

}
