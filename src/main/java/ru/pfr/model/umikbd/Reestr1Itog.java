package ru.pfr.model.umikbd;


import lombok.Getter;

@Getter
public class Reestr1Itog {

    private final Integer konv1;
    private final Integer konv4;
    private final Integer konv5;
    private final Integer konv6;
    private final Integer konv11;
    private final Integer konv14;
    private final Double sum;

    public Reestr1Itog(Integer konv1, Integer konv4, Integer konv5, Integer konv6, Integer konv11, Integer konv14, Double sum) {
        this.konv1 = konv1;
        this.konv4 = konv4;
        this.konv5 = konv5;
        this.konv6 = konv6;
        this.konv11 = konv11;
        this.konv14 = konv14;
        this.sum = sum;
    }

}
