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
 * A HeadPerson.
 */
@Entity
@Table(name = "head_person")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "headperson")
public class HeadPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @NotNull
    @Size(max = 32)
    @Column(name = "job", length = 32, nullable = false)
    private String job;

    @NotNull
    @Size(max = 32)
    @Column(name = "email", length = 32, nullable = false)
    private String email;

    @NotNull
    @Size(max = 32)
    @Column(name = "phone", length = 32, nullable = false)
    private String phone;

    @ManyToMany(mappedBy = "headPeople")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Office> offices = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public HeadPerson name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public HeadPerson job(String job) {
        this.job = job;
        return this;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getEmail() {
        return email;
    }

    public HeadPerson email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public HeadPerson phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Office> getOffices() {
        return offices;
    }

    public HeadPerson offices(Set<Office> offices) {
        this.offices = offices;
        return this;
    }

    public HeadPerson addOffice(Office office) {
        offices.add(office);
        office.getHeadPeople().add(this);
        return this;
    }

    public HeadPerson removeOffice(Office office) {
        offices.remove(office);
        office.getHeadPeople().remove(this);
        return this;
    }

    public void setOffices(Set<Office> offices) {
        this.offices = offices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HeadPerson headPerson = (HeadPerson) o;
        if (headPerson.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, headPerson.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HeadPerson{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", job='" + job + "'" +
            ", email='" + email + "'" +
            ", phone='" + phone + "'" +
            '}';
    }
}
