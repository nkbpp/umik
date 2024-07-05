package ru.pfr.model.umikbd;

import lombok.Getter;
import lombok.Setter;
import ru.pfr.global.DateUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "peddeloproizvodstvo")
public class PEDdeloproizvodstvo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "iddeloproizvodstvo")
    private Long iddeloproizvodstvo;

    @Column(name = "reg_pref")
    private String reg_pref;

    @Column(name = "reg_number")
    private String reg_number;

    @Column(name = "reg_postf")
    private String reg_postf;

    @Column(name = "reg_date")
    private LocalDateTime reg_date;

    @Column(name = "id_name")
    private Long id_name;

    @Column(name = "name")
    private String name;

    @Column(name = "text_org")
    private String text_org;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_konv")
    private Spravkonv spravkonv;

    @Column(name = "sum")
    private Double sum;

    @Column(name = "kol_vo")
    private Integer kol_vo;

    public String getReg_datestr() {
        return DateUtils.formatToString(reg_date);
    }

    public PEDdeloproizvodstvo() {
    }

    public PEDdeloproizvodstvo(Long iddeloproizvodstvo, String reg_pref, String reg_number, String reg_postf, LocalDateTime reg_date, Long id_name, String name, String text_org, Spravkonv spravkonv, Double sum, Integer kol_vo) {
        this.iddeloproizvodstvo = iddeloproizvodstvo;
        this.reg_pref = reg_pref;
        this.reg_number = reg_number;
        this.reg_postf = reg_postf;
        this.reg_date = reg_date;
        this.id_name = id_name;
        this.name = name;
        this.text_org = text_org;
        this.spravkonv = spravkonv;
        this.sum = sum;
        this.kol_vo = kol_vo;
    }

}
