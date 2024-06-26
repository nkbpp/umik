package ru.pfr.service.bdumik;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pfr.model.umikbd.Adminparam;
import ru.pfr.repo.umikbd.AdminparamRepository;

import java.util.Optional;


@Service
public class AdminparamService {

    @Autowired
    private AdminparamRepository adminparamRepository;

    /**
     * Сохранить сущность Adminparam в базу данных.
     * @param adminparam сущность Adminparam для сохранения.
     */
    @Transactional
    public void save(Adminparam adminparam) {
        adminparamRepository.save(adminparam);
    }

    /**
     * Найти сущность Adminparam по заданному ID.
     * @return сущность Adminparam.
     * @throws RuntimeException если сущность Adminparam с заданным ID не найдена.
     */
    public Adminparam findByAdminparam() {
        Optional<Adminparam> optionalAdminparam = adminparamRepository.findById(1L);
        if (optionalAdminparam.isPresent()) {
            return optionalAdminparam.get();
        } else {
            throw new RuntimeException("Adminparam не найден с id: 1");
        }
    }

}
