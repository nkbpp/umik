package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Logi;
import ru.pfr.repo.umikbd.LogiRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LogiService {
    @Autowired
    private LogiRepository logiRepository;

    /**
     * Сохраняет запись логирования в базу данных.
     *
     * @param logi объект логирования
     */
    @Transactional
    public void save(Logi logi) {
        logiRepository.save(logi);
    }

    /**
     * Очищает все записи логирования из базы данных.
     */
    @Transactional
    public void clear() {
        logiRepository.deleteAll();
    }

    /**
     * Возвращает все записи логирования.
     *
     * @return список всех записей логирования
     */
    public List<Logi> findAll() {
        return logiRepository.findAll();
    }

    /**
     * Возвращает записи логирования по заданному диапазону дат, пользователю, типу и тексту.
     *
     * @param d1   начальная дата и время
     * @param d2   конечная дата и время
     * @param user имя пользователя
     * @param l    тип записи
     * @param text текст записи
     * @return список записей логирования, соответствующих заданным параметрам
     */
    public List<Logi> findByDateBetween(LocalDateTime d1, LocalDateTime d2, String user, Long l, String text) {
        return logiRepository.findByDateParam(d1, d2, user, l, text);
    }

    /**
     * Возвращает записи логирования по пользователю, типу и тексту.
     *
     * @param user имя пользователя
     * @param l    тип записи
     * @param text текст записи
     * @return список записей логирования, соответствующих заданным параметрам
     */
    public List<Logi> findByDateBetween(String user, Long l, String text) {
        return logiRepository.findByUser(user, l, text);
    }

    /**
     * Возвращает запись логирования по имени пользователя.
     *
     * @param login имя пользователя
     * @return объект логирования
     */
    public Logi findByUser(String login) {
        Optional<Logi> logiOptional = logiRepository.findByUser(login);
        return logiOptional.orElse(null);
    }
}
