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
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "company_name", length = 32, nullable = false)
    private String companyName;

    @NotNull
    @Size(max = 32)
    @Column(name = "company_register_id", length = 32, nullable = false)
    private String companyRegisterId;

    @NotNull
    @Size(max = 32)
    @Column(name = "company_capital", length = 32, nullable = false)
    private String companyCapital;

    @NotNull
    @Size(max = 256)
    @Column(name = "company_address", length = 256, nullable = false)
    private String companyAddress;

    @NotNull
    @Size(max = 256)
    @Column(name = "business_address", length = 256, nullable = false)
    private String businessAddress;

    @NotNull
    @Size(max = 4069)
    @Column(name = "business_scope", length = 4069, nullable = false)
    private String businessScope;

    @NotNull
    @Size(max = 32)
    @Column(name = "company_owner", length = 32, nullable = false)
    private String companyOwner;

    @NotNull
    @Size(max = 32)
    @Column(name = "company_date", length = 32, nullable = false)
    private String companyDate;

    @NotNull
    @Size(max = 1)
    @Column(name = "company_status", length = 1, nullable = false)
    private String companyStatus;

    @Size(max = 20)
    @Column(name = "company_phone", length = 20)
    private String companyPhone;

    @Max(value = 10000)
    @Column(name = "check_count")
    private Integer checkCount;

    @Size(max = 32)
    @Column(name = "last_check_date", length = 32)
    private String lastCheckDate;

    @Size(max = 4069)
    @Column(name = "abnormal_info", length = 4069)
    private String abnormalInfo;

    @Size(max = 4069)
    @Column(name = "description", length = 4069)
    private String description;

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DoubleRandomResult> doubleRandomResults = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Punish> punishes = new HashSet<>();

    @ManyToOne
    private CompanyType companyType;

    @ManyToOne
    private IndustryType industryType;

    @ManyToOne
    private LawenforceDepartment companySupervisory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Company companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyRegisterId() {
        return companyRegisterId;
    }

    public Company companyRegisterId(String companyRegisterId) {
        this.companyRegisterId = companyRegisterId;
        return this;
    }

    public void setCompanyRegisterId(String companyRegisterId) {
        this.companyRegisterId = companyRegisterId;
    }

    public String getCompanyCapital() {
        return companyCapital;
    }

    public Company companyCapital(String companyCapital) {
        this.companyCapital = companyCapital;
        return this;
    }

    public void setCompanyCapital(String companyCapital) {
        this.companyCapital = companyCapital;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public Company companyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
        return this;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public Company businessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
        return this;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public Company businessScope(String businessScope) {
        this.businessScope = businessScope;
        return this;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getCompanyOwner() {
        return companyOwner;
    }

    public Company companyOwner(String companyOwner) {
        this.companyOwner = companyOwner;
        return this;
    }

    public void setCompanyOwner(String companyOwner) {
        this.companyOwner = companyOwner;
    }

    public String getCompanyDate() {
        return companyDate;
    }

    public Company companyDate(String companyDate) {
        this.companyDate = companyDate;
        return this;
    }

    public void setCompanyDate(String companyDate) {
        this.companyDate = companyDate;
    }

    public String getCompanyStatus() {
        return companyStatus;
    }

    public Company companyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
        return this;
    }

    public void setCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public Company companyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
        return this;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public Integer getCheckCount() {
        return checkCount;
    }

    public Company checkCount(Integer checkCount) {
        this.checkCount = checkCount;
        return this;
    }

    public void setCheckCount(Integer checkCount) {
        this.checkCount = checkCount;
    }

    public String getLastCheckDate() {
        return lastCheckDate;
    }

    public Company lastCheckDate(String lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
        return this;
    }

    public void setLastCheckDate(String lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    public String getAbnormalInfo() {
        return abnormalInfo;
    }

    public Company abnormalInfo(String abnormalInfo) {
        this.abnormalInfo = abnormalInfo;
        return this;
    }

    public void setAbnormalInfo(String abnormalInfo) {
        this.abnormalInfo = abnormalInfo;
    }

    public String getDescription() {
        return description;
    }

    public Company description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<DoubleRandomResult> getDoubleRandomResults() {
        return doubleRandomResults;
    }

    public Company doubleRandomResults(Set<DoubleRandomResult> doubleRandomResults) {
        this.doubleRandomResults = doubleRandomResults;
        return this;
    }

    public Company addDoubleRandomResult(DoubleRandomResult doubleRandomResult) {
        doubleRandomResults.add(doubleRandomResult);
        doubleRandomResult.setCompany(this);
        return this;
    }

    public Company removeDoubleRandomResult(DoubleRandomResult doubleRandomResult) {
        doubleRandomResults.remove(doubleRandomResult);
        doubleRandomResult.setCompany(null);
        return this;
    }

    public void setDoubleRandomResults(Set<DoubleRandomResult> doubleRandomResults) {
        this.doubleRandomResults = doubleRandomResults;
    }

    public Set<Punish> getPunishes() {
        return punishes;
    }

    public Company punishes(Set<Punish> punishes) {
        this.punishes = punishes;
        return this;
    }

    public Company addPunish(Punish punish) {
        punishes.add(punish);
        punish.setCompany(this);
        return this;
    }

    public Company removePunish(Punish punish) {
        punishes.remove(punish);
        punish.setCompany(null);
        return this;
    }

    public void setPunishes(Set<Punish> punishes) {
        this.punishes = punishes;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }

    public Company companyType(CompanyType companyType) {
        this.companyType = companyType;
        return this;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }

    public IndustryType getIndustryType() {
        return industryType;
    }

    public Company industryType(IndustryType industryType) {
        this.industryType = industryType;
        return this;
    }

    public void setIndustryType(IndustryType industryType) {
        this.industryType = industryType;
    }

    public LawenforceDepartment getCompanySupervisory() {
        return companySupervisory;
    }

    public Company companySupervisory(LawenforceDepartment lawenforceDepartment) {
        this.companySupervisory = lawenforceDepartment;
        return this;
    }

    public void setCompanySupervisory(LawenforceDepartment lawenforceDepartment) {
        this.companySupervisory = lawenforceDepartment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Company company = (Company) o;
        if (company.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, company.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + id +
            ", companyName='" + companyName + "'" +
            ", companyRegisterId='" + companyRegisterId + "'" +
            ", companyCapital='" + companyCapital + "'" +
            ", companyAddress='" + companyAddress + "'" +
            ", businessAddress='" + businessAddress + "'" +
            ", businessScope='" + businessScope + "'" +
            ", companyOwner='" + companyOwner + "'" +
            ", companyDate='" + companyDate + "'" +
            ", companyStatus='" + companyStatus + "'" +
            ", companyPhone='" + companyPhone + "'" +
            ", checkCount='" + checkCount + "'" +
            ", lastCheckDate='" + lastCheckDate + "'" +
            ", abnormalInfo='" + abnormalInfo + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
