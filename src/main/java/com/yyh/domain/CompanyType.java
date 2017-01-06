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
 * A CompanyType.
 */
@Entity
@Table(name = "company_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "companytype")
public class CompanyType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "type_name", length = 32, nullable = false)
    private String typeName;

    @OneToMany(mappedBy = "companyType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Company> companies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public CompanyType typeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public CompanyType companies(Set<Company> companies) {
        this.companies = companies;
        return this;
    }

    public CompanyType addCompany(Company company) {
        companies.add(company);
        company.setCompanyType(this);
        return this;
    }

    public CompanyType removeCompany(Company company) {
        companies.remove(company);
        company.setCompanyType(null);
        return this;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompanyType companyType = (CompanyType) o;
        if (companyType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, companyType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CompanyType{" +
            "id=" + id +
            ", typeName='" + typeName + "'" +
            '}';
    }
}
