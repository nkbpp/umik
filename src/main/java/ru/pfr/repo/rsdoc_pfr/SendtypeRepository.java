package ru.pfr.repo.rsdoc_pfr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.rsdoc_pfr.Sendtype;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SendtypeRepository extends JpaRepository<Sendtype, Long> {

    public Optional<Sendtype> findById(Long l);

    //public Optional<Deloproizvodstvo> findByRegnumberAndRegdateBetween(String r, Date d1, Date d2);


    @Query(
            value = "select  sendtypeid, name from sendtype " +
                    "where sendtypeid=11 or sendtypeid=3 or sendtypeid=12 or sendtypeid=13",
            nativeQuery = true)
    public List<Sendtype> findAllS();

    public List<Sendtype> findAll();
}
