package ru.pfr.model.umikbd;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "shablon")
public class Shablon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dokument", nullable = false)
    private Long id;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "dokument", columnDefinition = "LONGBLOB")
    private byte[] dokument;
    @Column(name = "name_dokument", length = 400)
    private String nameDokument;
    @Column(name = "name_file", length = 400)
    private String nameFile;

    public Shablon() {
    }

    public Shablon(byte[] dokument, String nameDokument, String nameFile) {
        this.dokument = dokument;
        this.nameDokument = nameDokument;
        this.nameFile = nameFile;
    }

}
