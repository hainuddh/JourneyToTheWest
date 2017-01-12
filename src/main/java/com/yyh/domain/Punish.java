package com.yyh.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Punish.
 */
@Entity
@Table(name = "punish")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "punish")
public class Punish implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "punish_id", length = 32, nullable = false)
    private String punishId;

    @NotNull
    @Size(max = 32)
    @Column(name = "person_name", length = 32, nullable = false)
    private String personName;

    @NotNull
    @Size(max = 32)
    @Column(name = "person_register_id", length = 32, nullable = false)
    private String personRegisterId;

    @NotNull
    @Size(max = 32)
    @Column(name = "unit_name", length = 32, nullable = false)
    private String unitName;

    @NotNull
    @Size(max = 32)
    @Column(name = "unit_register_id", length = 32, nullable = false)
    private String unitRegisterId;

    @NotNull
    @Size(max = 32)
    @Column(name = "unit_owner", length = 32, nullable = false)
    private String unitOwner;

    @NotNull
    @Size(max = 32)
    @Column(name = "break_law", length = 32, nullable = false)
    private String breakLaw;

    @NotNull
    @Size(max = 32)
    @Column(name = "punish_content", length = 32, nullable = false)
    private String punishContent;

    @NotNull
    @Size(max = 32)
    @Column(name = "punish_date", length = 32, nullable = false)
    private String punishDate;

    @OneToOne
    @JoinColumn(unique = true)
    private LawenforceDepartment department;

    @ManyToOne
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPunishId() {
        return punishId;
    }

    public Punish punishId(String punishId) {
        this.punishId = punishId;
        return this;
    }

    public void setPunishId(String punishId) {
        this.punishId = punishId;
    }

    public String getPersonName() {
        return personName;
    }

    public Punish personName(String personName) {
        this.personName = personName;
        return this;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonRegisterId() {
        return personRegisterId;
    }

    public Punish personRegisterId(String personRegisterId) {
        this.personRegisterId = personRegisterId;
        return this;
    }

    public void setPersonRegisterId(String personRegisterId) {
        this.personRegisterId = personRegisterId;
    }

    public String getUnitName() {
        return unitName;
    }

    public Punish unitName(String unitName) {
        this.unitName = unitName;
        return this;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitRegisterId() {
        return unitRegisterId;
    }

    public Punish unitRegisterId(String unitRegisterId) {
        this.unitRegisterId = unitRegisterId;
        return this;
    }

    public void setUnitRegisterId(String unitRegisterId) {
        this.unitRegisterId = unitRegisterId;
    }

    public String getUnitOwner() {
        return unitOwner;
    }

    public Punish unitOwner(String unitOwner) {
        this.unitOwner = unitOwner;
        return this;
    }

    public void setUnitOwner(String unitOwner) {
        this.unitOwner = unitOwner;
    }

    public String getBreakLaw() {
        return breakLaw;
    }

    public Punish breakLaw(String breakLaw) {
        this.breakLaw = breakLaw;
        return this;
    }

    public void setBreakLaw(String breakLaw) {
        this.breakLaw = breakLaw;
    }

    public String getPunishContent() {
        return punishContent;
    }

    public Punish punishContent(String punishContent) {
        this.punishContent = punishContent;
        return this;
    }

    public void setPunishContent(String punishContent) {
        this.punishContent = punishContent;
    }

    public String getPunishDate() {
        return punishDate;
    }

    public Punish punishDate(String punishDate) {
        this.punishDate = punishDate;
        return this;
    }

    public void setPunishDate(String punishDate) {
        this.punishDate = punishDate;
    }

    public LawenforceDepartment getDepartment() {
        return department;
    }

    public Punish department(LawenforceDepartment lawenforceDepartment) {
        this.department = lawenforceDepartment;
        return this;
    }

    public void setDepartment(LawenforceDepartment lawenforceDepartment) {
        this.department = lawenforceDepartment;
    }

    public Company getCompany() {
        return company;
    }

    public Punish company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Punish punish = (Punish) o;
        if (punish.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, punish.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Punish{" +
            "id=" + id +
            ", punishId='" + punishId + "'" +
            ", personName='" + personName + "'" +
            ", personRegisterId='" + personRegisterId + "'" +
            ", unitName='" + unitName + "'" +
            ", unitRegisterId='" + unitRegisterId + "'" +
            ", unitOwner='" + unitOwner + "'" +
            ", breakLaw='" + breakLaw + "'" +
            ", punishContent='" + punishContent + "'" +
            ", punishDate='" + punishDate + "'" +
            '}';
    }
}
