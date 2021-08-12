package ru.pfr.repo.umikbd;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.Prihod;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PrihodRepository extends JpaRepository<Prihod, Long> {
    public Optional<Prihod> findById(Long l);
    public List<Prihod> findAll();

    @Query(
            value = "select id, prefix, indexx, kol_vo, price, id_konv, dat " +
                    "from prihod " +
                    "where dat BETWEEN ?1 AND ?2 ",
            nativeQuery = true)
    public List<Prihod> findAllD(Date d1, Date d2);

    @Query(
            value = "select id, prefix, indexx, kol_vo, price, id_konv, dat " +
                    "from prihod " +
                    "where dat BETWEEN ?1 AND ?2 and id=?3 ",
            nativeQuery = true)
    public List<Prihod> findbyDatId(Date d1, Date d2, Long i);

    //Конверт D
    @Query(
            value = "select id, prefix, indexx, kol_vo, price, id_konv, dat " +
                    "from prihod " +
                    "where id_konv=3",
            nativeQuery = true)
    public List<Prihod> findAllTypeD();

    //Конверт D max
    @Query(
            value = "select id, prefix, indexx, kol_vo, price, id_konv, dat " +
                    "from prihod where id = (select max(id) from prihod where id_konv=3) ",
            nativeQuery = true)
    public Prihod findTypeDLast();
}
