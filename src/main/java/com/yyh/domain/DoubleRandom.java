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
 * A DoubleRandom.
 */
@Entity
@Table(name = "double_random")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "doublerandom")
public class DoubleRandom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "double_random_name", length = 32, nullable = false)
    private String doubleRandomName;

    @NotNull
    @Size(max = 32)
    @Column(name = "double_random_date", length = 32, nullable = false)
    private String doubleRandomDate;

    @NotNull
    @Size(max = 32)
    @Column(name = "double_random_notary", length = 32, nullable = false)
    private String doubleRandomNotary;

    @NotNull
    @Size(max = 64)
    @Column(name = "double_random_company_name", length = 64, nullable = false)
    private String doubleRandomCompanyName;

    @NotNull
    @Size(max = 64)
    @Column(name = "double_random_company_area", length = 64, nullable = false)
    private String doubleRandomCompanyArea;

    @NotNull
    @Size(max = 64)
    @Column(name = "double_random_company_supervisory", length = 64, nullable = false)
    private String doubleRandomCompanySupervisory;

    @NotNull
    @Size(max = 64)
    @Column(name = "double_random_company_type", length = 64, nullable = false)
    private String doubleRandomCompanyType;

    @NotNull
    @Size(max = 64)
    @Column(name = "double_random_company_industry_type", length = 64, nullable = false)
    private String doubleRandomCompanyIndustryType;

    @NotNull
    @Size(max = 64)
    @Column(name = "double_random_company_ratio", length = 64, nullable = false)
    private String doubleRandomCompanyRatio;

    @NotNull
    @Size(max = 64)
    @Column(name = "double_random_manager_name", length = 64, nullable = false)
    private String doubleRandomManagerName;

    @NotNull
    @Size(max = 64)
    @Column(name = "double_random_manager_number", length = 64, nullable = false)
    private String doubleRandomManagerNumber;

    @NotNull
    @Size(max = 64)
    @Column(name = "double_random_manager_department", length = 64, nullable = false)
    private String doubleRandomManagerDepartment;

    @NotNull
    @Size(max = 64)
    @Column(name = "double_random_manager_ratio", length = 64, nullable = false)
    private String doubleRandomManagerRatio;

    @OneToMany(mappedBy = "doubleRandom")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "doubleRandom")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DoubleRandomResult> doubleRandomResults = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDoubleRandomName() {
        return doubleRandomName;
    }

    public DoubleRandom doubleRandomName(String doubleRandomName) {
        this.doubleRandomName = doubleRandomName;
        return this;
    }

    public void setDoubleRandomName(String doubleRandomName) {
        this.doubleRandomName = doubleRandomName;
    }

    public String getDoubleRandomDate() {
        return doubleRandomDate;
    }

    public DoubleRandom doubleRandomDate(String doubleRandomDate) {
        this.doubleRandomDate = doubleRandomDate;
        return this;
    }

    public void setDoubleRandomDate(String doubleRandomDate) {
        this.doubleRandomDate = doubleRandomDate;
    }

    public String getDoubleRandomNotary() {
        return doubleRandomNotary;
    }

    public DoubleRandom doubleRandomNotary(String doubleRandomNotary) {
        this.doubleRandomNotary = doubleRandomNotary;
        return this;
    }

    public void setDoubleRandomNotary(String doubleRandomNotary) {
        this.doubleRandomNotary = doubleRandomNotary;
    }

    public String getDoubleRandomCompanyName() {
        return doubleRandomCompanyName;
    }

    public DoubleRandom doubleRandomCompanyName(String doubleRandomCompanyName) {
        this.doubleRandomCompanyName = doubleRandomCompanyName;
        return this;
    }

    public void setDoubleRandomCompanyName(String doubleRandomCompanyName) {
        this.doubleRandomCompanyName = doubleRandomCompanyName;
    }

    public String getDoubleRandomCompanyArea() {
        return doubleRandomCompanyArea;
    }

    public DoubleRandom doubleRandomCompanyArea(String doubleRandomCompanyArea) {
        this.doubleRandomCompanyArea = doubleRandomCompanyArea;
        return this;
    }

    public void setDoubleRandomCompanyArea(String doubleRandomCompanyArea) {
        this.doubleRandomCompanyArea = doubleRandomCompanyArea;
    }

    public String getDoubleRandomCompanySupervisory() {
        return doubleRandomCompanySupervisory;
    }

    public DoubleRandom doubleRandomCompanySupervisory(String doubleRandomCompanySupervisory) {
        this.doubleRandomCompanySupervisory = doubleRandomCompanySupervisory;
        return this;
    }

    public void setDoubleRandomCompanySupervisory(String doubleRandomCompanySupervisory) {
        this.doubleRandomCompanySupervisory = doubleRandomCompanySupervisory;
    }

    public String getDoubleRandomCompanyType() {
        return doubleRandomCompanyType;
    }

    public DoubleRandom doubleRandomCompanyType(String doubleRandomCompanyType) {
        this.doubleRandomCompanyType = doubleRandomCompanyType;
        return this;
    }

    public void setDoubleRandomCompanyType(String doubleRandomCompanyType) {
        this.doubleRandomCompanyType = doubleRandomCompanyType;
    }

    public String getDoubleRandomCompanyIndustryType() {
        return doubleRandomCompanyIndustryType;
    }

    public DoubleRandom doubleRandomCompanyIndustryType(String doubleRandomCompanyIndustryType) {
        this.doubleRandomCompanyIndustryType = doubleRandomCompanyIndustryType;
        return this;
    }

    public void setDoubleRandomCompanyIndustryType(String doubleRandomCompanyIndustryType) {
        this.doubleRandomCompanyIndustryType = doubleRandomCompanyIndustryType;
    }

    public String getDoubleRandomCompanyRatio() {
        return doubleRandomCompanyRatio;
    }

    public DoubleRandom doubleRandomCompanyRatio(String doubleRandomCompanyRatio) {
        this.doubleRandomCompanyRatio = doubleRandomCompanyRatio;
        return this;
    }

    public void setDoubleRandomCompanyRatio(String doubleRandomCompanyRatio) {
        this.doubleRandomCompanyRatio = doubleRandomCompanyRatio;
    }

    public String getDoubleRandomManagerName() {
        return doubleRandomManagerName;
    }

    public DoubleRandom doubleRandomManagerName(String doubleRandomManagerName) {
        this.doubleRandomManagerName = doubleRandomManagerName;
        return this;
    }

    public void setDoubleRandomManagerName(String doubleRandomManagerName) {
        this.doubleRandomManagerName = doubleRandomManagerName;
    }

    public String getDoubleRandomManagerNumber() {
        return doubleRandomManagerNumber;
    }

    public DoubleRandom doubleRandomManagerNumber(String doubleRandomManagerNumber) {
        this.doubleRandomManagerNumber = doubleRandomManagerNumber;
        return this;
    }

    public void setDoubleRandomManagerNumber(String doubleRandomManagerNumber) {
        this.doubleRandomManagerNumber = doubleRandomManagerNumber;
    }

    public String getDoubleRandomManagerDepartment() {
        return doubleRandomManagerDepartment;
    }

    public DoubleRandom doubleRandomManagerDepartment(String doubleRandomManagerDepartment) {
        this.doubleRandomManagerDepartment = doubleRandomManagerDepartment;
        return this;
    }

    public void setDoubleRandomManagerDepartment(String doubleRandomManagerDepartment) {
        this.doubleRandomManagerDepartment = doubleRandomManagerDepartment;
    }

    public String getDoubleRandomManagerRatio() {
        return doubleRandomManagerRatio;
    }

    public DoubleRandom doubleRandomManagerRatio(String doubleRandomManagerRatio) {
        this.doubleRandomManagerRatio = doubleRandomManagerRatio;
        return this;
    }

    public void setDoubleRandomManagerRatio(String doubleRandomManagerRatio) {
        this.doubleRandomManagerRatio = doubleRandomManagerRatio;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public DoubleRandom tasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public DoubleRandom addTask(Task task) {
        tasks.add(task);
        task.setDoubleRandom(this);
        return this;
    }

    public DoubleRandom removeTask(Task task) {
        tasks.remove(task);
        task.setDoubleRandom(null);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<DoubleRandomResult> getDoubleRandomResults() {
        return doubleRandomResults;
    }

    public DoubleRandom doubleRandomResults(Set<DoubleRandomResult> doubleRandomResults) {
        this.doubleRandomResults = doubleRandomResults;
        return this;
    }

    public DoubleRandom addDoubleRandomResult(DoubleRandomResult doubleRandomResult) {
        doubleRandomResults.add(doubleRandomResult);
        doubleRandomResult.setDoubleRandom(this);
        return this;
    }

    public DoubleRandom removeDoubleRandomResult(DoubleRandomResult doubleRandomResult) {
        doubleRandomResults.remove(doubleRandomResult);
        doubleRandomResult.setDoubleRandom(null);
        return this;
    }

    public void setDoubleRandomResults(Set<DoubleRandomResult> doubleRandomResults) {
        this.doubleRandomResults = doubleRandomResults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DoubleRandom doubleRandom = (DoubleRandom) o;
        if (doubleRandom.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, doubleRandom.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DoubleRandom{" +
            "id=" + id +
            ", doubleRandomName='" + doubleRandomName + "'" +
            ", doubleRandomDate='" + doubleRandomDate + "'" +
            ", doubleRandomNotary='" + doubleRandomNotary + "'" +
            ", doubleRandomCompanyName='" + doubleRandomCompanyName + "'" +
            ", doubleRandomCompanyArea='" + doubleRandomCompanyArea + "'" +
            ", doubleRandomCompanySupervisory='" + doubleRandomCompanySupervisory + "'" +
            ", doubleRandomCompanyType='" + doubleRandomCompanyType + "'" +
            ", doubleRandomCompanyIndustryType='" + doubleRandomCompanyIndustryType + "'" +
            ", doubleRandomCompanyRatio='" + doubleRandomCompanyRatio + "'" +
            ", doubleRandomManagerName='" + doubleRandomManagerName + "'" +
            ", doubleRandomManagerNumber='" + doubleRandomManagerNumber + "'" +
            ", doubleRandomManagerDepartment='" + doubleRandomManagerDepartment + "'" +
            ", doubleRandomManagerRatio='" + doubleRandomManagerRatio + "'" +
            '}';
    }
}
