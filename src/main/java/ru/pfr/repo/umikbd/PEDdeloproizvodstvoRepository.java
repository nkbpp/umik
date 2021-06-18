package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.PEDdeloproizvodstvo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PEDdeloproizvodstvoRepository extends JpaRepository<PEDdeloproizvodstvo, Long> {

    public Optional<PEDdeloproizvodstvo> findById(Long l);

    public List<PEDdeloproizvodstvo> findAll();

    @Query(
            value = "select * " +
                    "from peddeloproizvodstvo " +
                    "where reg_date BETWEEN ?1 AND ?2 ",
            nativeQuery = true)
    public List<PEDdeloproizvodstvo> findAllDate(Date d1, Date d2);

    @Query(
            value = "select * " +
                    "from peddeloproizvodstvo " +
                    "where reg_date BETWEEN ?1 AND ?2 order by id desc",
            nativeQuery = true)
    public List<PEDdeloproizvodstvo> findAllDateOrderBy(Date d1, Date d2);
}
