package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pfr.model.umikbd.Reestr1Viev;
import ru.pfr.repo.umikbd.Reestr1VievRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class Reestr1VievService {

    @Autowired
    private Reestr1VievRepository reestr1VievRepository;

    /**
     * Получить все записи.
     *
     * @return Список всех записей.
     */
    public List<Reestr1Viev> findAll() {
        return reestr1VievRepository.findAll();
    }

    /**
     * Найти все записи в заданном диапазоне дат.
     *
     * @param d1 Начальная дата.
     * @param d2 Конечная дата.
     * @return Список записей в диапазоне дат.
     */
    public List<Reestr1Viev> findAllD(LocalDateTime d1, LocalDateTime d2) {
        if (d1 == null || d2 == null) {
            throw new IllegalArgumentException("Дата не может быть null");
        }

        return reestr1VievRepository.findAllD(d1, d2);
    }

    /**
     * Найти первую запись в заданном диапазоне дат.
     *
     * @param d1 Начальная дата.
     * @param d2 Конечная дата.
     * @return Первая запись в диапазоне дат.
     */
    public Reestr1Viev findAllI(LocalDateTime d1, LocalDateTime d2) {

        if (d1 == null || d2 == null) {
            throw new IllegalArgumentException("Дата не может быть null");
        }

        List<Reestr1Viev> resultList = reestr1VievRepository.findAllI(d1, d2);
        if (resultList.isEmpty()) {
            throw new RuntimeException("Записи не найдены");
        }

        return resultList.get(0);
    }

}
