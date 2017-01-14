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
    @Column(name = "double_random_date", length = 32, nullable = false)
    private String doubleRandomDate;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    @Column(name = "double_random_company_ratio", nullable = false)
    private Integer doubleRandomCompanyRatio;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    @Column(name = "double_random_manager_ratio", nullable = false)
    private Integer doubleRandomManagerRatio;

    @Max(value = 10)
    @Column(name = "double_random_manager_number")
    private Integer doubleRandomManagerNumber;

    @Max(value = 100000)
    @Column(name = "double_random_company_count")
    private Integer doubleRandomCompanyCount;

    @Size(max = 32)
    @Column(name = "double_random_notary", length = 32)
    private String doubleRandomNotary;

    @Size(max = 2048)
    @Column(name = "double_random_task_content", length = 2048)
    private String doubleRandomTaskContent;

    @Size(max = 64)
    @Column(name = "double_random_company_name", length = 64)
    private String doubleRandomCompanyName;

    @Size(max = 64)
    @Column(name = "double_random_company_area", length = 64)
    private String doubleRandomCompanyArea;

    @Size(max = 64)
    @Column(name = "double_random_company_supervisory", length = 64)
    private String doubleRandomCompanySupervisory;

    @Size(max = 64)
    @Column(name = "double_random_company_type", length = 64)
    private String doubleRandomCompanyType;

    @Size(max = 64)
    @Column(name = "double_random_company_industry_type", length = 64)
    private String doubleRandomCompanyIndustryType;

    @Size(max = 64)
    @Column(name = "double_random_manager_name", length = 64)
    private String doubleRandomManagerName;

    @Size(max = 64)
    @Column(name = "double_random_manager_department", length = 64)
    private String doubleRandomManagerDepartment;

    @Size(max = 1024)
    @Column(name = "description", length = 1024)
    private String description;

    @OneToMany(mappedBy = "doubleRandom")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DoubleRandomResult> doubleRandomResults = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "double_random_task",
               joinColumns = @JoinColumn(name="double_randoms_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="tasks_id", referencedColumnName="ID"))
    private Set<Task> tasks = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getDoubleRandomCompanyRatio() {
        return doubleRandomCompanyRatio;
    }

    public DoubleRandom doubleRandomCompanyRatio(Integer doubleRandomCompanyRatio) {
        this.doubleRandomCompanyRatio = doubleRandomCompanyRatio;
        return this;
    }

    public void setDoubleRandomCompanyRatio(Integer doubleRandomCompanyRatio) {
        this.doubleRandomCompanyRatio = doubleRandomCompanyRatio;
    }

    public Integer getDoubleRandomManagerRatio() {
        return doubleRandomManagerRatio;
    }

    public DoubleRandom doubleRandomManagerRatio(Integer doubleRandomManagerRatio) {
        this.doubleRandomManagerRatio = doubleRandomManagerRatio;
        return this;
    }

    public void setDoubleRandomManagerRatio(Integer doubleRandomManagerRatio) {
        this.doubleRandomManagerRatio = doubleRandomManagerRatio;
    }

    public Integer getDoubleRandomManagerNumber() {
        return doubleRandomManagerNumber;
    }

    public DoubleRandom doubleRandomManagerNumber(Integer doubleRandomManagerNumber) {
        this.doubleRandomManagerNumber = doubleRandomManagerNumber;
        return this;
    }

    public void setDoubleRandomManagerNumber(Integer doubleRandomManagerNumber) {
        this.doubleRandomManagerNumber = doubleRandomManagerNumber;
    }

    public Integer getDoubleRandomCompanyCount() {
        return doubleRandomCompanyCount;
    }

    public DoubleRandom doubleRandomCompanyCount(Integer doubleRandomCompanyCount) {
        this.doubleRandomCompanyCount = doubleRandomCompanyCount;
        return this;
    }

    public void setDoubleRandomCompanyCount(Integer doubleRandomCompanyCount) {
        this.doubleRandomCompanyCount = doubleRandomCompanyCount;
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

    public String getDoubleRandomTaskContent() {
        return doubleRandomTaskContent;
    }

    public DoubleRandom doubleRandomTaskContent(String doubleRandomTaskContent) {
        this.doubleRandomTaskContent = doubleRandomTaskContent;
        return this;
    }

    public void setDoubleRandomTaskContent(String doubleRandomTaskContent) {
        this.doubleRandomTaskContent = doubleRandomTaskContent;
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

    public String getDescription() {
        return description;
    }

    public DoubleRandom description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Set<Task> getTasks() {
        return tasks;
    }

    public DoubleRandom tasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public DoubleRandom addTask(Task task) {
        tasks.add(task);
        return this;
    }

    public DoubleRandom removeTask(Task task) {
        tasks.remove(task);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
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
            ", doubleRandomDate='" + doubleRandomDate + "'" +
            ", doubleRandomCompanyRatio='" + doubleRandomCompanyRatio + "'" +
            ", doubleRandomManagerRatio='" + doubleRandomManagerRatio + "'" +
            ", doubleRandomManagerNumber='" + doubleRandomManagerNumber + "'" +
            ", doubleRandomCompanyCount='" + doubleRandomCompanyCount + "'" +
            ", doubleRandomNotary='" + doubleRandomNotary + "'" +
            ", doubleRandomTaskContent='" + doubleRandomTaskContent + "'" +
            ", doubleRandomCompanyName='" + doubleRandomCompanyName + "'" +
            ", doubleRandomCompanyArea='" + doubleRandomCompanyArea + "'" +
            ", doubleRandomCompanySupervisory='" + doubleRandomCompanySupervisory + "'" +
            ", doubleRandomCompanyType='" + doubleRandomCompanyType + "'" +
            ", doubleRandomCompanyIndustryType='" + doubleRandomCompanyIndustryType + "'" +
            ", doubleRandomManagerName='" + doubleRandomManagerName + "'" +
            ", doubleRandomManagerDepartment='" + doubleRandomManagerDepartment + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
