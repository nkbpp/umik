package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pfr.model.umikbd.Reestr1Itog;
import ru.pfr.model.umikbd.Reestr1Viev;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Reestr1VievService {

    //@Autowired
    //private Reestr1VievRepository reestr1VievRepository;

    @Autowired
    private PEDdeloproizvodstvoService peDdeloproizvodstvoService;

    @Autowired
    private BolgariaService bolgariaService;

    @Autowired
    private InoeService inoeService;

    @Autowired
    private PEDobragrajService peDobragrajService;

    /**
     * Получить все записи. из deloproizvodstvo и bolgaria
     *
     * @return Список всех записей.
     */
    public List<Reestr1Viev> findAll() {
        List<Reestr1Viev> reestr1Vievs = peDdeloproizvodstvoService.findAll().stream()
                .map(Reestr1Viev::new)
                .collect(Collectors.toList());
        reestr1Vievs.addAll(bolgariaService.findAll().stream()
                .map(Reestr1Viev::new)
                .collect(Collectors.toList()));
        return reestr1Vievs.stream().sorted((reestr1Viev, t1) ->
                        t1.getReg_date().compareTo(reestr1Viev.getReg_date()))
                .collect(Collectors.toList());
    }

    protected List<Reestr1Viev> findInoeAndObragrajByRegDateBetween(LocalDateTime d1, LocalDateTime d2) {
        List<Reestr1Viev> reestr1Vievs = peDobragrajService.findAllByRegDateBetween(d1, d2).stream()
                .map(Reestr1Viev::new)
                .collect(Collectors.toList());

        reestr1Vievs.addAll(inoeService.findAllByRegDateBetween(d1, d2).stream()
                .map(Reestr1Viev::new)
                .collect(Collectors.toList()));
        return reestr1Vievs;
    }

    protected List<Reestr1Viev> findDeloproizvodstvoAndBolgariaByRegDateBetween(LocalDateTime d1, LocalDateTime d2) {
        List<Reestr1Viev> reestr1Vievs = peDdeloproizvodstvoService.findAllByRegDateBetween(d1, d2).stream()
                .map(Reestr1Viev::new)
                .collect(Collectors.toList());

        reestr1Vievs.addAll(bolgariaService.findAllByRegDateBetween(d1, d2).stream()
                .map(Reestr1Viev::new)
                .collect(Collectors.toList()));
        return reestr1Vievs;
    }

    protected List<Reestr1Viev> findAllByRegDateBetween(LocalDateTime d1, LocalDateTime d2) {
        List<Reestr1Viev> reestr1Vievs = findDeloproizvodstvoAndBolgariaByRegDateBetween(d1, d2);
        reestr1Vievs.addAll(findInoeAndObragrajByRegDateBetween(d1, d2));
        return reestr1Vievs.stream()
                .filter(reestr1Viev -> reestr1Viev.getSpravkonv().getId().equals(1L) ||
                        reestr1Viev.getSpravkonv().getId().equals(4L) ||
                        reestr1Viev.getSpravkonv().getId().equals(5L) ||
                        reestr1Viev.getSpravkonv().getId().equals(6L) ||
                        reestr1Viev.getSpravkonv().getId().equals(7L) ||
                        reestr1Viev.getSpravkonv().getId().equals(22L) ||
                        reestr1Viev.getSpravkonv().getId().equals(14L) ||
                        reestr1Viev.getSpravkonv().getId().equals(11L))
                .sorted((reestr1Viev, t1) ->
                        t1.getReg_date().compareTo(reestr1Viev.getReg_date()))
                .collect(Collectors.toList());
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

        return findAllByRegDateBetween(d1, d2);
    }

    /**
     * Найти первую запись в заданном диапазоне дат.
     *
     * @param d1 Начальная дата.
     * @param d2 Конечная дата.
     * @return Первая запись в диапазоне дат.
     */
/*    public Reestr1Viev findAllI(LocalDateTime d1, LocalDateTime d2) {

        if (d1 == null || d2 == null) {
            throw new IllegalArgumentException("Дата не может быть null");
        }

        List<Reestr1Viev> resultList = reestr1VievRepository.findAllI(d1, d2);
        if (resultList.isEmpty()) {
            throw new RuntimeException("Записи не найдены");
        }

        return resultList.get(0);
    }*/
    public Reestr1Itog findAllI(List<Reestr1Viev> reestr1s) {
        return new Reestr1Itog(
                reestr1s.stream()
                        .map(Reestr1Viev::getId_konv1)
                        .mapToInt(integer -> integer)
                        .sum(),
                reestr1s.stream()
                        .map(Reestr1Viev::getId_konv4)
                        .mapToInt(integer -> integer)
                        .sum(),
                reestr1s.stream()
                        .map(Reestr1Viev::getId_konv5)
                        .mapToInt(integer -> integer)
                        .sum(),
                reestr1s.stream()
                        .map(Reestr1Viev::getId_konv6)
                        .mapToInt(integer -> integer)
                        .sum(),
                reestr1s.stream()
                        .map(Reestr1Viev::getId_konv11)
                        .mapToInt(integer -> integer)
                        .sum(),
                reestr1s.stream()
                        .map(Reestr1Viev::getId_konv14)
                        .mapToInt(integer -> integer)
                        .sum(),
                reestr1s.stream()
                        .map(Reestr1Viev::getSum)
                        .mapToDouble(s -> s)
                        .sum()
        );
    }


}
