package com.wwillers.admin.catalog.application.category.update;

import com.wwillers.admin.catalog.domain.category.Category;
import com.wwillers.admin.catalog.domain.category.CategoryGateway;
import com.wwillers.admin.catalog.domain.category.CategoryID;
import com.wwillers.admin.catalog.domain.exceptions.DomainException;
import com.wwillers.admin.catalog.domain.validation.Error;
import com.wwillers.admin.catalog.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand command) {
        final var id = CategoryID.from(command.id());
        final var category = this.categoryGateway.findById(id)
                .orElseThrow(notFound(id));
        final var notification = Notification.create();
        category.update(
                        command.name(),
                        command.description(),
                        command.isActive()
                )
                .validate(notification);
        return notification.hasErrors()
                ? Left(notification)
                : update(category);
    }

    private Either<Notification, UpdateCategoryOutput> update(final Category category) {
        return Try(() -> this.categoryGateway.update(category))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }

    private static Supplier<DomainException> notFound(final CategoryID id) {
        return () ->
                DomainException.with(new Error("Category with id %s not found.".formatted(id.getValue())));
    }
}
