/*
package ru.pfr.service.rsdoc_pfr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pfr.model.rsdoc_pfr.Deloproizvodstvo;
import ru.pfr.model.rsdoc_pfr.Oblagraj;
import ru.pfr.repo.rsdoc_pfr.DeloproizvodstvoRepository;


import java.util.Date;
import java.util.List;

@Service
public class DeloproizvodstvoService {

    @Autowired
    DeloproizvodstvoRepository deloproizvodstvoRepository;


    public Deloproizvodstvo findById(Long id) {
        return deloproizvodstvoRepository.findById(id).get();
    }

*/
/*    public Deloproizvodstvo findByRegnumberAndRegdateBetween(String r, Date d1, Date d2) {
        return deloproizvodstvoRepository.findByRegnumberAndRegdateBetween(r, d1, d2).get();
    }*//*


    public Deloproizvodstvo findByRegnumber(Integer r, Date d1, Date d2) {
        return deloproizvodstvoRepository.findByRegnumber(r, d1, d2).get();
    }



*/
/*    public Deloproizvodstvo findByRegnumber2(Integer r, Date d1, Date d2, String textorg) {
        return deloproizvodstvoRepository.findByRegnumber2(r, d1, d2, textorg).get(0);
    }*//*


    public Deloproizvodstvo findByRegnumber4(Integer r, Date d1, Date d2, String textorg, String name) {
        List<Deloproizvodstvo> d = deloproizvodstvoRepository.findByRegnumber2(r, d1, d2, textorg, name);


        return d.get(0);*/
/*        Oblagraj oblagraj = null;
        if (d.size() <= 1) {
            oblagraj = d.get(0);
        } else {
            for (Oblagraj o :
                    d) {
                String org = o.getText_org()!=null?o.getText_org():"";
                String textorg = textorg1!=null?textorg1:"";

                String fio = o.getText_fio()!=null?o.getText_fio():"";
                String text_fio = text_fio1!=null?text_fio1:"";
                if (
                        org.equals(textorg)
                                &&
                                fio.equals(text_fio)
                ) {
                    oblagraj = o;
                }
            }
        }*//*

    }

    public List<Deloproizvodstvo> findByRegnumber3(Integer r, Integer r2, Date d1, Date d2) {
        return deloproizvodstvoRepository.findByRegnumber3(r, r2, d1, d2);
    }

    public List<Deloproizvodstvo> findAll() {
        return deloproizvodstvoRepository.findAll();
    }

}
*/
