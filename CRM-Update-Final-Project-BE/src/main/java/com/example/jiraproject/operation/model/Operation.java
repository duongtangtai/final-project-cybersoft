package com.example.jiraproject.operation.model;

import com.example.jiraproject.common.model.BaseEntity;
import com.example.jiraproject.common.util.JoinTableUtil;
import com.example.jiraproject.role.model.Role;
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
@Table(name = Operation.OperationEntity.TABLE_NAME, uniqueConstraints = {
        @UniqueConstraint(name = Operation.OperationEntity.NAME_TYPE_UNIQUE_CONSTRAINT ,columnNames = {
                Operation.OperationEntity.NAME,
                Operation.OperationEntity.TYPE
        })
})
public class Operation extends BaseEntity {

    @Column(name = OperationEntity.NAME, nullable = false)
    @Size(max = 100, message = "{}")
    @NotBlank(message = "{}")
    private String name;

    @Column(name = OperationEntity.DESCRIPTION, nullable = false)
    @Size(max = 500, message = "{}")
    @NotBlank(message = "{}")
    private String description;

    @Column(name = OperationEntity.TYPE, nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToMany(mappedBy = JoinTableUtil.OPERATION_MAPPED_BY_ROLE)
    private Set<Role> roles;

    @Override
    public int hashCode() {
        return (getClass() + name + type.toString()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Operation operation = (Operation) obj;
        return operation.getName().equals(name) && operation.getType().toString().equals(type.toString());
    }

    @Override
    public String toString() {
        return "Operation{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                '}';
    }

    public enum Type {
        FETCH,
        SAVE_OR_UPDATE,
        REMOVE
    }

    @UtilityClass
    static class OperationEntity {
        public static final String TABLE_NAME = "J_OPERATION";
        public static final String NAME = "J_NAME";
        public static final String DESCRIPTION = "J_DESCRIPTION";
        public static final String TYPE = "J_TYPE";
        public static final String NAME_TYPE_UNIQUE_CONSTRAINT = "NAME_TYPE_UNIQUE_CONSTRAINT";
    }
}
