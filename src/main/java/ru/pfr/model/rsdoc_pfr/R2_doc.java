package ru.pfr.model.rsdoc_pfr;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "r2_doc")
public class R2_doc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "id_doc_type")
    private Long id_doc_type;
    @Column(name="id_doc_kind")
    private Long id_doc_kind;
    @Column(name="id_doc_status")
    private Long id_doc_status;
    @Column(name="id_author")
    private Long id_author;
    @Column(name="author_name")
    private String author_name;
    @Column(name="reg_pref")
    private String reg_pref;
    @Column(name="reg_number")
    private Long reg_number;
    @Column(name="reg_postf")
    private String reg_postf;
    @Column(name="reg_date")
    private Date reg_date;
    @Column(name="ref_doc_n")
    private String ref_doc_n;
    @Column(name="ref_doc_dt")
    private Date ref_doc_dt;
    @Column(name="doc_date")
    private Date doc_date;
    @Column(name="doc_no")
    private String doc_no;
    @Column(name="name")
    private String name;
    @Column(name="short_contents")
    private String short_contents;
    @Column(name="note")
    private String note;
    @Column(name="doc_version")
    private Long doc_version;
    @Column(name="actual_version")
    private Long actual_version;
    @Column(name="id_arch_volume")
    private Double id_arch_volume;
    //id_arch_volume numeric
    @Column(name="dt_pass_arch")
    private Date dt_pass_arch;
    @Column(name="n_list")
    private Long n_list;
    @Column(name="n_copies")
    private Long n_copies;
    @Column(name="dt_creation")
    private Date dt_creation;
    @Column(name="id_creator")
    private Long id_creator;
    @Column(name="account_num")
    private Long account_num;
    @Column(name="account_date")
    private Date account_date;
    @Column(name="dt_change")
    private Date dt_change;
    @Column(name="id_ref_doc")
    private Long id_ref_doc;
    @Column(name="id_doc_group")
    private Long id_doc_group;
    @Column(name="id_control_kind")
    private Long id_control_kind;
    @Column(name="id_security_type")
    private Long id_security_type;
    @Column(name="id_validity")
    private Long id_validity;
    @Column(name="id_urgency")
    private Long id_urgency;
    @Column(name="id_doc_category")
    private Long id_doc_category;
    @Column(name="id_sys_status")
    private Long id_sys_status;
    @Column(name="id_theme")
    private Long id_theme;
    @Column(name="dt_create")
    private Date dt_create;
    @Column(name="dt_edit")
    private Date dt_edit;
    @Column(name="id_editor")
    private Long id_editor;
    @Column(name="dt_delete")
    private Date dt_delete;
    @Column(name="dt_start")
    private Date dt_start;
    @Column(name="dt_expire")
    private Date dt_expire;
    @Column(name="id_arch_folder")
    private Long id_arch_folder;
    @Column(name="guid")
    private String guid;
    @Column(name="report_contents")
    private String report_contents;
    @Column(name="id_report_statistic")
    private Long id_report_statistic;
    @Column(name="code")
    private Long code;

    public R2_doc() {
    }

    public R2_doc(Long id_doc_type, Long id_doc_kind, Long id_doc_status, Long id_author, String author_name, String reg_pref, Long reg_number, String reg_postf, Date reg_date, String ref_doc_n, Date ref_doc_dt, Date doc_date, String doc_no, String name, String short_contents, String note, Long doc_version, Long actual_version, Double id_arch_volume, Date dt_pass_arch, Long n_list, Long n_copies, Date dt_creation, Long id_creator, Long account_num, Date account_date, Date dt_change, Long id_ref_doc, Long id_doc_group, Long id_control_kind, Long id_security_type, Long id_validity, Long id_urgency, Long id_doc_category, Long id_sys_status, Long id_theme, Date dt_create, Date dt_edit, Long id_editor, Date dt_delete, Date dt_start, Date dt_expire, Long id_arch_folder, String guid, String report_contents, Long id_report_statistic, Long code) {
        this.id_doc_type = id_doc_type;
        this.id_doc_kind = id_doc_kind;
        this.id_doc_status = id_doc_status;
        this.id_author = id_author;
        this.author_name = author_name;
        this.reg_pref = reg_pref;
        this.reg_number = reg_number;
        this.reg_postf = reg_postf;
        this.reg_date = reg_date;
        this.ref_doc_n = ref_doc_n;
        this.ref_doc_dt = ref_doc_dt;
        this.doc_date = doc_date;
        this.doc_no = doc_no;
        this.name = name;
        this.short_contents = short_contents;
        this.note = note;
        this.doc_version = doc_version;
        this.actual_version = actual_version;
        this.id_arch_volume = id_arch_volume;
        this.dt_pass_arch = dt_pass_arch;
        this.n_list = n_list;
        this.n_copies = n_copies;
        this.dt_creation = dt_creation;
        this.id_creator = id_creator;
        this.account_num = account_num;
        this.account_date = account_date;
        this.dt_change = dt_change;
        this.id_ref_doc = id_ref_doc;
        this.id_doc_group = id_doc_group;
        this.id_control_kind = id_control_kind;
        this.id_security_type = id_security_type;
        this.id_validity = id_validity;
        this.id_urgency = id_urgency;
        this.id_doc_category = id_doc_category;
        this.id_sys_status = id_sys_status;
        this.id_theme = id_theme;
        this.dt_create = dt_create;
        this.dt_edit = dt_edit;
        this.id_editor = id_editor;
        this.dt_delete = dt_delete;
        this.dt_start = dt_start;
        this.dt_expire = dt_expire;
        this.id_arch_folder = id_arch_folder;
        this.guid = guid;
        this.report_contents = report_contents;
        this.id_report_statistic = id_report_statistic;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_doc_type() {
        return id_doc_type;
    }

    public void setId_doc_type(Long id_doc_type) {
        this.id_doc_type = id_doc_type;
    }

    public Long getId_doc_kind() {
        return id_doc_kind;
    }

    public void setId_doc_kind(Long id_doc_kind) {
        this.id_doc_kind = id_doc_kind;
    }

    public Long getId_doc_status() {
        return id_doc_status;
    }

    public void setId_doc_status(Long id_doc_status) {
        this.id_doc_status = id_doc_status;
    }

    public Long getId_author() {
        return id_author;
    }

    public void setId_author(Long id_author) {
        this.id_author = id_author;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getReg_pref() {
        return reg_pref;
    }

    public void setReg_pref(String reg_pref) {
        this.reg_pref = reg_pref;
    }

    public Long getReg_number() {
        return reg_number;
    }

    public void setReg_number(Long reg_number) {
        this.reg_number = reg_number;
    }

    public String getReg_postf() {
        return reg_postf;
    }

    public void setReg_postf(String reg_postf) {
        this.reg_postf = reg_postf;
    }

    public Date getReg_date() {
        return reg_date;
    }

    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }

    public String getRef_doc_n() {
        return ref_doc_n;
    }

    public void setRef_doc_n(String ref_doc_n) {
        this.ref_doc_n = ref_doc_n;
    }

    public Date getRef_doc_dt() {
        return ref_doc_dt;
    }

    public void setRef_doc_dt(Date ref_doc_dt) {
        this.ref_doc_dt = ref_doc_dt;
    }

    public Date getDoc_date() {
        return doc_date;
    }

    public void setDoc_date(Date doc_date) {
        this.doc_date = doc_date;
    }

    public String getDoc_no() {
        return doc_no;
    }

    public void setDoc_no(String doc_no) {
        this.doc_no = doc_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_contents() {
        return short_contents;
    }

    public void setShort_contents(String short_contents) {
        this.short_contents = short_contents;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getDoc_version() {
        return doc_version;
    }

    public void setDoc_version(Long doc_version) {
        this.doc_version = doc_version;
    }

    public Long getActual_version() {
        return actual_version;
    }

    public void setActual_version(Long actual_version) {
        this.actual_version = actual_version;
    }

    public Double getId_arch_volume() {
        return id_arch_volume;
    }

    public void setId_arch_volume(Double id_arch_volume) {
        this.id_arch_volume = id_arch_volume;
    }

    public Date getDt_pass_arch() {
        return dt_pass_arch;
    }

    public void setDt_pass_arch(Date dt_pass_arch) {
        this.dt_pass_arch = dt_pass_arch;
    }

    public Long getN_list() {
        return n_list;
    }

    public void setN_list(Long n_list) {
        this.n_list = n_list;
    }

    public Long getN_copies() {
        return n_copies;
    }

    public void setN_copies(Long n_copies) {
        this.n_copies = n_copies;
    }

    public Date getDt_creation() {
        return dt_creation;
    }

    public void setDt_creation(Date dt_creation) {
        this.dt_creation = dt_creation;
    }

    public Long getId_creator() {
        return id_creator;
    }

    public void setId_creator(Long id_creator) {
        this.id_creator = id_creator;
    }

    public Long getAccount_num() {
        return account_num;
    }

    public void setAccount_num(Long account_num) {
        this.account_num = account_num;
    }

    public Date getAccount_date() {
        return account_date;
    }

    public void setAccount_date(Date account_date) {
        this.account_date = account_date;
    }

    public Date getDt_change() {
        return dt_change;
    }

    public void setDt_change(Date dt_change) {
        this.dt_change = dt_change;
    }

    public Long getId_ref_doc() {
        return id_ref_doc;
    }

    public void setId_ref_doc(Long id_ref_doc) {
        this.id_ref_doc = id_ref_doc;
    }

    public Long getId_doc_group() {
        return id_doc_group;
    }

    public void setId_doc_group(Long id_doc_group) {
        this.id_doc_group = id_doc_group;
    }

    public Long getId_control_kind() {
        return id_control_kind;
    }

    public void setId_control_kind(Long id_control_kind) {
        this.id_control_kind = id_control_kind;
    }

    public Long getId_security_type() {
        return id_security_type;
    }

    public void setId_security_type(Long id_security_type) {
        this.id_security_type = id_security_type;
    }

    public Long getId_validity() {
        return id_validity;
    }

    public void setId_validity(Long id_validity) {
        this.id_validity = id_validity;
    }

    public Long getId_urgency() {
        return id_urgency;
    }

    public void setId_urgency(Long id_urgency) {
        this.id_urgency = id_urgency;
    }

    public Long getId_doc_category() {
        return id_doc_category;
    }

    public void setId_doc_category(Long id_doc_category) {
        this.id_doc_category = id_doc_category;
    }

    public Long getId_sys_status() {
        return id_sys_status;
    }

    public void setId_sys_status(Long id_sys_status) {
        this.id_sys_status = id_sys_status;
    }

    public Long getId_theme() {
        return id_theme;
    }

    public void setId_theme(Long id_theme) {
        this.id_theme = id_theme;
    }

    public Date getDt_create() {
        return dt_create;
    }

    public void setDt_create(Date dt_create) {
        this.dt_create = dt_create;
    }

    public Date getDt_edit() {
        return dt_edit;
    }

    public void setDt_edit(Date dt_edit) {
        this.dt_edit = dt_edit;
    }

    public Long getId_editor() {
        return id_editor;
    }

    public void setId_editor(Long id_editor) {
        this.id_editor = id_editor;
    }

    public Date getDt_delete() {
        return dt_delete;
    }

    public void setDt_delete(Date dt_delete) {
        this.dt_delete = dt_delete;
    }

    public Date getDt_start() {
        return dt_start;
    }

    public void setDt_start(Date dt_start) {
        this.dt_start = dt_start;
    }

    public Date getDt_expire() {
        return dt_expire;
    }

    public void setDt_expire(Date dt_expire) {
        this.dt_expire = dt_expire;
    }

    public Long getId_arch_folder() {
        return id_arch_folder;
    }

    public void setId_arch_folder(Long id_arch_folder) {
        this.id_arch_folder = id_arch_folder;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getReport_contents() {
        return report_contents;
    }

    public void setReport_contents(String report_contents) {
        this.report_contents = report_contents;
    }

    public Long getId_report_statistic() {
        return id_report_statistic;
    }

    public void setId_report_statistic(Long id_report_statistic) {
        this.id_report_statistic = id_report_statistic;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }
}
