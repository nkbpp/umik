package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Prihodmarki;
import ru.pfr.repo.umikbd.PrihodmarkiRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrihodmarkiService {

    @Autowired
    private PrihodmarkiRepository prihodmarkiRepository;

    public Prihodmarki findById(Long id) {
        return prihodmarkiRepository.findById(id).orElse(null);
    }

    public List<Prihodmarki> findAll() {
        return prihodmarkiRepository.findAll().stream().sorted((prihodmarki, t1) ->
                t1.getDate().compareTo(prihodmarki.getDate())).collect(Collectors.toList());
    }

    public List<Prihodmarki> findAllDat(LocalDateTime d1, LocalDateTime d2) {
        return prihodmarkiRepository.findAllDat(d1, d2);
    }

    @Transactional
    public void save(Prihodmarki prihod) {
        prihodmarkiRepository.save(prihod);
    }

    @Transactional
    public void delete(Long id) {
        prihodmarkiRepository.deleteById(id);
    }

}
