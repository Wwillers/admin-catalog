package com.wwillers.admin.catalog.application.category.retrieve.list;

import com.wwillers.admin.catalog.domain.category.CategoryGateway;
import com.wwillers.admin.catalog.domain.category.CategorySearchQuery;
import com.wwillers.admin.catalog.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(CategorySearchQuery query) {
        return this.categoryGateway.findAll(query)
                .map(CategoryListOutput::from);
    }
}
