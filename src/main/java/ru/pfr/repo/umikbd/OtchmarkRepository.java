package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.Otchmark;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OtchmarkRepository extends JpaRepository<Otchmark, Long> {
    Optional<Otchmark> findById(Long l);

    List<Otchmark> findAll();

    @Query(
            value = "select id, dat, rashod1, rashod2, ostatok " +
                    "from otchmark " +
                    "where dat BETWEEN ?1 AND ?2",
            nativeQuery = true)
    List<Otchmark> findAllDat(LocalDateTime d1, LocalDateTime d2);

}
