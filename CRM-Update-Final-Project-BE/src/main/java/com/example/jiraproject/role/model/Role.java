package com.example.jiraproject.role.model;

import com.example.jiraproject.common.model.BaseEntity;
import com.example.jiraproject.common.util.JoinTableUtil;
import com.example.jiraproject.operation.model.Operation;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Role.RoleEntity.TABLE_NAME)
public class Role extends BaseEntity {

    @Column(name = RoleEntity.NAME, nullable = false)
    @Size(min = 2, max = 50, message = "{role.name.size}")
    @NotBlank(message = "{role.name.not-blank}")
    private String name;

    @Column(name = RoleEntity.CODE, nullable = false)
    @Size(min = 2, max = 50, message = "{role.code.size}")
    @NotBlank(message = "{role.code.not-blank}")
    private String code;

    @Column(name = RoleEntity.DESCRIPTION, nullable = false)
    @Size(min = 2, max = 500, message = "{role.description.size}")
    @NotBlank(message = "{role.description.not-blank}")
    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = JoinTableUtil.ROLE_JOIN_WITH_OPERATION,
            joinColumns = @JoinColumn(name = JoinTableUtil.ROLE_ID),
            inverseJoinColumns = @JoinColumn(name = JoinTableUtil.OPERATION_ID)
    )
    private Set<Operation> operations;

    @ManyToMany(mappedBy = JoinTableUtil.ROLE_MAPPED_BY_USER)
    private Set<User> users;

    public void addOperation(Operation operation) {
        this.getOperations().add(operation);
        operation.getRoles().add(this);
    }

    public void removeOperation(Operation operation) {
        this.getOperations().remove(operation);
        operation.getRoles().remove(this);
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
        Role role = (Role) obj;
        return role.getName().equals(name);
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @UtilityClass
    static class RoleEntity {
        public static final String TABLE_NAME = "J_ROLE";
        public static final String NAME = "J_NAME";
        public static final String CODE = "J_CODE";
        public static final String DESCRIPTION = "J_DESCRIPTION";
    }
}
