package com.wwillers.admin.catalog.domain.category;

import com.wwillers.admin.catalog.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {

    Category create(Category category);
    Category update(Category category);
    void deleteBy(CategoryID categoryID);
    Optional<Category> findById(CategoryID categoryID);

    Pagination<Category> findAll(CategorySearchQuery query);
}
