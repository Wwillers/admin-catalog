package com.wwillers.admin.catalog.infrastructure.category;

import com.wwillers.admin.catalog.domain.category.Category;
import com.wwillers.admin.catalog.domain.category.CategoryGateway;
import com.wwillers.admin.catalog.domain.category.CategoryID;
import com.wwillers.admin.catalog.domain.category.CategorySearchQuery;
import com.wwillers.admin.catalog.domain.pagination.Pagination;
import com.wwillers.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryPostgresGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    public CategoryPostgresGateway(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category category) {
        return null;
    }

    @Override
    public Category update(Category category) {
        return null;
    }

    @Override
    public void deleteBy(CategoryID categoryID) {

    }

    @Override
    public Optional<Category> findById(CategoryID categoryID) {
        return Optional.empty();
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery query) {
        return null;
    }
}
