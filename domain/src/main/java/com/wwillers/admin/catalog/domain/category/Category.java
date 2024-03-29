package com.wwillers.admin.catalog.domain.category;

import com.wwillers.admin.catalog.domain.AggregateRoot;
import com.wwillers.admin.catalog.domain.validation.ValidationHandler;

import java.time.Instant;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(final CategoryID id,
                     final String name,
                     final String description,
                     final boolean isActive,
                     final Instant createdAt,
                     final Instant updatedAt,
                     final Instant deletedAt) {
        super(id);
        this.name = name;
        this.description = description;
        this.active = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Category newCategory(final String name,
                                       final String description,
                                       final boolean isActive) {
        final var uuid = CategoryID.unique();
        final Instant now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Category(uuid, name, description, isActive, now, now, deletedAt);
    }

    public static Category with(Category category) {
        return with(category.getId(),
                    category.getName(),
                    category.getDescription(),
                    category.isActive(),
                    category.getCreatedAt(),
                    category.getUpdatedAt(),
                    category.getDeletedAt());
    }

    public static Category with(CategoryID id,
                                String name,
                                String description,
                                boolean active,
                                Instant createdAt,
                                Instant updatedAt,
                                Instant deletedAt) {
        return new Category(id, name, description, active, createdAt, updatedAt, deletedAt);
    }

    @Override
    public void validate(ValidationHandler validationHandler) {
        new CategoryValidator(this, validationHandler).validate();
    }

    public Category activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = Instant.now();
        }
        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category update(final String name, final String description, final boolean isActive) {
        this.name = name;
        this.description = description;
        this.updatedAt = Instant.now();
        if (isActive) activate();
        else deactivate();
        return this;
    }

    public CategoryID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
