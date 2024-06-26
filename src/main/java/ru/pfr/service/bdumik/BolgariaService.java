package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Bolgaria;
import ru.pfr.repo.umikbd.BolgariaRepository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class BolgariaService {

    @Autowired
    private BolgariaRepository bolgariaRepository;

    /**
     * Найти сущность Bolgaria по ID.
     * @param id ID сущности.
     * @return сущность Bolgaria, если найдена, иначе выбрасывается RuntimeException.
     */
    public Bolgaria findById(Long id) {
        Optional<Bolgaria> bolgaria = bolgariaRepository.findById(id);
        return bolgaria.orElseThrow(() -> new RuntimeException("Запись не найдена с id: " + id));
    }

    /**
     * Найти все записи Bolgaria.
     * @return список всех записей Bolgaria.
     */
    public List<Bolgaria> findAll() {
        return bolgariaRepository.findAll();
    }

    /**
     * Найти все записи Bolgaria за текущий месяц.
     * @return список всех записей Bolgaria за текущий месяц.
     */
    public List<Bolgaria> getAllRecordsForCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        return bolgariaRepository.findAllByRegDateBetween(startOfMonth, endOfMonth);
    }

    /**
     * Сохранить сущность Bolgaria в базу данных.
     * @param bolgaria сущность Bolgaria для сохранения.
     */
    @Transactional
    public void save(Bolgaria bolgaria) {
        bolgariaRepository.save(bolgaria);
    }

    /**
     * Удалить сущность Bolgaria по ID.
     * @param id ID сущности для удаления.
     */
    @Transactional
    public void delete(Long id) {
        bolgariaRepository.deleteById(id);
    }

}
