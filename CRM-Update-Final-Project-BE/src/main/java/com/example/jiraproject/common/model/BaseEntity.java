package com.example.jiraproject.common.model;

import com.example.jiraproject.common.util.DateTimeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.experimental.UtilityClass;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable, Comparable<BaseEntity> {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue
    @Column(name = Columns.ID)
    protected UUID id;

    @Version
    @Column(name = Columns.VERSION)
    protected Integer version;

    @CreatedDate
    @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_TIME_FORMAT)
    @Column(name = Columns.CREATED_AT)
    protected LocalDateTime createdAt;

    @CreatedBy
    @Column(name = Columns.CREATED_BY)
    protected String createdBy;

    @LastModifiedDate
    @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_TIME_FORMAT)
    @Column(name = Columns.LAST_MODIFIED_AT)
    protected LocalDateTime lastModifiedAt;

    @LastModifiedBy
    @Column(name = Columns.LAST_MODIFIED_BY)
    protected String lastModifiedBy;

    @Override
    public int compareTo(BaseEntity entity) {
        LocalDateTime entityTime = entity.getCreatedAt();
        if (entityTime.isAfter(createdAt)) {
            return -1;
        } else { // equal or before
            return 1;
        }
    }


    @UtilityClass
    static class Columns {
        public static final String ID = "ID";
        public static final String VERSION = "VERSION";
        public static final String CREATED_AT = "CREATED_AT";
        public static final String CREATED_BY = "CREATED_BY";
        public static final String LAST_MODIFIED_AT = "LAST_MODIFIED_AT";
        public static final String LAST_MODIFIED_BY = "LAST_MODIFIED_BY";
    }
}
