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
 * A Office.
 */
@Entity
@Table(name = "office")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "office")
public class Office implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "office_name", length = 32, nullable = false)
    private String officeName;

    @NotNull
    @Size(max = 2048)
    @Column(name = "office_duty", length = 2048, nullable = false)
    private String officeDuty;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "office_head_person",
               joinColumns = @JoinColumn(name="offices_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="head_people_id", referencedColumnName="ID"))
    private Set<HeadPerson> headPeople = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOfficeName() {
        return officeName;
    }

    public Office officeName(String officeName) {
        this.officeName = officeName;
        return this;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficeDuty() {
        return officeDuty;
    }

    public Office officeDuty(String officeDuty) {
        this.officeDuty = officeDuty;
        return this;
    }

    public void setOfficeDuty(String officeDuty) {
        this.officeDuty = officeDuty;
    }

    public Set<HeadPerson> getHeadPeople() {
        return headPeople;
    }

    public Office headPeople(Set<HeadPerson> headPeople) {
        this.headPeople = headPeople;
        return this;
    }

    public Office addHeadPerson(HeadPerson headPerson) {
        headPeople.add(headPerson);
        headPerson.getOffices().add(this);
        return this;
    }

    public Office removeHeadPerson(HeadPerson headPerson) {
        headPeople.remove(headPerson);
        headPerson.getOffices().remove(this);
        return this;
    }

    public void setHeadPeople(Set<HeadPerson> headPeople) {
        this.headPeople = headPeople;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Office office = (Office) o;
        if (office.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, office.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Office{" +
            "id=" + id +
            ", officeName='" + officeName + "'" +
            ", officeDuty='" + officeDuty + "'" +
            '}';
    }
}
