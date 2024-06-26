package ru.pfr.model.umikbd;

import ru.pfr.global.DateUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
