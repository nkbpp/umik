package ru.pfr.repo.umikbd;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.Prihodmarki;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PrihodmarkiRepository extends JpaRepository<Prihodmarki, Long> {
    Optional<Prihodmarki> findById(Long l);
    List<Prihodmarki> findAll();

    @Query(
            value = "select id, price, dat " +
                    "from prihodmarki " +
                    "where dat BETWEEN ?1 AND ?2  order by dat desc" ,
            nativeQuery = true)
    List<Prihodmarki> findAllDat(LocalDateTime d1, LocalDateTime d2);

}
