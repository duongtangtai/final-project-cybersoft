package com.example.jiraproject.notification.model;


import com.example.jiraproject.common.model.BaseEntity;
import com.example.jiraproject.common.util.JoinTableUtil;
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

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Notification.NotificationEntity.TABLE_NAME)
public class Notification extends BaseEntity {

    @Column(name = NotificationEntity.DESCRIPTION, nullable = false, updatable = false)
    @Size(max = 200, message = "{notification.description.size}")
    @NotBlank(message = "{notification.description.not-blank}")
    private String description;

    @Column(name = NotificationEntity.STATUS, nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        SENT,
        READ
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = JoinTableUtil.NOTIFICATION_SENDER_REFERENCE_USER, nullable = false, updatable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = JoinTableUtil.NOTIFICATION_RECEIVER_REFERENCE_RECEIVER, nullable = false, updatable = false)
    private User receiver;


    //-----------------ENTITY LIFE CYCLES-----------------------
    @PrePersist
    private void beforeSaveNotification() {
        if (status == null) {
            status = Status.SENT;
        }
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Notification notification = (Notification) obj;
        return notification.getId().equals(id);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @UtilityClass
    static class NotificationEntity {
        public static final String TABLE_NAME = "J_NOTIFICATION";
        public static final String DESCRIPTION = "J_DESCRIPTION";
        public static final String STATUS = "J_STATUS";
    }
}
