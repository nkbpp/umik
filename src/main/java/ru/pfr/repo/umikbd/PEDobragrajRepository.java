package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.PEDobragraj;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PEDobragrajRepository extends JpaRepository<PEDobragraj, Long> {

    Optional<PEDobragraj> findById(Long l);

    List<PEDobragraj> findAll();

    @Query(
            value = "select * " +
                    "from pedobragraj " +
                    "where reg_date BETWEEN ?1 AND ?2",
            nativeQuery = true)
    List<PEDobragraj> findAllByRegDateBetween(LocalDateTime d1, LocalDateTime d2);

    @Query(
            value = "select * " +
                    "from pedobragraj " +
                    "where reg_date BETWEEN ?1 AND ?2 order by id desc",
            nativeQuery = true)
    List<PEDobragraj> findAllDateOrderBy(LocalDateTime d1, LocalDateTime d2);

}
