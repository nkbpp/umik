package ru.pfr.model.umikbd;



public class Reestr1Itog {

    private Integer konv1;
    private Integer konv4;
    private Integer konv5;
    private Integer konv6;
    private Integer konv11;
    private Integer konv14;
    private Double sum;

    public Reestr1Itog(Integer konv1, Integer konv4, Integer konv5, Integer konv6, Integer konv11, Integer konv14, Double sum) {
        this.konv1 = konv1;
        this.konv4 = konv4;
        this.konv5 = konv5;
        this.konv6 = konv6;
        this.konv11 = konv11;
        this.konv14 = konv14;
        this.sum = sum;
    }

    public Integer getKonv1() {
        return konv1;
    }

    public Integer getKonv4() {
        return konv4;
    }

    public Integer getKonv5() {
        return konv5;
    }

    public Integer getKonv6() {
        return konv6;
    }

    public Integer getKonv11() {
        return konv11;
    }

    public Integer getKonv14() {
        return konv14;
    }

    public Double getSum() {
        return sum;
    }
}
