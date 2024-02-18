package com.wwillers.admin.catalog.application.category.create;

import com.wwillers.admin.catalog.application.UseCase;
import com.wwillers.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
