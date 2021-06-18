package ru.pfr.model.rsdoc_pfr;

import javax.persistence.*;

@Entity
@Table(name = "sendtype")
public class Sendtype {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sendtypeid")
    private Long sendtypeid;

    @Column(name = "name")
    private String name;

    public Sendtype() {
    }

    public Sendtype(String name) {
        this.name = name;
    }

    public Long getSendtypeid() {
        return sendtypeid;
    }

    public void setSendtypeid(Long sendtypeid) {
        this.sendtypeid = sendtypeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
