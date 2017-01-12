package com.yyh.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Law.
 */
@Entity
@Table(name = "law")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "law")
public class Law implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "law_name", length = 64, nullable = false)
    private String lawName;

    @NotNull
    @Size(max = 4096)
    @Column(name = "law_content", length = 4096, nullable = false)
    private String lawContent;

    @Size(max = 1024)
    @Column(name = "description", length = 1024)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLawName() {
        return lawName;
    }

    public Law lawName(String lawName) {
        this.lawName = lawName;
        return this;
    }

    public void setLawName(String lawName) {
        this.lawName = lawName;
    }

    public String getLawContent() {
        return lawContent;
    }

    public Law lawContent(String lawContent) {
        this.lawContent = lawContent;
        return this;
    }

    public void setLawContent(String lawContent) {
        this.lawContent = lawContent;
    }

    public String getDescription() {
        return description;
    }

    public Law description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Law law = (Law) o;
        if (law.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, law.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Law{" +
            "id=" + id +
            ", lawName='" + lawName + "'" +
            ", lawContent='" + lawContent + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
