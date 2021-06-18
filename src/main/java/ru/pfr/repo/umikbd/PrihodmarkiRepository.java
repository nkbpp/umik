package ru.pfr.repo.umikbd;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.Prihodmarki;


import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PrihodmarkiRepository extends JpaRepository<Prihodmarki, Long> {
    public Optional<Prihodmarki> findById(Long l);
    public List<Prihodmarki> findAll();

    @Query(
            value = "select id, price, dat " +
                    "from prihodmarki " +
                    "where dat BETWEEN ?1 AND ?2 ",
            nativeQuery = true)
    public List<Prihodmarki> findAllDat(Date d1, Date d2);

}
