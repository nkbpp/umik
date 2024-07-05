package ru.pfr.model.umikbd;

import lombok.Getter;
import lombok.Setter;
import ru.pfr.global.DateUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "otchmark")
public class Otchmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dat")
    private LocalDateTime date;

    public String getDatestr() {
        return DateUtils.formatToString(this.date);
    }

    @Column(name = "rashod1")
    private Double rashod1;

    @Column(name = "rashod2")
    private Double rashod2;

    @Column(name = "ostatok")
    private Double ostatok;


    public Otchmark() {
    }

    public Otchmark(LocalDateTime date, Double rashod1, Double rashod2, Double ostatok) {
        this.date = date;
        this.rashod1 = rashod1;
        this.rashod2 = rashod2;
        this.ostatok = ostatok;
    }

}
