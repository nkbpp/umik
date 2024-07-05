package ru.pfr.model.umikbd;

import lombok.Getter;
import lombok.Setter;
import ru.pfr.global.DateUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "otchraschodkonv")
public class Otchraschodkonv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_prihod")
    private Prihod prihod;

    @Column(name = "dat")
    private LocalDateTime date;

    public String getDatestr() {
        return DateUtils.formatToString(date);
    }

    @Column(name = "reestr1")
    private Integer reestr1;

    public Integer getRashod(){
        return this.reestr1;    }

    @Column(name = "ostatok")
    private Integer ostatok;


    public Otchraschodkonv() {
    }

    public Otchraschodkonv(Prihod prihod, LocalDateTime date, Integer reestr1, Integer ostatok) {
        this.prihod = prihod;
        this.date = date;
        this.reestr1 = reestr1;
        this.ostatok = ostatok;
    }


}
