package com.uth.quizclear.model.base;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Base entity class providing common audit fields and lifecycle management
 * Following AutoScore pattern for consistent entity structure
 * 
 * Features:
 * - Automatic audit trails (created/updated timestamps and users)
 * - Soft delete functionality
 * - Optimistic locking with version control
 * - Lifecycle callbacks for automatic timestamp management
 */
@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    // Optimistic locking để tránh concurrent modification
    @Version
    @Column(name = "version")
    private Long version;    /**
     * Abstract method to get entity ID - must be implemented by subclasses
     */
    public abstract Long getId();

    /**
     * Check if this is a new entity (not persisted yet)
     */
    public boolean isNew() {
        return getId() == null;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Soft delete functionality
     * @param deletedBy ID of user performing the delete operation
     */
    public void markDeleted(Long deletedBy) {
        LocalDateTime now = LocalDateTime.now();
        this.deletedAt = now;
        this.deletedBy = deletedBy;
        this.updatedAt = now;
        this.updatedBy = deletedBy;
    }

    /**
     * Soft delete without specifying user (for system operations)
     */
    public void markDeleted() {
        this.markDeleted(null);
    }

    /**
     * Check if entity is deleted
     */
    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    /**
     * Check if entity is active (not deleted)
     */
    public boolean isActive() {
        return !isDeleted();
    }

    /**
     * Restore soft deleted entity
     * @param restoredBy ID of user performing the restore operation
     */
    public void restore(Long restoredBy) {
        this.deletedAt = null;
        this.deletedBy = null;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = restoredBy;
    }

    /**
     * Restore soft deleted entity without specifying user
     */
    public void restore() {
        this.restore(null);
    }

    /**
     * Update the updatedBy field manually (useful for service layer)
     */
    public void setUpdatedBy(Long userId) {
        this.updatedBy = userId;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Get formatted creation timestamp
     */
    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toString() : "N/A";
    }

    /**
     * Get formatted update timestamp  
     */
    public String getFormattedUpdatedAt() {
        return updatedAt != null ? updatedAt.toString() : "N/A";
    }
}
