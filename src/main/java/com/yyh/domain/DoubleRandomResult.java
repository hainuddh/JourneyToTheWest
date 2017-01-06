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

    @ManyToOne
    private DoubleRandom doubleRandom;

    @ManyToMany(mappedBy = "doubleRandomResults")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Manager> managers = new HashSet<>();

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
