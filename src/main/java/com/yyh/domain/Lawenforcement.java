package com.yyh.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Lawenforcement.
 */
@Entity
@Table(name = "lawenforcement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lawenforcement")
public class Lawenforcement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "enforcement_name", length = 32, nullable = false)
    private String enforcementName;

    @OneToOne
    @JoinColumn(unique = true)
    private Punish punish;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnforcementName() {
        return enforcementName;
    }

    public Lawenforcement enforcementName(String enforcementName) {
        this.enforcementName = enforcementName;
        return this;
    }

    public void setEnforcementName(String enforcementName) {
        this.enforcementName = enforcementName;
    }

    public Punish getPunish() {
        return punish;
    }

    public Lawenforcement punish(Punish punish) {
        this.punish = punish;
        return this;
    }

    public void setPunish(Punish punish) {
        this.punish = punish;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lawenforcement lawenforcement = (Lawenforcement) o;
        if (lawenforcement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lawenforcement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lawenforcement{" +
            "id=" + id +
            ", enforcementName='" + enforcementName + "'" +
            '}';
    }
}
