package ru.pfr.model.umikbd;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private Date reg_date;

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
    private Float sum;

    @Column(name = "kol_vo")
    private Integer kol_vo;

    public String getReg_datestr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(this.reg_date);
    }

    public PEDdeloproizvodstvo() {
    }

    public PEDdeloproizvodstvo(Long iddeloproizvodstvo, String reg_pref, String reg_number, String reg_postf, Date reg_date, Long id_name, String name, String text_org, Spravkonv spravkonv, Float sum, Integer kol_vo) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIddeloproizvodstvo() {
        return iddeloproizvodstvo;
    }

    public void setIddeloproizvodstvo(Long iddeloproizvodstvo) {
        this.iddeloproizvodstvo = iddeloproizvodstvo;
    }

    public String getReg_pref() {
        return reg_pref;
    }

    public void setReg_pref(String reg_pref) {
        this.reg_pref = reg_pref;
    }

    public String getReg_number() {
        return reg_number;
    }

    public void setReg_number(String reg_number) {
        this.reg_number = reg_number;
    }

    public String getReg_postf() {
        return reg_postf;
    }

    public void setReg_postf(String reg_postf) {
        this.reg_postf = reg_postf;
    }

    public Date getReg_date() {
        return reg_date;
    }

    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }

    public Long getId_name() {
        return id_name;
    }

    public void setId_name(Long id_name) {
        this.id_name = id_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText_org() {
        return text_org;
    }

    public void setText_org(String text_org) {
        this.text_org = text_org;
    }

    public Spravkonv getSpravkonv() {
        return spravkonv;
    }

    public void setSpravkonv(Spravkonv spravkonv) {
        this.spravkonv = spravkonv;
    }

    public Float getSum() {
        return sum;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }

    public Integer getKol_vo() {
        return kol_vo;
    }

    public void setKol_vo(Integer kol_vo) {
        this.kol_vo = kol_vo;
    }
}
