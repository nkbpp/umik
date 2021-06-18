package ru.pfr.model.umikbd;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "otchmark")
public class Otchmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

/*    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_prihodmark")
    private Prihodmarki prihodmark;*/

    @Column(name = "dat")
    private Date date;

    public String getDatestr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(this.date);
    }

    @Column(name = "rashod1")
    private Double rashod1;

    @Column(name = "rashod2")
    private Double rashod2;

    @Column(name = "ostatok")
    private Double ostatok;


    public Otchmark() {
    }

    public Otchmark( Date date, Double rashod1, Double rashod2, Double ostatok) {
        this.date = date;
        this.rashod1 = rashod1;
        this.rashod2 = rashod2;
        this.ostatok = ostatok;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getRashod1() {
        return rashod1;
    }

    public void setRashod1(Double rashod1) {
        this.rashod1 = rashod1;
    }

    public Double getRashod2() {
        return rashod2;
    }

    public void setRashod2(Double rashod2) {
        this.rashod2 = rashod2;
    }

    public Double getOstatok() {
        return ostatok;
    }

    public void setOstatok(Double ostatok) {
        this.ostatok = ostatok;
    }
}
