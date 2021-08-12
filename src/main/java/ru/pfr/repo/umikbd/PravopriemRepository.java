package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.Pravopriem;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PravopriemRepository extends JpaRepository<Pravopriem, Long> {

    public Optional<Pravopriem> findById(Long l);

    public List<Pravopriem> findAll();

    @Query(
            value = "select * " +
                    "from pravopriem " +
                    "where date BETWEEN ?1 AND ?2 order by date",
            nativeQuery = true)
    public List<Pravopriem> findAllD(Date d1, Date d2);

    @Query(
            value = "select * " +
                    "from pravopriem " +
                    "where date BETWEEN ?1 AND ?2 order by id desc",
            nativeQuery = true)
    public List<Pravopriem> findAllDateOrderBy(Date d1, Date d2);

    @Query(
            value = "select a.cena_sell from (select max(id), cena_sell " +
                    "from pravopriem where cena_sell>0) a",
            nativeQuery = true)
    public Long getMaxCena_Sell();
}
