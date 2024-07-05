package ru.pfr.model.umikbd;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
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

}
