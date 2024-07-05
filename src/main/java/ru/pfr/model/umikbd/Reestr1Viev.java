package ru.pfr.model.umikbd;

import lombok.Getter;
import lombok.Setter;
import ru.pfr.global.DateUtils;

import java.time.LocalDateTime;
import java.util.UUID;


@Setter
@Getter
public class Reestr1Viev {

    private UUID id;

    private LocalDateTime reg_date;

    private String reg_number;

    private String text_org;

    private String text_fio;

    private String name;

    private Spravkonv spravkonv;

    private Double sum;

    private Integer kol_vo;

    public Reestr1Viev(Bolgaria bolgaria) {
        this.id = UUID.randomUUID();
        this.reg_date = bolgaria.getReg_date();
        this.reg_number = bolgaria.getReg_number();
        this.text_org = bolgaria.getText_org();
        this.text_fio = "";
        this.name = bolgaria.getName();
        this.spravkonv = bolgaria.getSpravkonv();
        this.sum = bolgaria.getSum();
        this.kol_vo = bolgaria.getKol_vo();
    }

    public Reestr1Viev(PEDdeloproizvodstvo peDdeloproizvodstvo) {
        this.id = UUID.randomUUID();
        this.reg_date = peDdeloproizvodstvo.getReg_date();
        this.reg_number = peDdeloproizvodstvo.getReg_number();
        this.text_org = peDdeloproizvodstvo.getText_org();
        this.text_fio = "";
        this.name = peDdeloproizvodstvo.getName();
        this.spravkonv = peDdeloproizvodstvo.getSpravkonv();
        this.sum = peDdeloproizvodstvo.getSum();
        this.kol_vo = peDdeloproizvodstvo.getKol_vo();
    }

    public Reestr1Viev(PEDobragraj peDobragraj) {
        this.id = UUID.randomUUID();
        this.reg_date = peDobragraj.getReg_date();
        this.reg_number = peDobragraj.getReg_number();
        this.text_org = peDobragraj.getText_org();
        this.text_fio = peDobragraj.getText_fio();
        this.name = peDobragraj.getName();
        this.spravkonv = peDobragraj.getSpravkonv();
        this.sum = peDobragraj.getSum();
        this.kol_vo = peDobragraj.getKol_vo();
    }

    public Reestr1Viev(Inoe inoe) {
        this.id = UUID.randomUUID();
        this.reg_date = inoe.getReg_date();
        this.reg_number = "";
        this.text_org = inoe.getVidanykonv().getAdres();
        this.text_fio = "";
        this.name = "";
        this.spravkonv = inoe.getSpravkonv();
        this.sum = .0;
        this.kol_vo = inoe.getKol_vo();
    }

    public Reestr1Viev(UUID id, LocalDateTime reg_date, String reg_number, String text_org,
                       String text_fio, String name, Spravkonv spravkonv,
                       Double sum, Integer kol_vo) {
        this.id = id;
        this.reg_date = reg_date;
        this.reg_number = reg_number;
        this.text_org = text_org;
        this.text_fio = text_fio;
        this.name = name;
        this.spravkonv = spravkonv;
        this.sum = sum;
        this.kol_vo = kol_vo;
    }

    public String getAddr() {
        String s;
        if (text_org.isEmpty())
            s = text_fio;
        else s = text_org;
        return s;
    }

    public Double getsumob() {
        return this.kol_vo * this.sum;
    }

    public String getReg_datestr() {
        return DateUtils.formatToString(reg_date);
    }

    public Integer getId_konv1() {
        return spravkonv.getId().equals(1L) ? kol_vo : 0;
    }


    public Integer getId_konv4() {
        return spravkonv.getId().equals(4L) ? kol_vo : 0;
    }

    public Integer getId_konv5() {
        return spravkonv.getId().equals(5L) ? kol_vo : 0;
    }

    public Integer getId_konv6() {
        return spravkonv.getId().equals(6L) ? kol_vo : 0;
    }

    public Integer getId_konv7() {
        return spravkonv.getId().equals(7L) ? kol_vo : 0;
    }

    public Integer getId_konv22() {
        return spravkonv.getId().equals(22L) ? kol_vo : 0;
    }

    public Integer getId_konv11() {
        return spravkonv.getId().equals(11L) ? kol_vo : 0;
    }

    public Integer getId_konv14() {
        return spravkonv.getId().equals(14L) ? kol_vo : 0;
    }

}
