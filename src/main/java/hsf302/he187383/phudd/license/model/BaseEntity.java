package hsf302.he187383.phudd.license.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
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

    public UUID getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
