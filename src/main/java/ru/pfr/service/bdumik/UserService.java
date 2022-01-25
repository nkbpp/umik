package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pfr.model.umikbd.User;
import ru.pfr.repo.umikbd.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByLoginuser(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
