package ru.pfr.model.umikbd;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "rayon")
public class Rayon implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_rayon")
    private Long id_rayon;

    @Setter
    @Getter
    @Column(name = "namerayon")
    private String namerayon;

    @Setter
    @Getter
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

    @Override
    public String getAuthority() {
        return getKod();
    }
}
