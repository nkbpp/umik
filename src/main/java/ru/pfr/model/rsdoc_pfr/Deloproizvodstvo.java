package ru.pfr.model.rsdoc_pfr;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "deloproizvodstvo")
public class Deloproizvodstvo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id2")
    private Long id2;

    @Column(name = "id")
    private Long id;

    @Column(name = "reg_pref")
    private String reg_pref;

    @Column(name = "reg_number")
    private String regnumber;

    @Column(name = "reg_postf")
    private String reg_postf;

    @Column(name = "reg_date")
    private Date regdate;

    @Column(name = "id_type_send")
    private Long id_type_send;

    @Column(name = "n")
    private String name;

    @Column(name = "id_com_type")
    private String id_com_type;

    @Column(name = "text_org")
    private String text_org;

    public String getReg_datestr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(this.regdate);
    }

    public Deloproizvodstvo() {
    }

    public Deloproizvodstvo(Long id, String reg_pref, String regnumber, String reg_postf, Date regdate, Long id_type_send, String name, String id_com_type, String text_org) {
        this.id = id;
        this.reg_pref = reg_pref;
        this.regnumber = regnumber;
        this.reg_postf = reg_postf;
        this.regdate = regdate;
        this.id_type_send = id_type_send;
        this.name = name;
        this.id_com_type = id_com_type;
        this.text_org = text_org;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReg_pref() {
        return reg_pref;
    }

    public void setReg_pref(String reg_pref) {
        this.reg_pref = reg_pref;
    }

    public String getRegnumber() {
        return regnumber;
    }

    public void setRegnumber(String regnumber) {
        this.regnumber = regnumber;
    }

    public String getReg_postf() {
        return reg_postf;
    }

    public void setReg_postf(String reg_postf) {
        this.reg_postf = reg_postf;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public Long getId_type_send() {
        return id_type_send;
    }

    public void setId_type_send(Long id_type_send) {
        this.id_type_send = id_type_send;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_com_type() {
        return id_com_type;
    }

    public void setId_com_type(String id_com_type) {
        this.id_com_type = id_com_type;
    }

    public String getText_org() {
        return text_org;
    }

    public void setText_org(String text_org) {
        this.text_org = text_org;
    }
}
