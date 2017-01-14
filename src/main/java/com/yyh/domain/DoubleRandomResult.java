package com.yyh.domain;

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
 * A DoubleRandomResult.
 */
@Entity
@Table(name = "double_random_result")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "doublerandomresult")
public class DoubleRandomResult implements Serializable {

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
    @Column(name = "people", length = 32, nullable = false)
    private String people;

    @NotNull
    @Size(max = 32)
    @Column(name = "department", length = 32, nullable = false)
    private String department;

    @Size(max = 64)
    @Column(name = "result", length = 64)
    private String result;

    @Size(max = 64)
    @Column(name = "result_deal", length = 64)
    private String resultDeal;

    @Size(max = 64)
    @Column(name = "result_status", length = 64)
    private String resultStatus;

    @Size(max = 64)
    @Column(name = "check_date", length = 64)
    private String checkDate;

    @Size(max = 64)
    @Column(name = "finish_date", length = 64)
    private String finishDate;

    @OneToOne
    @JoinColumn(unique = true)
    private Lawenforcement lawenforcement;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "double_random_result_manager",
               joinColumns = @JoinColumn(name="double_random_results_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="managers_id", referencedColumnName="ID"))
    private Set<Manager> managers = new HashSet<>();

    @ManyToOne
    private Sign sign;

    @ManyToOne
    private Company company;

    @ManyToOne
    private DoubleRandom doubleRandom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public DoubleRandomResult companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyRegisterId() {
        return companyRegisterId;
    }

    public DoubleRandomResult companyRegisterId(String companyRegisterId) {
        this.companyRegisterId = companyRegisterId;
        return this;
    }

    public void setCompanyRegisterId(String companyRegisterId) {
        this.companyRegisterId = companyRegisterId;
    }

    public String getPeople() {
        return people;
    }

    public DoubleRandomResult people(String people) {
        this.people = people;
        return this;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getDepartment() {
        return department;
    }

    public DoubleRandomResult department(String department) {
        this.department = department;
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getResult() {
        return result;
    }

    public DoubleRandomResult result(String result) {
        this.result = result;
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultDeal() {
        return resultDeal;
    }

    public DoubleRandomResult resultDeal(String resultDeal) {
        this.resultDeal = resultDeal;
        return this;
    }

    public void setResultDeal(String resultDeal) {
        this.resultDeal = resultDeal;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public DoubleRandomResult resultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
        return this;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public DoubleRandomResult checkDate(String checkDate) {
        this.checkDate = checkDate;
        return this;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public DoubleRandomResult finishDate(String finishDate) {
        this.finishDate = finishDate;
        return this;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public Lawenforcement getLawenforcement() {
        return lawenforcement;
    }

    public DoubleRandomResult lawenforcement(Lawenforcement lawenforcement) {
        this.lawenforcement = lawenforcement;
        return this;
    }

    public void setLawenforcement(Lawenforcement lawenforcement) {
        this.lawenforcement = lawenforcement;
    }

    public Set<Manager> getManagers() {
        return managers;
    }

    public DoubleRandomResult managers(Set<Manager> managers) {
        this.managers = managers;
        return this;
    }

    public DoubleRandomResult addManager(Manager manager) {
        managers.add(manager);
        manager.getDoubleRandomResults().add(this);
        return this;
    }

    public DoubleRandomResult removeManager(Manager manager) {
        managers.remove(manager);
        manager.getDoubleRandomResults().remove(this);
        return this;
    }

    public void setManagers(Set<Manager> managers) {
        this.managers = managers;
    }

    public Sign getSign() {
        return sign;
    }

    public DoubleRandomResult sign(Sign sign) {
        this.sign = sign;
        return this;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public Company getCompany() {
        return company;
    }

    public DoubleRandomResult company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public DoubleRandom getDoubleRandom() {
        return doubleRandom;
    }

    public DoubleRandomResult doubleRandom(DoubleRandom doubleRandom) {
        this.doubleRandom = doubleRandom;
        return this;
    }

    public void setDoubleRandom(DoubleRandom doubleRandom) {
        this.doubleRandom = doubleRandom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DoubleRandomResult doubleRandomResult = (DoubleRandomResult) o;
        if (doubleRandomResult.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, doubleRandomResult.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DoubleRandomResult{" +
            "id=" + id +
            ", companyName='" + companyName + "'" +
            ", companyRegisterId='" + companyRegisterId + "'" +
            ", people='" + people + "'" +
            ", department='" + department + "'" +
            ", result='" + result + "'" +
            ", resultDeal='" + resultDeal + "'" +
            ", resultStatus='" + resultStatus + "'" +
            ", checkDate='" + checkDate + "'" +
            ", finishDate='" + finishDate + "'" +
            '}';
    }
}
