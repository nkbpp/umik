package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.Bolgaria;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BolgariaRepository extends JpaRepository<Bolgaria, Long> {
    Optional<Bolgaria> findById(Long l);
    List<Bolgaria> findAll();

    @Query("SELECT b FROM Bolgaria b WHERE b.reg_date >= :startOfMonth AND b.reg_date < :endOfMonth ORDER BY b.id")
    List<Bolgaria> findAllByRegDateBetween(LocalDateTime startOfMonth, LocalDateTime endOfMonth);

}
