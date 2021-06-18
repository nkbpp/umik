package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.Bolgaria;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BolgariaRepository extends JpaRepository<Bolgaria, Long> {
    public Optional<Bolgaria> findById(Long l);
    public List<Bolgaria> findAll();

    @Query(
            value = "select * " +
                    "from bolgaria " +
                    "where reg_date BETWEEN ?1 AND ?2 order by id desc",
            nativeQuery = true)
    public List<Bolgaria> findAllDateOrderBy(Date d1, Date d2);
}
