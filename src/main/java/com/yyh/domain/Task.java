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
    @Size(max = 64)
    @Column(name = "task_name", length = 64, nullable = false)
    private String taskName;

    @NotNull
    @Size(max = 256)
    @Column(name = "task_check_department", length = 256, nullable = false)
    private String taskCheckDepartment;

    @NotNull
    @Size(max = 1024)
    @Column(name = "task_content", length = 1024, nullable = false)
    private String taskContent;

    @Size(max = 1024)
    @Column(name = "law_content", length = 1024)
    private String lawContent;

    @Size(max = 1024)
    @Column(name = "description", length = 1024)
    private String description;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "task_law",
               joinColumns = @JoinColumn(name="tasks_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="laws_id", referencedColumnName="ID"))
    private Set<Law> laws = new HashSet<>();

    @ManyToOne
    private TaskProject taskProject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public Task taskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskCheckDepartment() {
        return taskCheckDepartment;
    }

    public Task taskCheckDepartment(String taskCheckDepartment) {
        this.taskCheckDepartment = taskCheckDepartment;
        return this;
    }

    public void setTaskCheckDepartment(String taskCheckDepartment) {
        this.taskCheckDepartment = taskCheckDepartment;
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

    public String getLawContent() {
        return lawContent;
    }

    public Task lawContent(String lawContent) {
        this.lawContent = lawContent;
        return this;
    }

    public void setLawContent(String lawContent) {
        this.lawContent = lawContent;
    }

    public String getDescription() {
        return description;
    }

    public Task description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Law> getLaws() {
        return laws;
    }

    public Task laws(Set<Law> laws) {
        this.laws = laws;
        return this;
    }

    public Task addLaw(Law law) {
        laws.add(law);
        return this;
    }

    public Task removeLaw(Law law) {
        laws.remove(law);
        return this;
    }

    public void setLaws(Set<Law> laws) {
        this.laws = laws;
    }

    public TaskProject getTaskProject() {
        return taskProject;
    }

    public Task taskProject(TaskProject taskProject) {
        this.taskProject = taskProject;
        return this;
    }

    public void setTaskProject(TaskProject taskProject) {
        this.taskProject = taskProject;
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
            ", taskName='" + taskName + "'" +
            ", taskCheckDepartment='" + taskCheckDepartment + "'" +
            ", taskContent='" + taskContent + "'" +
            ", lawContent='" + lawContent + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
