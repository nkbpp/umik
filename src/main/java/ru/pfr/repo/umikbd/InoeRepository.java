package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.Inoe;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface InoeRepository extends JpaRepository<Inoe, Long> {
    public Optional<Inoe> findById(Long l);
    public List<Inoe> findAll();

    @Query(
            value = "select * " +
                    "from inoe " +
                    "where reg_date BETWEEN ?1 AND ?2 ",
            nativeQuery = true)
    public List<Inoe> findAllD(Date d1, Date d2);

    @Query(
            value = "select * " +
                    "from inoe " +
                    "where reg_date BETWEEN ?1 AND ?2 order by id desc",
            nativeQuery = true)
    public List<Inoe> findAllDateOrderBy(Date d1, Date d2);
}
