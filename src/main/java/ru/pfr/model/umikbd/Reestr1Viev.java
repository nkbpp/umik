package ru.pfr.model.umikbd;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "reestr1")
public class Reestr1Viev {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "reg_date")
    private Date reg_date;

    @Column(name = "reg_number")
    private String reg_number;

    @Column(name = "text_org")
    private String text_org;

    @Column(name = "text_fio")
    private String text_fio;

    public String getAddr() {
        String s;
        if(text_org.equals("") || text_org==null)
            s=text_fio;
        else s=text_org;
        return s;
    }

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_konv")
    private Spravkonv spravkonv;

    @Column(name = "id_konv1")
    private Integer id_konv1;

    @Column(name = "id_konv4")
    private Integer id_konv4;

    @Column(name = "id_konv5")
    private Integer id_konv5;

    @Column(name = "id_konv6")
    private Integer id_konv6;

    @Column(name = "id_konv7")
    private Integer id_konv7;

    @Column(name = "id_konv22")
    private Integer id_konv22;

    @Column(name = "id_konv11")
    private Integer id_konv11;

    @Column(name = "id_konv14")
    private Integer id_konv14;

    @Column(name = "sum")
    private String sum;

    @Column(name = "kol_vo")
    private Integer kol_vo;

    public Float getsumob(){
        return  this.kol_vo * Float.valueOf(this.sum);
    }

    public String getReg_datestr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(this.reg_date);
    }

    public Reestr1Viev() {
    }

    public Reestr1Viev(Long id, Date reg_date, String reg_number, String text_org,
                       String text_fio, String name, Spravkonv spravkonv, Integer id_konv1,
                       Integer id_konv4, Integer id_konv5, Integer id_konv6, Integer id_konv7,
                       Integer id_konv22, Integer id_konv11, Integer id_konv14,
                       String sum, Integer kol_vo) {
        this.id = id;
        this.reg_date = reg_date;
        this.reg_number = reg_number;
        this.text_org = text_org;
        this.text_fio = text_fio;
        this.name = name;
        this.spravkonv = spravkonv;
        this.id_konv1 = id_konv1;
        this.id_konv4 = id_konv4;
        this.id_konv5 = id_konv5;
        this.id_konv6 = id_konv6;
        this.id_konv7 = id_konv7;
        this.id_konv22 = id_konv22;
        this.id_konv11 = id_konv11;
        this.id_konv14 = id_konv14;
        this.sum = sum;
        this.kol_vo = kol_vo;
    }

    public String getText_fio() {
        return text_fio;
    }

    public void setText_fio(String text_fio) {
        this.text_fio = text_fio;
    }

    public Integer getId_konv1() {
        return id_konv1;
    }

    public void setId_konv1(Integer id_konv1) {
        this.id_konv1 = id_konv1;
    }

    public Integer getId_konv4() {
        return id_konv4;
    }

    public void setId_konv4(Integer id_konv4) {
        this.id_konv4 = id_konv4;
    }

    public Integer getId_konv5() {
        return id_konv5;
    }

    public void setId_konv5(Integer id_konv5) {
        this.id_konv5 = id_konv5;
    }

    public Integer getId_konv6() {
        return id_konv6;
    }

    public void setId_konv6(Integer id_konv6) {
        this.id_konv6 = id_konv6;
    }

    public Integer getId_konv7() {
        return id_konv7;
    }

    public void setId_konv7(Integer id_konv7) {
        this.id_konv7 = id_konv7;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getReg_date() {
        return reg_date;
    }

    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }

    public String getReg_number() {
        return reg_number;
    }

    public void setReg_number(String reg_number) {
        this.reg_number = reg_number;
    }

    public String getText_org() {
        return text_org;
    }

    public void setText_org(String text_org) {
        this.text_org = text_org;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Spravkonv getSpravkonv() {
        return spravkonv;
    }

    public void setSpravkonv(Spravkonv spravkonv) {
        this.spravkonv = spravkonv;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public Integer getKol_vo() {
        return kol_vo;
    }

    public void setKol_vo(Integer kol_vo) {
        this.kol_vo = kol_vo;
    }

    public Integer getId_konv22() {
        return id_konv22;
    }

    public void setId_konv22(Integer id_konv22) {
        this.id_konv22 = id_konv22;
    }


    public Integer getId_konv11() {
        return id_konv11;
    }

    public void setId_konv11(Integer id_konv7) {
        this.id_konv11 = id_konv11;
    }

    public Integer getId_konv14() {
        return id_konv14;
    }

    public void setId_konv14(Integer id_konv14) {
        this.id_konv14 = id_konv14;
    }
}
