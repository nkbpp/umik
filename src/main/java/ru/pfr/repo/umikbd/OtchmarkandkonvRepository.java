package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.Otchmarkandkonv;


import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OtchmarkandkonvRepository extends JpaRepository<Otchmarkandkonv, Long> {
    public Optional<Otchmarkandkonv> findById(Long l);
    public List<Otchmarkandkonv> findAll();

   @Query(
            value = "select id, id_prihod, dat, reestr1, ostatok " +
                    "from otchmarkandkonv " +
                    "where dat BETWEEN ?1 AND ?2 ",
            nativeQuery = true)
    public List<Otchmarkandkonv> findAllD(Date d1, Date d2);

}
