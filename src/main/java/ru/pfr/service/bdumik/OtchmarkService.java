package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Otchmark;
import ru.pfr.repo.umikbd.OtchmarkRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OtchmarkService {

    @Autowired
    private OtchmarkRepository otchmarkRepository;

    public Otchmark findById(Long id) {
        return otchmarkRepository.findById(id).orElse(null);
    }

    public List<Otchmark> findAll() {
        return otchmarkRepository.findAll();
    }

    @Transactional
    public void save(Otchmark otchmark) {
        otchmarkRepository.save(otchmark);
    }

    @Transactional
    public void delete(Long id) {
        otchmarkRepository.deleteById(id);
    }

    public List<Otchmark> findAllD(LocalDateTime d1, LocalDateTime d2) {
        return otchmarkRepository.findAllDat(d1, d2);
    }

    @Transactional
    public void Del(LocalDateTime d1, LocalDateTime d2) {
        List<Otchmark> otchmarks = otchmarkRepository.findAllDat(d1, d2);
        for (Otchmark o : otchmarks) {
            otchmarkRepository.delete(o);
        }
    }

}
