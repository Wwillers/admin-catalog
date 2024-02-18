package com.wwillers.admin.catalog.application.category.create;

import com.wwillers.admin.catalog.domain.category.Category;
import com.wwillers.admin.catalog.domain.category.CategoryID;

public record CreateCategoryOutput(
        CategoryID id
) {

    public static CreateCategoryOutput with(Category category) {
        return new CreateCategoryOutput(category.getId());
    }

    public static CreateCategoryOutput from(Category category) {
        return new CreateCategoryOutput(category.getId());
    }
}
