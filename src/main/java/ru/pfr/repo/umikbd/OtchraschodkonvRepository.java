package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.Otchraschodkonv;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OtchraschodkonvRepository extends JpaRepository<Otchraschodkonv, Long> {
    Optional<Otchraschodkonv> findById(Long l);

    List<Otchraschodkonv> findAll();

    @Query(
            value = "select id, id_prihod, dat, reestr1, ostatok " +
                    "from otchraschodkonv " +
                    "where dat BETWEEN ?1 AND ?2",
            nativeQuery = true)
    List<Otchraschodkonv> findAllD(LocalDateTime d1, LocalDateTime d2);

}
