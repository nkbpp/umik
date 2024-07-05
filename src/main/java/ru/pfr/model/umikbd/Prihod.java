package ru.pfr.model.umikbd;

import lombok.Getter;
import lombok.Setter;
import ru.pfr.global.DateUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "prihod")
public class Prihod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "indexx")
    private String index;

    @Column(name = "kol_vo")
    private Integer kol_vo;

    @Column(name = "price")
    private Double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_konv")
    private Spravkonv spravkonv;

    @Column(name = "dat")
    private LocalDateTime date;

    public String getDatestr() {
        return DateUtils.formatToString(date);
    }

    public String getFullName() {
        return this.prefix + " " + this.index;
    }

    public String getFullNamePrice() {
        return this.prefix + " " + this.index + " Цена: " + this.price;
    }

    public Prihod() {
    }

    public Prihod(String prefix, String index, Integer kol_vo, Double price, Spravkonv spravkonv, LocalDateTime date) {
        this.prefix = prefix;
        this.index = index;
        this.kol_vo = kol_vo;
        this.price = price;
        this.spravkonv = spravkonv;
        this.date = date;
    }

}
