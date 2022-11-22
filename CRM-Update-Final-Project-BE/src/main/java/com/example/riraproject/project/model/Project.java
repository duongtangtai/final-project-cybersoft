package com.example.riraproject.project.model;

import com.example.riraproject.common.model.BaseEntity;
import com.example.riraproject.common.util.JoinTableUtil;
import com.example.riraproject.task.model.Task;
import com.example.riraproject.user.model.User;
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

    @Column(name = ProjectEntity.STATUS)
    @Enumerated(EnumType.STRING)
    private Status status;

    //------------------RELATIONSHIP-----------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = JoinTableUtil.PROJECT_CREATOR_REFERENCE_USER)
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = JoinTableUtil.PROJECT_LEADER_REFERENCE_USER)
    private User leader;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) //delete this project won't affect users
    @JoinTable(name = JoinTableUtil.PROJECT_JOIN_WITH_USER,
            joinColumns = @JoinColumn(name = JoinTableUtil.PROJECT_ID),
            inverseJoinColumns = @JoinColumn(name = JoinTableUtil.USER_ID))
    private Set<User> users;

    @OneToMany(mappedBy = JoinTableUtil.TASK_REFERENCE_PROJECT,
    cascade = CascadeType.ALL) // delete this project will delete all tasks involved
    private Set<Task> tasks;

    //-----------------------------------------------------

    //------------------ENTITY LIFE CYCLES-----------------
    @PrePersist
    private void beforeSave() {
        if (status == null) {
            status = Status.DOING;
        }
    }
    //------------------------------------------------------
    public void addUser(User user) {
        this.users.add(user);
        user.getProjects().add(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getProjects().remove(this);
    }

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

    public enum Status {
        DOING,
        DONE
    }

    @UtilityClass
    static class ProjectEntity {
        public static final String TABLE_NAME = "J_PROJECT";
        public static final String NAME = "J_NAME";
        public static final String DESCRIPTION = "J_DESCRIPTION";
        public static final String SYMBOL = "J_SYMBOL";
        public static final String STATUS = "J_STATUS";
    }
}
