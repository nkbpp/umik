package ru.pfr.model.umikbd;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "spravkonv")
public class Spravkonv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    public Spravkonv() {
    }

    public Spravkonv(String type) {
        this.type = type;
    }

    public boolean isA4(){
        return this.id==1;
    }

    public boolean isC5(){
        return this.id==4;
    }

    public boolean is110x220(){
        return this.id==5;
    }

    public boolean isD(){
        return this.id==3;
    }

    public boolean ispoly(){
        return this.id==6;
    }

    public boolean is11(){
        return this.id==11;
    }

    public boolean is14(){
        return this.id==14;
    }
}
