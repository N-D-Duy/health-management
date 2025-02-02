package com.example.health_management.common.utils.softdelete;

import java.time.LocalDateTime;

public interface SoftDeletable {
    LocalDateTime getDeletedAt();
    void setDeletedAt(LocalDateTime deletedAt);
}
