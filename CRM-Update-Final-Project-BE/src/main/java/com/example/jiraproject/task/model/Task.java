package com.example.jiraproject.task.model;

import com.example.jiraproject.comment.model.Comment;
import com.example.jiraproject.common.model.BaseEntity;
import com.example.jiraproject.common.util.DateTimeUtil;
import com.example.jiraproject.common.util.JoinTableUtil;
import com.example.jiraproject.project.model.Project;
import com.example.jiraproject.user.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.experimental.UtilityClass;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Task.TaskEntity.TABLE_NAME)
public class Task extends BaseEntity {

    @Column(name = TaskEntity.NAME, unique = true, nullable = false)
    @Size(min = 5, max = 50, message = "{task.name.size}")
    @NotBlank(message = "{task.name.not-blank}")
    private String name;

    @Column(name = TaskEntity.DESCRIPTION, nullable = false)
    @Size(min = 5, max = 300, message = "{task.description.size}")
    @NotBlank(message = "{task.description.not-blank}")
    private String description;

    @Column(name = TaskEntity.STARTED_DATE_EXPECTED, nullable = false)
    @DateTimeFormat(pattern = DateTimeUtil.DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_FORMAT)
    private LocalDate startDateExpected;

    @Column(name = TaskEntity.END_DATE_EXPECTED, nullable = false)
    @DateTimeFormat(pattern = DateTimeUtil.DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_FORMAT)
    private LocalDate endDateExpected;

    @Column(name = TaskEntity.STARTED_DATE_IN_FACT)
    @DateTimeFormat(pattern = DateTimeUtil.DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_FORMAT)
    private LocalDate startDateInFact;

    @Column(name = TaskEntity.END_DATE_IN_FACT)
    @DateTimeFormat(pattern = DateTimeUtil.DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_FORMAT)
    private LocalDate endDateInFact;

    @Column(name = TaskEntity.TASK_STATUS, nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = JoinTableUtil.TASK_REFERENCE_PROJECT, nullable = false, updatable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = JoinTableUtil.TASK_REFERENCE_USER) // nullable, reporter can be changed
    private User reporter;

    @OneToMany(mappedBy = JoinTableUtil.COMMENT_REFERENCE_TASK,
    cascade = CascadeType.ALL) //delete this task will delete all relative comments
    private Set<Comment> comments;

    @Override
    public int hashCode() {
        return (getClass() + name).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Task task = (Task) obj;
        return task.getName().equals(name);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDateExpected=" + startDateExpected +
                ", endDateExpected=" + endDateExpected +
                ", startDateInFact=" + startDateInFact +
                ", endDateInFact=" + endDateInFact +
                ", status=" + status +
                '}';
    }

    public enum Status {
        UNASSIGNED,
        STARTED,
        COMPLETED
    }

    @UtilityClass
    static class TaskEntity {
        public static final String TABLE_NAME = "J_TASK";
        public static final String NAME = "J_NAME";
        public static final String DESCRIPTION = "J_DESCRIPTION";
        public static final String STARTED_DATE_EXPECTED = "J_STARTED_DATE_EXPECTED";
        public static final String END_DATE_EXPECTED = "J_END_DATE_EXPECTED";
        public static final String STARTED_DATE_IN_FACT = "J_STARTED_DATE_IN_FACT";
        public static final String END_DATE_IN_FACT = "J_END_DATE_IN_FACT";
        public static final String TASK_STATUS = "J_TASK_STATUS";
    }
}
