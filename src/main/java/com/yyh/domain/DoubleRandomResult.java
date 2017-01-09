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

    @Size(max = 1024)
    @Column(name = "task", length = 1024)
    private String task;

    @OneToOne
    @JoinColumn(unique = true)
    private Company company;

    @OneToMany(mappedBy = "doubleRandomResult")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> tasks = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "double_random_result_manager",
               joinColumns = @JoinColumn(name="double_random_results_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="managers_id", referencedColumnName="ID"))
    private Set<Manager> managers = new HashSet<>();

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

    public String getTask() {
        return task;
    }

    public DoubleRandomResult task(String task) {
        this.task = task;
        return this;
    }

    public void setTask(String task) {
        this.task = task;
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

    public Set<Task> getTasks() {
        return tasks;
    }

    public DoubleRandomResult tasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public DoubleRandomResult addTask(Task task) {
        tasks.add(task);
        task.setDoubleRandomResult(this);
        return this;
    }

    public DoubleRandomResult removeTask(Task task) {
        tasks.remove(task);
        task.setDoubleRandomResult(null);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
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
            ", task='" + task + "'" +
            '}';
    }
}
