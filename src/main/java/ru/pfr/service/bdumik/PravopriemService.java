package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Pravopriem;
import ru.pfr.repo.umikbd.PravopriemRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PravopriemService {

    @Autowired
    PravopriemRepository pravopriemRepository;

    public Pravopriem findById(Long id) {
        return pravopriemRepository.findById(id).get();
    }

    public List<Pravopriem> findAll() {
        return pravopriemRepository.findAll();
    }

    public Long getMaxCena_Sell() {
        return pravopriemRepository.getMaxCena_Sell();
    }

    public List<Pravopriem> findAllD(LocalDateTime d1, LocalDateTime d2) {
        return pravopriemRepository.findAllD(d1, d2);
    }

    public List<Pravopriem> findAllTekMounth() {
        LocalDateTime date1 = LocalDate.now().withDayOfMonth(1).atStartOfDay(); // начало текущего месяца
        LocalDateTime date2 = date1.plusMonths(1).minusSeconds(1); // конец текущего месяца

        return pravopriemRepository.findAllDateOrderBy(date1, date2);
    }

    @Transactional
    public void save(Pravopriem pravopriem) {
        pravopriemRepository.save(pravopriem);
    }

    @Transactional
    public void delete(Long id) {
        pravopriemRepository.deleteById(id);
    }

}
