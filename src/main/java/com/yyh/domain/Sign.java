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
 * A Sign.
 */
@Entity
@Table(name = "sign")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sign")
public class Sign implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "sign_name", length = 10, nullable = false)
    private String signName;

    @NotNull
    @Max(value = 100)
    @Column(name = "sign_config", nullable = false)
    private Integer signConfig;

    @NotNull
    @Size(max = 20)
    @Column(name = "sign_css", length = 20, nullable = false)
    private String signCss;

    @OneToMany(mappedBy = "sign")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DoubleRandomResult> doubleRandomResults = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSignName() {
        return signName;
    }

    public Sign signName(String signName) {
        this.signName = signName;
        return this;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public Integer getSignConfig() {
        return signConfig;
    }

    public Sign signConfig(Integer signConfig) {
        this.signConfig = signConfig;
        return this;
    }

    public void setSignConfig(Integer signConfig) {
        this.signConfig = signConfig;
    }

    public String getSignCss() {
        return signCss;
    }

    public Sign signCss(String signCss) {
        this.signCss = signCss;
        return this;
    }

    public void setSignCss(String signCss) {
        this.signCss = signCss;
    }

    public Set<DoubleRandomResult> getDoubleRandomResults() {
        return doubleRandomResults;
    }

    public Sign doubleRandomResults(Set<DoubleRandomResult> doubleRandomResults) {
        this.doubleRandomResults = doubleRandomResults;
        return this;
    }

    public Sign addDoubleRandomResult(DoubleRandomResult doubleRandomResult) {
        doubleRandomResults.add(doubleRandomResult);
        doubleRandomResult.setSign(this);
        return this;
    }

    public Sign removeDoubleRandomResult(DoubleRandomResult doubleRandomResult) {
        doubleRandomResults.remove(doubleRandomResult);
        doubleRandomResult.setSign(null);
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
        Sign sign = (Sign) o;
        if (sign.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sign.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sign{" +
            "id=" + id +
            ", signName='" + signName + "'" +
            ", signConfig='" + signConfig + "'" +
            ", signCss='" + signCss + "'" +
            '}';
    }
}
