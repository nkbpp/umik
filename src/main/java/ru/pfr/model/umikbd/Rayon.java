package ru.pfr.model.umikbd;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "rayon")
public class Rayon implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_rayon")
    private Long id_rayon;

    @Column(name = "namerayon")
    private String namerayon;

    @Column(name = "kod")
    private String kod;

    public Rayon() {
    }

    public Rayon(String namerayon, String kod) {
        this.namerayon = namerayon;
        this.kod = kod;
    }

    public Rayon(Long id_rayon, String namerayon, String kod) {
        this.id_rayon = id_rayon;
        this.namerayon = namerayon;
        this.kod = kod;
    }

    public Long getId() {
        return id_rayon;
    }

    public void setId(Long id) {
        this.id_rayon = id;
    }

    public String getNamerayon() {
        return namerayon;
    }

    public void setNamerayon(String namerayon) {
        this.namerayon = namerayon;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    @Override
    public String getAuthority() {
        return getKod();
    }
}
