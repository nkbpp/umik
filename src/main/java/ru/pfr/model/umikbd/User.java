package ru.pfr.model.umikbd;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "active")
    private Long active;

    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rayon")
    private Rayon rayon;

    public User() {
    }

    public User(Long id, String login, Long active, Rayon rayon) {
        this.id = id;
        this.login = login;
        this.active = active;
        this.rayon = rayon;
        this.date = LocalDateTime.now();
    }

    public User(String login, Long active, Rayon rayon) {
        this.login = login;
        this.active = active;
        this.rayon = rayon;
        this.date = LocalDateTime.now();
    }

    public User(String login, Rayon rayon) {
        this.login = login;
        this.active = 1l;
        this.rayon = rayon;
        this.date = LocalDateTime.now();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public Rayon getRayon() {
        return rayon;
    }

    public void setRayon(Rayon rayon) {
        this.rayon = rayon;
    }
}
