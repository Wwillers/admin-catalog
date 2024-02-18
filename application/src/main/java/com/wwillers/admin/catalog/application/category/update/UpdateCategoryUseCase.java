package com.wwillers.admin.catalog.application.category.update;

import com.wwillers.admin.catalog.application.UseCase;
import com.wwillers.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
