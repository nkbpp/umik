package ru.pfr.model.umikbd;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "pravopriem")
public class Pravopriem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Date date;

    @Column(name = "konvert_d")
    private Long konvert_d;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_prihod")
    private Prihod prihod;

    @Column(name = "marki_k_zak_pis")
    private Float marki_k_zak_pis;


    public String get_datestr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(this.date);
    }

    public Float getPrice() {
        return this.prihod.getPrice();
    }

    public Float get_sumk() {
        return this.konvert_d*this.prihod.getPrice();
    }

    public Float get_sumob() {
        return get_sumk()+this.marki_k_zak_pis;
    }

    public Pravopriem() {
    }

    public Pravopriem(Date date, Long konvert_d, Prihod prihod, Float marki_k_zak_pis) {
        this.date = date;
        this.konvert_d = konvert_d;
        this.prihod = prihod;
        this.marki_k_zak_pis = marki_k_zak_pis;
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

    public Long getKonvert_d() {
        return konvert_d;
    }

    public void setKonvert_d(Long konvert_d) {
        this.konvert_d = konvert_d;
    }

    public Prihod getPrihod() {
        return prihod;
    }

    public void setPrihod(Prihod prihod) {
        this.prihod = prihod;
    }

    public Float getMarki_k_zak_pis() {
        return marki_k_zak_pis;
    }

    public void setMarki_k_zak_pis(Float marki_k_zak_pis) {
        this.marki_k_zak_pis = marki_k_zak_pis;
    }
}
