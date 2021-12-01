package org.utcn.socialapp.common.utils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.OffsetDateTime;

@Embeddable
public class Audit {

    @Column(name = "created_on")
    private OffsetDateTime createdOn;

    @Column(name = "updated_on")
    private OffsetDateTime updatedOn;

    @PrePersist
    public void prePersist() {
        createdOn = OffsetDateTime.now();
        updatedOn = createdOn;
    }

    @PreUpdate
    public void preUpdate() {
        updatedOn = OffsetDateTime.now();
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public OffsetDateTime getUpdatedOn() {
        return updatedOn;
    }
}
