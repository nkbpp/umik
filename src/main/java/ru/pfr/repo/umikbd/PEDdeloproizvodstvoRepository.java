package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.PEDdeloproizvodstvo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PEDdeloproizvodstvoRepository extends JpaRepository<PEDdeloproizvodstvo, Long> {

    Optional<PEDdeloproizvodstvo> findById(Long l);

    List<PEDdeloproizvodstvo> findByName(String name);

    List<PEDdeloproizvodstvo> findAll();

    @Query(
            value = "select * " +
                    "from peddeloproizvodstvo " +
                    "where reg_date BETWEEN ?1 AND ?2",
            nativeQuery = true)
    List<PEDdeloproizvodstvo> findAllByRegDateBetween(LocalDateTime d1, LocalDateTime d2);

    @Query(
            value = "select * " +
                    "from peddeloproizvodstvo " +
                    "where reg_date BETWEEN ?1 AND ?2 order by id desc",
            nativeQuery = true)
    List<PEDdeloproizvodstvo> findAllDateOrderBy(LocalDateTime d1, LocalDateTime d2);
}
