package ru.pfr.repo.umikbd;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.pfr.model.umikbd.Vidanykonv;

import java.util.List;
import java.util.Optional;

public interface VidanykonvRepository extends JpaRepository<Vidanykonv, Long> {
    Optional<Vidanykonv> findById(Long l);
    List<Vidanykonv> findAll();
}
