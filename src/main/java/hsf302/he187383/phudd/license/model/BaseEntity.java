package hsf302.he187383.phudd.license.model;

import jakarta.persistence.*;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uniqueidentifier")
    protected UUID id;

    @Column(nullable = false)
    protected Instant createdAt = Instant.now();

    @Column(nullable = false)
    protected Instant updatedAt = Instant.now();

    @PreUpdate
    protected void onUpdate() { this.updatedAt = Instant.now(); }
}
