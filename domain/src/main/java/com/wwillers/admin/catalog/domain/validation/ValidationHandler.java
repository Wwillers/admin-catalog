package com.wwillers.admin.catalog.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error error);
    ValidationHandler append(ValidationHandler handler);
    ValidationHandler validate(Validation validation);

    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default Error firstError() {
        if (getErrors() != null) {
            return getErrors().getFirst();
        }
        return null;
    }

    List<Error> getErrors();

    interface Validation {
        void validate();
    }
}
