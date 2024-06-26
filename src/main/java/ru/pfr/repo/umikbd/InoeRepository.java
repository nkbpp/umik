package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.Inoe;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InoeRepository extends JpaRepository<Inoe, Long> {
    Optional<Inoe> findById(Long l);
    List<Inoe> findAll();
    @Query("SELECT i FROM Inoe i WHERE i.reg_date >= :startOfMonth AND i.reg_date < :endOfMonth ORDER BY i.id")
    List<Inoe> findAllByRegDateBetween(LocalDateTime startOfMonth, LocalDateTime endOfMonth);
}
