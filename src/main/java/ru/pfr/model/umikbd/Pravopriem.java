package ru.pfr.model.umikbd;

import lombok.Getter;
import lombok.Setter;
import ru.pfr.global.DateUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "pravopriem")
public class Pravopriem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "konvert_d")
    private Long konvert_d;

    @Column(name = "cena_sell")
    private Double cena_sell; // тариф за пересылку 20г

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_prihod")
    private Prihod prihod;

    @Column(name = "marki_k_zak_pis")
    private Double marki_k_zak_pis;

    public Pravopriem() {
    }

    public Pravopriem(LocalDateTime date, Long konvert_d, Prihod prihod, Double cena_sell, Double marki_k_zak_pis) {
        this.date = date;
        this.konvert_d = konvert_d;
        this.prihod = prihod;
        this.cena_sell = cena_sell;
        this.marki_k_zak_pis = marki_k_zak_pis;
    }

    public String get_datestr() {
        return DateUtils.formatToString(date);
    }

    public Double getPrice() {
        return this.prihod.getPrice();
    }

    public Double get_sumk() {
        return (this.cena_sell!=0 && this.cena_sell>0)?
                (this.konvert_d * this.cena_sell):(this.konvert_d * this.prihod.getPrice());
    }

    public Double get_sumob() {
        return get_sumk()+this.marki_k_zak_pis;
    }

}
