package ru.pfr.model.umikbd;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "logi")
public class Logi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "datelog")
    private LocalDateTime date;

    @Column(name = "user")
    private String user;

    @Column(name = "type")
    private Long type;

    @Column(name = "text")
    private String text;

    public Logi() {
    }

    public Logi(LocalDateTime date, String user, Long type, String text) {
        this.date = date;
        this.user = user;
        this.type = type;
        this.text = text;
    }

    public Logi(LocalDateTime date, String user, String text) {
        this.date = date;
        this.user = user;
        this.type = 0L;
        this.text = text;
    }

}
