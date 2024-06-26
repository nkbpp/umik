package ru.pfr.repo.umikbd;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.pfr.model.umikbd.User;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
