package com.example.jiraproject.project.model;

import com.example.jiraproject.common.model.BaseEntity;
import com.example.jiraproject.common.util.JoinTableUtil;
import com.example.jiraproject.task.model.Task;
import com.example.jiraproject.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.experimental.UtilityClass;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Project.ProjectEntity.TABLE_NAME)
public class Project extends BaseEntity{

    @Column(name = ProjectEntity.NAME, unique = true, nullable = false)
    @Size(min = 5, max = 50, message = "{project.name.size}")
    @NotBlank(message = "{project.name.not-blank}")
    private String name;

    @Column(name = ProjectEntity.DESCRIPTION, nullable = false)
    @Size(min = 5, max = 500, message = "{project.description.size}")
    @NotBlank(message = "{project.description.not-blank}")
    private String description;

    @Column(name = ProjectEntity.SYMBOL)
    @Size(min = 5, max = 100, message = "{project.symbol.size}")
    private String symbol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = JoinTableUtil.PROJECT_CREATOR_REFERENCE_USER)
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = JoinTableUtil.PROJECT_LEADER_REFERENCE_USER)
    private User leader;

    @OneToMany(mappedBy = JoinTableUtil.TASK_REFERENCE_PROJECT,
    cascade = CascadeType.ALL) // delete this project will delete all tasks involved
    private Set<Task> tasks;

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
        Project project = (Project) obj;
        return project.getName().equals(name);
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }

    @UtilityClass
    static class ProjectEntity {
        public static final String TABLE_NAME = "J_PROJECT";
        public static final String NAME = "J_NAME";
        public static final String DESCRIPTION = "J_DESCRIPTION";
        public static final String SYMBOL = "J_SYMBOL";
    }
}
