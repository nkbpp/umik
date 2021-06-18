package ru.pfr.repo.umikbd;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.pfr.model.umikbd.Spravkonv;


import java.util.List;
import java.util.Optional;

public interface SpravkonvRepository extends JpaRepository<Spravkonv, Long> {
    public Optional<Spravkonv> findById(Long l);
    public List<Spravkonv> findAll();
}
