package com.solvd.userservice.domain.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
public abstract class AbstractEvent implements Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "aggregate_id")
    protected String aggregateId;

    @Enumerated(EnumType.STRING)
    protected EventType type;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    protected LocalDateTime timestamp;

    @JdbcTypeCode(SqlTypes.JSON)
    protected Object payload;

}
