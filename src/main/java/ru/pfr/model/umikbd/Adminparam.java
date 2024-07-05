package ru.pfr.model.umikbd;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "adminparam")
public class Adminparam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "kolpopitok")
    private Long kolpopitok;

    @Column(name = "koefpopitok")
    private Long koefpopitok;

    @Column(name = "block")
    private Long block;


    public Adminparam() {
    }

    public Adminparam(Long kolpopitok, Long koefpopitok, Long block) {
        id = 1L;
        this.kolpopitok = kolpopitok;
        this.koefpopitok = koefpopitok;
        this.block = block;
    }

}
