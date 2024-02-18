package com.wwillers.admin.catalog.domain.validation.handler;

import com.wwillers.admin.catalog.domain.exceptions.DomainException;
import com.wwillers.admin.catalog.domain.validation.Error;
import com.wwillers.admin.catalog.domain.validation.ValidationHandler;

import java.util.Collections;
import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(final Error error) {
        throw DomainException.with(error);
    }

    @Override
    public ValidationHandler append(final ValidationHandler handler) {
        throw DomainException.with(handler.getErrors());
    }

    @Override
    public ValidationHandler validate(final Validation validation) {
        try {
            validation.validate();
        } catch (final Exception e) {
            throw DomainException.with(Collections.singletonList(new Error(e.getMessage())));
        }
        return this;
    }

    @Override
    public boolean hasErrors() {
        return ValidationHandler.super.hasErrors();
    }

    @Override
    public List<Error> getErrors() {
        return null;
    }
}
