package com.wwillers.admin.catalog.application.category.retrieve.list;

import com.wwillers.admin.catalog.application.UseCase;
import com.wwillers.admin.catalog.domain.category.CategorySearchQuery;
import com.wwillers.admin.catalog.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase
        extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
