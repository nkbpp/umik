package ru.pfr.repo.umikbd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.pfr.model.umikbd.Reestr1Viev;

import java.util.Date;
import java.util.List;

public interface Reestr1VievRepository extends JpaRepository<Reestr1Viev, Long> {

    public List<Reestr1Viev> findAll();

    @Query(
            value = "select id, reg_date, reg_number, text_org, text_fio, name, id_konv, sum, kol_vo, id_konv1, " +
                    "id_konv4, id_konv5, id_konv6, id_konv7, id_konv22 " +
                    "from otch2 " +
                    "where reg_date BETWEEN ?1 AND ?2 order by reg_date",
            nativeQuery = true)
    public List<Reestr1Viev> findAllD(Date d1, Date d2);

    @Query(
            value = "select 1 id, '2020-01-01' reg_date, '' reg_number, '' text_org, '' text_fio, '' name, 1 id_konv, '' kol_vo, " +
                    "sum(id_konv1) id_konv1, sum(id_konv4) id_konv4, sum(id_konv5) id_konv5, " +
                    "sum(id_konv6) id_konv6, sum(id_konv7) id_konv7, sum(id_konv22) id_konv22, sum(sum) sum " +
                    "from otch2 " +
                    "where reg_date BETWEEN ?1 AND ?2 order by reg_date",
            nativeQuery = true)
    public List<Reestr1Viev> findAllI(Date d1, Date d2);


}
