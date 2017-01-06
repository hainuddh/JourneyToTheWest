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
 * A LawenforceDepartment.
 */
@Entity
@Table(name = "lawenforce_department")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lawenforcedepartment")
public class LawenforceDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "department_name", length = 32, nullable = false)
    private String departmentName;

    @NotNull
    @Size(max = 32)
    @Column(name = "department_address", length = 32, nullable = false)
    private String departmentAddress;

    @OneToMany(mappedBy = "companySupervisory")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Company> companies = new HashSet<>();

    @OneToMany(mappedBy = "managerLawenforceDepartment")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Manager> managers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public LawenforceDepartment departmentName(String departmentName) {
        this.departmentName = departmentName;
        return this;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentAddress() {
        return departmentAddress;
    }

    public LawenforceDepartment departmentAddress(String departmentAddress) {
        this.departmentAddress = departmentAddress;
        return this;
    }

    public void setDepartmentAddress(String departmentAddress) {
        this.departmentAddress = departmentAddress;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public LawenforceDepartment companies(Set<Company> companies) {
        this.companies = companies;
        return this;
    }

    public LawenforceDepartment addCompany(Company company) {
        companies.add(company);
        company.setCompanySupervisory(this);
        return this;
    }

    public LawenforceDepartment removeCompany(Company company) {
        companies.remove(company);
        company.setCompanySupervisory(null);
        return this;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    public Set<Manager> getManagers() {
        return managers;
    }

    public LawenforceDepartment managers(Set<Manager> managers) {
        this.managers = managers;
        return this;
    }

    public LawenforceDepartment addManager(Manager manager) {
        managers.add(manager);
        manager.setManagerLawenforceDepartment(this);
        return this;
    }

    public LawenforceDepartment removeManager(Manager manager) {
        managers.remove(manager);
        manager.setManagerLawenforceDepartment(null);
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
        LawenforceDepartment lawenforceDepartment = (LawenforceDepartment) o;
        if (lawenforceDepartment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lawenforceDepartment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LawenforceDepartment{" +
            "id=" + id +
            ", departmentName='" + departmentName + "'" +
            ", departmentAddress='" + departmentAddress + "'" +
            '}';
    }
}
