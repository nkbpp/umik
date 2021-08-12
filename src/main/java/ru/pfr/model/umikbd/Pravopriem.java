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

    @Column(name = "cena_sell")
    private Float cena_sell; // тариф за пересылку 20г

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
        return (this.cena_sell!=0 && this.cena_sell>0)?
                (this.konvert_d * this.cena_sell):(this.konvert_d * this.prihod.getPrice());
    }

    public Float get_sumob() {
        return get_sumk()+this.marki_k_zak_pis;
    }

    public Pravopriem() {
    }

    public Pravopriem(Date date, Long konvert_d, Prihod prihod, Float cena_sell, Float marki_k_zak_pis) {
        this.date = date;
        this.konvert_d = konvert_d;
        this.prihod = prihod;
        this.cena_sell = cena_sell;
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

/*    public Float getStoimost() {
        return cena_sell!=null && cena_sell>0?cena_sell:marki_k_zak_pis;
    }*/

    public Float getCena_sell() {
        return cena_sell;
    }

    public void setCena_sell(Float cena_sell) {
        this.cena_sell = cena_sell;
    }

    public void setMarki_k_zak_pis(Float marki_k_zak_pis) {
        this.marki_k_zak_pis = marki_k_zak_pis;
    }
}
