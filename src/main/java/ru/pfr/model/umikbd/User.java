package ru.pfr.model.umikbd;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
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
        this.active = 1L;
        this.rayon = rayon;
        this.date = LocalDateTime.now();
    }

}
