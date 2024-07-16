package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pfr.model.umikbd.VidDost;

import java.util.List;
import java.util.Optional;

public interface VidDostRepository extends JpaRepository<VidDost, Long> {
    Optional<VidDost> findById(Long l);

    List<VidDost> findAll();
}
