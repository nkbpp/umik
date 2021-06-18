package ru.pfr.model.umikbd;

import javax.persistence.*;

@Entity
@Table(name = "vidanykonv")
public class Vidanykonv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "adres")
    private String adres;

    public Vidanykonv() {
    }

    public Vidanykonv(String adres) {
        this.adres = adres;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }
}
