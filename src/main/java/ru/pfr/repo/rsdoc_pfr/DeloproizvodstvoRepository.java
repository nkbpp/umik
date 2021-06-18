package ru.pfr.repo.rsdoc_pfr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.rsdoc_pfr.Deloproizvodstvo;


import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DeloproizvodstvoRepository extends JpaRepository<Deloproizvodstvo, Long> {

    public Optional<Deloproizvodstvo> findById(Long l);

    //public Optional<Deloproizvodstvo> findByRegnumberAndRegdateBetween(String r, Date d1, Date d2);


    @Query(
            value = "select * " +
                    "from deloproizvodstvo " +
                    "where reg_number=?1 and reg_date BETWEEN ?2 AND ?3 ",
            nativeQuery = true)
    public Optional<Deloproizvodstvo> findByRegnumber(Integer r, Date d1, Date d2);

    @Query(
            value = "select * " +
                    "from deloproizvodstvo " +
                    "where reg_number=?1 and reg_date BETWEEN ?2 AND ?3 and text_org=?4 and n=?5  ",
            nativeQuery = true)
    public List<Deloproizvodstvo> findByRegnumber2(Integer r, Date d1, Date d2, String textorg, String name);


    @Query(
            value = "select * " +
                    "from deloproizvodstvo " +
                    "where reg_number BETWEEN ?1 AND ?2 and reg_date BETWEEN ?3 AND ?4 ",
            nativeQuery = true)
    public List<Deloproizvodstvo> findByRegnumber3(Integer r, Integer r2, Date d1, Date d2);

    public List<Deloproizvodstvo> findAll();
}
