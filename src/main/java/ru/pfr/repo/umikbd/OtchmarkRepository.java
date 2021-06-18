package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.Otchmark;
import ru.pfr.model.umikbd.Otchmarkandkonv;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OtchmarkRepository extends JpaRepository<Otchmark, Long> {
    public Optional<Otchmark> findById(Long l);
    public List<Otchmark> findAll();

   @Query(
            value = "select id, dat, rashod1, rashod2, ostatok " +
                    "from otchmark " +
                    "where dat BETWEEN ?1 AND ?2 ",
            nativeQuery = true)
    public List<Otchmark> findAllDat(Date d1, Date d2);

}
