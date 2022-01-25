/*
package ru.pfr.service.rsdoc_pfr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pfr.model.rsdoc_pfr.Oblagraj;
import ru.pfr.repo.rsdoc_pfr.OblagrajRepository;

import java.util.Date;
import java.util.List;

@Service
public class OblagrajService {

    @Autowired
    OblagrajRepository oblagrajRepository;

    public Oblagraj findById(Long id) {
        return oblagrajRepository.findById(id).get();
    }

    public Oblagraj findByRegnumber(Integer r, Date d1, Date d2) {
        return oblagrajRepository.findByRegnumber(r, d1, d2).get(0);
    }

    public Oblagraj findByRegnumber2(Integer r, Date d1, Date d2, String textorg1, String text_fio1) {
        List<Oblagraj> d = oblagrajRepository.findByRegnumber2(r, d1, d2);
        Oblagraj oblagraj = null;
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
        }

        return oblagraj;
    }

    public List<Oblagraj> findByRegnumber3(Integer r, Integer r2, Date d1, Date d2) {
        return oblagrajRepository.findByRegnumber3(r, r2, d1, d2);
    }

    public List<Oblagraj> findAll() {
        return oblagrajRepository.findAll();
    }

}
*/
