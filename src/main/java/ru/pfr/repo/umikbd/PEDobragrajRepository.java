package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.PEDobragraj;


import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PEDobragrajRepository extends JpaRepository<PEDobragraj, Long> {

    public Optional<PEDobragraj> findById(Long l);

    public List<PEDobragraj> findAll();

    @Query(
            value = "select * " +
                    "from pedobragraj " +
                    "where reg_date BETWEEN ?1 AND ?2 ",
            nativeQuery = true)
    public List<PEDobragraj> findAllDate(Date d1, Date d2);

    @Query(
            value = "select * " +
                    "from pedobragraj " +
                    "where reg_date BETWEEN ?1 AND ?2 order by id desc",
            nativeQuery = true)
    public List<PEDobragraj> findAllDateOrderBy(Date d1, Date d2);

}
