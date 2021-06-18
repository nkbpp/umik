package ru.pfr.model.umikbd;

import javax.persistence.*;

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
    @Column(name = "name_dokument", nullable = true, length = 400)
    private String nameDokument;
    @Column(name = "name_file", nullable = true, length = 400)
    private String nameFile;

    public Shablon() {
    }

    public Shablon(byte[] dokument, String nameDokument, String nameFile) {
        this.dokument = dokument;
        this.nameDokument = nameDokument;
        this.nameFile = nameFile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getDokument() {
        return dokument;
    }

    public void setDokument(byte[] dokument) {
        this.dokument = dokument;
    }

    public String getNameDokument() {
        return nameDokument;
    }

    public void setNameDokument(String nameDokument) {
        this.nameDokument = nameDokument;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }
}
