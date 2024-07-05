package ru.pfr.model.umikbd;

import lombok.Getter;
import lombok.Setter;
import ru.pfr.global.DateUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "bolgaria")
public class Bolgaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reg_number")
    private String reg_number;

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



    public Bolgaria() {
    }

    public Bolgaria(String reg_number, LocalDateTime reg_date, Long id_name, String name, String text_org, Spravkonv spravkonv, Double sum, Integer kol_vo) {
        this.reg_number = reg_number;
        this.reg_date = reg_date;
        this.id_name = id_name;
        this.name = name;
        this.text_org = text_org;
        this.spravkonv = spravkonv;
        this.sum = sum;
        this.kol_vo = kol_vo;
    }

    public String getReg_datestr() {
        return DateUtils.formatToString(this.reg_date);
    }

}
