package com.wwillers.admin.catalog.domain.category;

import com.wwillers.admin.catalog.domain.exceptions.DomainException;
import com.wwillers.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryTest {

    @Test
    void givenAValidParamsWhenCallNewCategoryThenInstantiateACategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "All about movies";
        final var expectedIsActive = true;

        Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertNotNull(category);
        assertNotNull(category.getId());
        assertEquals(expectedName, category.getName());
        assertEquals(expectedDescription, category.getDescription());
        assertEquals(expectedIsActive, category.isActive());
        assertNotNull(category.getCreatedAt());
        assertNotNull(category.getUpdatedAt());
        assertNull(category.getDeletedAt());
    }

    @Test
    void givenAnInvalidNullNameWhenCallNewCategoryAndValidateThenShouldReceiverError() {
        final String expectedName = null;
        final var expectedDescription = "All about movies";
        final var expectedIsActive = true;

        Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = assertThrows(DomainException.class, () -> {
            category.validate(new ThrowsValidationHandler());
        });
        assertEquals("Name cannot be null", actualException.getErrors().get(0).message());
        assertEquals(1, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidEmptyNameWhenCallNewCategoryAndValidateThenShouldReceiverError() {
        final String expectedName = "  ";
        final var expectedDescription = "All about movies";
        final var expectedIsActive = true;

        Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = assertThrows(DomainException.class, () -> {
            category.validate(new ThrowsValidationHandler());
        });
        assertEquals("Name cannot be blank", actualException.getErrors().get(0).message());
        assertEquals(1, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidNameLengthLessThen3WhenCallNewCategoryAndValidateThenShouldReceiverError() {
        final String expectedName = "Fa ";
        final var expectedDescription = "All about movies";
        final var expectedIsActive = true;

        Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = assertThrows(DomainException.class, () -> {
            category.validate(new ThrowsValidationHandler());
        });
        assertEquals("Name must be between 3 and 255 characters", actualException.getErrors().get(0).message());
        assertEquals(1, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidNameLengthMoreThen255WhenCallNewCategoryAndValidateThenShouldReceiverError() {
        final String expectedName = """
                No entanto, não podemos esquecer que a competitividade nas transações comerciais maximiza 
                as possibilidades por conta do remanejamento dos quadros funcionais. 
                Caros amigos, a crescente influência da mídia desafia a capacidade de equalização do orçamento setorial.
                """;
        final var expectedDescription = "All about movies";
        final var expectedIsActive = true;

        Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = assertThrows(DomainException.class, () -> {
            category.validate(new ThrowsValidationHandler());
        });
        assertEquals("Name must be between 3 and 255 characters", actualException.getErrors().get(0).message());
        assertEquals(1, actualException.getErrors().size());
    }

    @Test
    void givenAValidEmptyDescriptionWhenCallNewCategoryAndValidateThenShouldNotReceiveAnError() {
        final var expectedName = "Movies";
        final var expectedDescription = " ";
        final var expectedIsActive = true;

        Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        assertNotNull(category);
        assertNotNull(category.getId());
        assertEquals(expectedName, category.getName());
        assertEquals(expectedDescription, category.getDescription());
        assertEquals(expectedIsActive, category.isActive());
        assertNotNull(category.getCreatedAt());
        assertNotNull(category.getUpdatedAt());
        assertNull(category.getDeletedAt());
    }

    @Test
    void givenAValidFalseIsActiveWhenCallNewCategoryAndValidateThenShouldNotReceiveAnError() {
        final var expectedName = "Movies";
        final var expectedDescription = "Melhor categoria";
        final var expectedIsActive = false;

        Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        assertNotNull(category);
        assertNotNull(category.getId());
        assertEquals(expectedName, category.getName());
        assertEquals(expectedDescription, category.getDescription());
        assertEquals(expectedIsActive, category.isActive());
        assertNotNull(category.getCreatedAt());
        assertNotNull(category.getUpdatedAt());
        assertNotNull(category.getDeletedAt());
    }

    @Test
    void givenAValidActiveCategoryWhenCallDeactivateThenReturnCategoryInactivated() {
        final var expectedName = "Movies";
        final var expectedDescription = "Melhor categoria";
        final var expectedIsActive = false;

        Category category = Category.newCategory(expectedName, expectedDescription, true);

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var updatedAt = category.getUpdatedAt();
        assertTrue(category.isActive());
        assertNull(category.getDeletedAt());

        final var actualCategory = category.deactivate();
        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getDeletedAt());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    void givenAValidInactiveCategoryWhenCallActivateThenReturnAnActiveCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Melhor categoria";
        final var expectedIsActive = true;

        Category category = Category.newCategory(expectedName, expectedDescription, false);
        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();
        assertFalse(category.isActive());
        assertNotNull(category.getDeletedAt());

        final var actualCategory = category.activate();

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(createdAt, actualCategory.getCreatedAt());
        assertNull(actualCategory.getDeletedAt());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    void givenAValidCategoryWhenCallUpdateThenReturnCategoryUpdated() {
        final var expectedName = "Movies";
        final var expectedDescription = "Melhor categoria";
        final var expectedIsActive = true;

        Category category = Category.newCategory("Movie", "Melhor", expectedIsActive);

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));


        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        final var actualCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(createdAt, actualCategory.getCreatedAt());
        assertNull(actualCategory.getDeletedAt());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    void givenAValidCategoryWhenCallUpdateToInactiveThenReturnCategoryUpdated() {
        final var expectedName = "Movies";
        final var expectedDescription = "Melhor categoria";
        final var expectedIsActive = false;

        Category category = Category.newCategory("Movie", "Melhor", true);

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        assertTrue(category.isActive());
        assertNull(category.getDeletedAt());

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        final var actualCategory = category.update(expectedName, expectedDescription, false);
        assertFalse(category.isActive());
        assertNotNull(category.getDeletedAt());

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(createdAt, actualCategory.getCreatedAt());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    void givenAValidCategoryWhenCallUpdateWithInvalidParamsThenReturnCategoryUpdated() {
        final var expectedDescription = "Melhor categoria";
        final var expectedIsActive = true;

        Category category = Category.newCategory("Movies", expectedDescription, expectedIsActive);
        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        final var actualCategory = category.update(null, expectedDescription, expectedIsActive);

        assertEquals(category.getId(), actualCategory.getId());
        assertNull(actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertTrue(actualCategory.isActive());
        assertEquals(createdAt, actualCategory.getCreatedAt());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNull(actualCategory.getDeletedAt());
    }
}
