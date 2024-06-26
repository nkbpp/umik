package ru.pfr.repo.umikbd;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.Prihod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PrihodRepository extends JpaRepository<Prihod, Long> {
    Optional<Prihod> findById(Long l);

    List<Prihod> findAll();

    @Query(
            value = "select id, prefix, indexx, kol_vo, price, id_konv, dat " +
                    "from prihod " +
                    "where dat BETWEEN ?1 AND ?2",
            nativeQuery = true)
    List<Prihod> findAllD(LocalDateTime d1, LocalDateTime d2);

    @Query(
            value = "select id, prefix, indexx, kol_vo, price, id_konv, dat " +
                    "from prihod " +
                    "where dat BETWEEN ?1 AND ?2 and id=?3",
            nativeQuery = true)
    List<Prihod> findbyDatId(LocalDateTime d1, LocalDateTime d2, Long i);

    // Конверт D
    @Query(
            value = "select id, prefix, indexx, kol_vo, price, id_konv, dat " +
                    "from prihod " +
                    "where id_konv=3",
            nativeQuery = true)
    List<Prihod> findAllTypeD();

    // Конверт D max
    @Query(
            value = "select id, prefix, indexx, kol_vo, price, id_konv, dat " +
                    "from prihod where id = (select max(id) from prihod where id_konv=3)",
            nativeQuery = true)
    Prihod findTypeDLast();
}
