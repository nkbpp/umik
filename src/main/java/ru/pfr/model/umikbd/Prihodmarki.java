package ru.pfr.model.umikbd;

import lombok.Getter;
import lombok.Setter;
import ru.pfr.global.DateUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "prihodmarki")
public class Prihodmarki {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "price")
    private Double price;

    @Column(name = "dat")
    private LocalDateTime date;

    public String getDatestr() {
        return DateUtils.formatToString(date);
    }

    public Prihodmarki() {
    }

    public Prihodmarki(Double price, LocalDateTime date) {
        this.price = price;
        this.date = date;
    }

}
