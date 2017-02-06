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
 * A Manager.
 */
@Entity
@Table(name = "manager")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "manager")
public class Manager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "manager_name", length = 32, nullable = false)
    private String managerName;

    @Size(max = 32)
    @Column(name = "manager_hn_card", length = 32)
    private String managerHNCard;

    @NotNull
    @Size(max = 32)
    @Column(name = "manager_ic_card", length = 32, nullable = false)
    private String managerICCard;

    @NotNull
    @Size(max = 1)
    @Column(name = "manager_sex", length = 1, nullable = false)
    private String managerSex;

    @Size(max = 20)
    @Column(name = "manager_phone", length = 20)
    private String managerPhone;

    @Size(max = 1)
    @Column(name = "manager_flag", length = 1)
    private String managerFlag;

    @Max(value = 10000)
    @Column(name = "check_count")
    private Integer checkCount;

    @Size(max = 4069)
    @Column(name = "description", length = 4069)
    private String description;

    @OneToOne
    @JoinColumn(unique = true)
    private User managerUser;

    @ManyToOne
    private LawenforceDepartment managerLawenforceDepartment;

    @ManyToMany(mappedBy = "managers")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DoubleRandomResult> doubleRandomResults = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManagerName() {
        return managerName;
    }

    public Manager managerName(String managerName) {
        this.managerName = managerName;
        return this;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerHNCard() {
        return managerHNCard;
    }

    public Manager managerHNCard(String managerHNCard) {
        this.managerHNCard = managerHNCard;
        return this;
    }

    public void setManagerHNCard(String managerHNCard) {
        this.managerHNCard = managerHNCard;
    }

    public String getManagerICCard() {
        return managerICCard;
    }

    public Manager managerICCard(String managerICCard) {
        this.managerICCard = managerICCard;
        return this;
    }

    public void setManagerICCard(String managerICCard) {
        this.managerICCard = managerICCard;
    }

    public String getManagerSex() {
        return managerSex;
    }

    public Manager managerSex(String managerSex) {
        this.managerSex = managerSex;
        return this;
    }

    public void setManagerSex(String managerSex) {
        this.managerSex = managerSex;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public Manager managerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
        return this;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public String getManagerFlag() {
        return managerFlag;
    }

    public Manager managerFlag(String managerFlag) {
        this.managerFlag = managerFlag;
        return this;
    }

    public void setManagerFlag(String managerFlag) {
        this.managerFlag = managerFlag;
    }

    public Integer getCheckCount() {
        return checkCount;
    }

    public Manager checkCount(Integer checkCount) {
        this.checkCount = checkCount;
        return this;
    }

    public void setCheckCount(Integer checkCount) {
        this.checkCount = checkCount;
    }

    public String getDescription() {
        return description;
    }

    public Manager description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getManagerUser() {
        return managerUser;
    }

    public Manager managerUser(User user) {
        this.managerUser = user;
        return this;
    }

    public void setManagerUser(User user) {
        this.managerUser = user;
    }

    public LawenforceDepartment getManagerLawenforceDepartment() {
        return managerLawenforceDepartment;
    }

    public Manager managerLawenforceDepartment(LawenforceDepartment lawenforceDepartment) {
        this.managerLawenforceDepartment = lawenforceDepartment;
        return this;
    }

    public void setManagerLawenforceDepartment(LawenforceDepartment lawenforceDepartment) {
        this.managerLawenforceDepartment = lawenforceDepartment;
    }

    public Set<DoubleRandomResult> getDoubleRandomResults() {
        return doubleRandomResults;
    }

    public Manager doubleRandomResults(Set<DoubleRandomResult> doubleRandomResults) {
        this.doubleRandomResults = doubleRandomResults;
        return this;
    }

    public Manager addDoubleRandomResult(DoubleRandomResult doubleRandomResult) {
        doubleRandomResults.add(doubleRandomResult);
        doubleRandomResult.getManagers().add(this);
        return this;
    }

    public Manager removeDoubleRandomResult(DoubleRandomResult doubleRandomResult) {
        doubleRandomResults.remove(doubleRandomResult);
        doubleRandomResult.getManagers().remove(this);
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
        Manager manager = (Manager) o;
        if (manager.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, manager.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Manager{" +
            "id=" + id +
            ", managerName='" + managerName + "'" +
            ", managerHNCard='" + managerHNCard + "'" +
            ", managerICCard='" + managerICCard + "'" +
            ", managerSex='" + managerSex + "'" +
            ", managerPhone='" + managerPhone + "'" +
            ", managerFlag='" + managerFlag + "'" +
            ", checkCount='" + checkCount + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
