package ru.pfr.model.umikbd;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "pedobragraj")
public class PEDobragraj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "idobragraj")
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

    @Column(name = "text_fio")
    private String text_fio;

    @Column(name = "addr_list")
    private String addr_list;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_konv")
    private Spravkonv spravkonv;

    @Column(name = "sum")
    private Double sum;

    @Column(name = "kol_vo")
    private Integer kol_vo;

    public String getReg_datestr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(this.reg_date);
    }

    public PEDobragraj() {
    }

    public PEDobragraj(Long iddeloproizvodstvo, String reg_pref, String reg_number, String reg_postf, Date reg_date, Long id_name, String name, String text_org, String text_fio, String addr_list, Spravkonv spravkonv, Double sum, Integer kol_vo) {
        this.iddeloproizvodstvo = iddeloproizvodstvo;
        this.reg_pref = reg_pref;
        this.reg_number = reg_number;
        this.reg_postf = reg_postf;
        this.reg_date = reg_date;
        this.id_name = id_name;
        this.name = name;
        this.text_org = text_org;
        this.text_fio = text_fio;
        this.addr_list = addr_list;
        this.spravkonv = spravkonv;
        this.sum = sum;
        this.kol_vo = kol_vo;
    }

    public String getText_fio() {
        return text_fio;
    }

    public void setText_fio(String text_fio) {
        this.text_fio = text_fio;
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

    public String getAddr_list() {
        return addr_list;
    }

    public void setAddr_list(String addr_list) {
        this.addr_list = addr_list;
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

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }
}
