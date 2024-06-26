package ru.pfr.model.umikbd;

import ru.pfr.global.DateUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inoe")
public class Inoe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reg_date")
    private LocalDateTime reg_date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vidanykonv")
    private Vidanykonv vidanykonv;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_konv")
    private Spravkonv spravkonv;

    @Column(name = "kol_vo")
    private Integer kol_vo;

    public String getReg_datestr() {
        return DateUtils.formatToString(this.reg_date);
    }

    public Inoe() {
    }

    public Inoe(LocalDateTime reg_date, Vidanykonv vidanykonv, Spravkonv spravkonv, Integer kol_vo) {
        this.reg_date = reg_date;
        this.vidanykonv = vidanykonv;
        this.spravkonv = spravkonv;
        this.kol_vo = kol_vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getReg_date() {
        return reg_date;
    }

    public void setReg_date(LocalDateTime reg_date) {
        this.reg_date = reg_date;
    }

    public Vidanykonv getVidanykonv() {
        return vidanykonv;
    }

    public void setVidanykonv(Vidanykonv vidanykonv) {
        this.vidanykonv = vidanykonv;
    }

    public Spravkonv getSpravkonv() {
        return spravkonv;
    }

    public void setSpravkonv(Spravkonv spravkonv) {
        this.spravkonv = spravkonv;
    }

    public Integer getKol_vo() {
        return kol_vo;
    }

    public void setKol_vo(Integer kol_vo) {
        this.kol_vo = kol_vo;
    }
}
