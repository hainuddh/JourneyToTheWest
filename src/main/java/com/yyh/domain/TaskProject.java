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
 * A TaskProject.
 */
@Entity
@Table(name = "task_project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "taskproject")
public class TaskProject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "task_project_name", length = 64, nullable = false)
    private String taskProjectName;

    @Size(max = 1024)
    @Column(name = "description", length = 1024)
    private String description;

    @OneToMany(mappedBy = "taskProject")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> tasks = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskProjectName() {
        return taskProjectName;
    }

    public TaskProject taskProjectName(String taskProjectName) {
        this.taskProjectName = taskProjectName;
        return this;
    }

    public void setTaskProjectName(String taskProjectName) {
        this.taskProjectName = taskProjectName;
    }

    public String getDescription() {
        return description;
    }

    public TaskProject description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public TaskProject tasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public TaskProject addTask(Task task) {
        tasks.add(task);
        task.setTaskProject(this);
        return this;
    }

    public TaskProject removeTask(Task task) {
        tasks.remove(task);
        task.setTaskProject(null);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskProject taskProject = (TaskProject) o;
        if (taskProject.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, taskProject.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TaskProject{" +
            "id=" + id +
            ", taskProjectName='" + taskProjectName + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
