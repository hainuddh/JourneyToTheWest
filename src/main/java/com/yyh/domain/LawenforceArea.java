package com.yyh.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A LawenforceArea.
 */
@Entity
@Table(name = "lawenforce_area")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lawenforcearea")
public class LawenforceArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "area_name", length = 32, nullable = false)
    private String areaName;

    @OneToMany(mappedBy = "companyArea")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Company> companies = new HashSet<>();

    @ManyToMany(mappedBy = "managerLawenforceAreas")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Manager> managers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public LawenforceArea areaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public LawenforceArea companies(Set<Company> companies) {
        this.companies = companies;
        return this;
    }

    public LawenforceArea addCompany(Company company) {
        companies.add(company);
        company.setCompanyArea(this);
        return this;
    }

    public LawenforceArea removeCompany(Company company) {
        companies.remove(company);
        company.setCompanyArea(null);
        return this;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    public Set<Manager> getManagers() {
        return managers;
    }

    public LawenforceArea managers(Set<Manager> managers) {
        this.managers = managers;
        return this;
    }

    public LawenforceArea addManager(Manager manager) {
        managers.add(manager);
        manager.getManagerLawenforceAreas().add(this);
        return this;
    }

    public LawenforceArea removeManager(Manager manager) {
        managers.remove(manager);
        manager.getManagerLawenforceAreas().remove(this);
        return this;
    }

    public void setManagers(Set<Manager> managers) {
        this.managers = managers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LawenforceArea lawenforceArea = (LawenforceArea) o;
        if (lawenforceArea.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lawenforceArea.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LawenforceArea{" +
            "id=" + id +
            ", areaName='" + areaName + "'" +
            '}';
    }
}
