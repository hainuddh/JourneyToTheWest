package com.yyh.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "task")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 1024)
    @Column(name = "task_content", length = 1024, nullable = false)
    private String taskContent;

    @ManyToOne
    private DoubleRandom doubleRandom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public Task taskContent(String taskContent) {
        this.taskContent = taskContent;
        return this;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public DoubleRandom getDoubleRandom() {
        return doubleRandom;
    }

    public Task doubleRandom(DoubleRandom doubleRandom) {
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
        Task task = (Task) o;
        if (task.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + id +
            ", taskContent='" + taskContent + "'" +
            '}';
    }
}
