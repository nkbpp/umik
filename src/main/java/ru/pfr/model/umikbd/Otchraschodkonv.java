package ru.pfr.model.umikbd;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "otchraschodkonv")
public class Otchraschodkonv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_prihod")
    private Prihod prihod;

    @Column(name = "dat")
    private Date date;

    public String getDatestr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(this.date);
    }

    @Column(name = "reestr1")
    private Integer reestr1;

/*    @Column(name = "reestr2")
    private Integer reestr2;*/

    public Integer getRashod(){
        return this.reestr1;    }

    @Column(name = "ostatok")
    private Integer ostatok;


    public Otchraschodkonv() {
    }

    public Otchraschodkonv(Prihod prihod, Date date, Integer reestr1, Integer ostatok) {
        this.prihod = prihod;
        this.date = date;
        this.reestr1 = reestr1;
        this.ostatok = ostatok;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prihod getPrihod() {
        return prihod;
    }

    public void setPrihod(Prihod prihod) {
        this.prihod = prihod;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getReestr1() {
        return reestr1;
    }

    public void setReestr1(Integer reestr1) {
        this.reestr1 = reestr1;
    }


    public Integer getOstatok() {
        return ostatok;
    }

    public void setOstatok(Integer ostatok) {
        this.ostatok = ostatok;
    }
}
