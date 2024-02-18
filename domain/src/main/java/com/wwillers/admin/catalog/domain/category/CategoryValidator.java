package com.wwillers.admin.catalog.domain.category;

import com.wwillers.admin.catalog.domain.validation.Error;
import com.wwillers.admin.catalog.domain.validation.ValidationHandler;
import com.wwillers.admin.catalog.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private static final int NAME_MIN_LENGTH = 3;
    private static final int NAME_MAX_LENGTH = 255;
    private final Category category;

    public CategoryValidator(final Category category, final ValidationHandler validationHandler) {
        super(validationHandler);
        this.category = category;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = category.getName();
        if (name == null) {
            validationHandler().append(new Error("Name cannot be null"));
            return;
        }
        if (name.isBlank()) {
            validationHandler().append(new Error("Name cannot be blank"));
            return;
        }
        final int length = name.trim().length();
        if (length < NAME_MIN_LENGTH || length > NAME_MAX_LENGTH) {
            validationHandler().append(new Error("Name must be between 3 and 255 characters"));
        }
    }
}
