package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.Otchraschodkonv;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OtchraschodkonvRepository extends JpaRepository<Otchraschodkonv, Long> {
    public Optional<Otchraschodkonv> findById(Long l);
    public List<Otchraschodkonv> findAll();

   @Query(
            value = "select id, id_prihod, dat, reestr1, ostatok " +
                    "from otchraschodkonv " +
                    "where dat BETWEEN ?1 AND ?2 ",
            nativeQuery = true)
    public List<Otchraschodkonv> findAllD(Date d1, Date d2);


}
