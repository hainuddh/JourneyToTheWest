package com.yyh.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
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
    @Size(max = 32)
    @Column(name = "company_phone", length = 32, nullable = false)
    private String companyPhone;

    @Max(value = 10000)
    @Column(name = "check_count")
    private Integer checkCount;

    @NotNull
    @Size(max = 32)
    @Column(name = "company_status", length = 32, nullable = false)
    private String companyStatus;

    @Size(max = 32)
    @Column(name = "last_check_date", length = 32)
    private String lastCheckDate;

    @Size(max = 4069)
    @Column(name = "abnormal_info", length = 4069)
    private String abnormalInfo;

    @Size(max = 4069)
    @Column(name = "description", length = 4069)
    private String description;

    @ManyToOne
    private CompanyType companyType;

    @ManyToOne
    private IndustryType industryType;

    @ManyToOne
    private LawenforceArea companyArea;

    @ManyToOne
    private LawenforceDepartment companySupervisory;

    @OneToOne(mappedBy = "company")
    @JsonIgnore
    private DoubleRandomResult doubleRandomResult;

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

    public LawenforceArea getCompanyArea() {
        return companyArea;
    }

    public Company companyArea(LawenforceArea lawenforceArea) {
        this.companyArea = lawenforceArea;
        return this;
    }

    public void setCompanyArea(LawenforceArea lawenforceArea) {
        this.companyArea = lawenforceArea;
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

    public DoubleRandomResult getDoubleRandomResult() {
        return doubleRandomResult;
    }

    public Company doubleRandomResult(DoubleRandomResult doubleRandomResult) {
        this.doubleRandomResult = doubleRandomResult;
        return this;
    }

    public void setDoubleRandomResult(DoubleRandomResult doubleRandomResult) {
        this.doubleRandomResult = doubleRandomResult;
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
            ", companyPhone='" + companyPhone + "'" +
            ", checkCount='" + checkCount + "'" +
            ", companyStatus='" + companyStatus + "'" +
            ", lastCheckDate='" + lastCheckDate + "'" +
            ", abnormalInfo='" + abnormalInfo + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
