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
    @Column(name = "manager_id", length = 32, nullable = false)
    private String managerId;

    @NotNull
    @Size(max = 32)
    @Column(name = "manager_name", length = 32, nullable = false)
    private String managerName;

    @NotNull
    @Size(max = 32)
    @Column(name = "manager_card_id", length = 32, nullable = false)
    private String managerCardId;

    @NotNull
    @Size(max = 64)
    @Column(name = "manager_card_type", length = 64, nullable = false)
    private String managerCardType;

    @NotNull
    @Size(max = 1)
    @Column(name = "manager_sex", length = 1, nullable = false)
    private String managerSex;

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

    public String getManagerId() {
        return managerId;
    }

    public Manager managerId(String managerId) {
        this.managerId = managerId;
        return this;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
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

    public String getManagerCardId() {
        return managerCardId;
    }

    public Manager managerCardId(String managerCardId) {
        this.managerCardId = managerCardId;
        return this;
    }

    public void setManagerCardId(String managerCardId) {
        this.managerCardId = managerCardId;
    }

    public String getManagerCardType() {
        return managerCardType;
    }

    public Manager managerCardType(String managerCardType) {
        this.managerCardType = managerCardType;
        return this;
    }

    public void setManagerCardType(String managerCardType) {
        this.managerCardType = managerCardType;
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
            ", managerId='" + managerId + "'" +
            ", managerName='" + managerName + "'" +
            ", managerCardId='" + managerCardId + "'" +
            ", managerCardType='" + managerCardType + "'" +
            ", managerSex='" + managerSex + "'" +
            ", managerFlag='" + managerFlag + "'" +
            ", checkCount='" + checkCount + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
