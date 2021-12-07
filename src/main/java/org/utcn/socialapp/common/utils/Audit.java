package org.utcn.socialapp.common.utils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

@Embeddable
public class Audit {

    @Column()
    private Instant createdOn;

    @Column()
    private Instant updatedOn;

    @PrePersist
    public void prePersist() {
        createdOn = Instant.now();
        updatedOn = createdOn;
    }

    @PreUpdate
    public void preUpdate() {
        updatedOn = Instant.now();
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }
}
