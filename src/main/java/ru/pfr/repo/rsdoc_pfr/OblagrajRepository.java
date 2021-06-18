package ru.pfr.repo.rsdoc_pfr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.rsdoc_pfr.Oblagraj;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OblagrajRepository extends JpaRepository<Oblagraj, Long> {

    public Optional<Oblagraj> findById(Long l);

    //public Optional<Deloproizvodstvo> findByRegnumberAndRegdateBetween(String r, Date d1, Date d2);


    @Query(
            value = "select * " +
                    "from oblagraj " +
                    "where reg_number=?1 and reg_date BETWEEN ?2 AND ?3 ",
            nativeQuery = true)
    public List<Oblagraj> findByRegnumber(Integer r, Date d1, Date d2);

    @Query(
            value = "select * " +
                    "from oblagraj " +
                    "where reg_number=?1 and reg_date BETWEEN ?2 AND ?3 ",
            nativeQuery = true)
    public List<Oblagraj> findByRegnumber2(Integer r, Date d1, Date d2);


    @Query(
            value = "select * " +
                    "from oblagraj " +
                    "where reg_number BETWEEN ?1 AND ?2 and reg_date BETWEEN ?3 AND ?4 ",
            nativeQuery = true)
    public List<Oblagraj> findByRegnumber3(Integer r, Integer r2, Date d1, Date d2);

    public List<Oblagraj> findAll();
}
