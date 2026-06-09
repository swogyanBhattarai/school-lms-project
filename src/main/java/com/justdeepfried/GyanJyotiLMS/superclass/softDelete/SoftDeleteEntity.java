package com.justdeepfried.GyanJyotiLMS.superclass.softDelete;

import com.justdeepfried.GyanJyotiLMS.superclass.base.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.FilterDef;

import java.time.LocalDateTime;

@MappedSuperclass
@FilterDef(
        name = "softDeleteFilter",
        defaultCondition = "deleted_at IS NULL"
)
public abstract class SoftDeleteEntity extends BaseEntity {

    private LocalDateTime deletedAt;

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
