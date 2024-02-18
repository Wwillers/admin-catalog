package com.wwillers.admin.catalog.application.category.retrieve.get;

import com.wwillers.admin.catalog.domain.category.CategoryGateway;
import com.wwillers.admin.catalog.domain.category.CategoryID;
import com.wwillers.admin.catalog.domain.exceptions.DomainException;
import com.wwillers.admin.catalog.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(final String input) {
        final var categoryID = CategoryID.from(input);
        return categoryGateway.findById(categoryID)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(categoryID));
    }

    private static Supplier<DomainException> notFound(final CategoryID id) {
        return () ->
                DomainException.with(new Error("Category with id %s not found.".formatted(id.getValue())));
    }
}
