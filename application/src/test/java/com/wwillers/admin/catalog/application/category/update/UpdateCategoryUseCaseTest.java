package com.wwillers.admin.catalog.application.category.update;

import com.wwillers.admin.catalog.domain.category.Category;
import com.wwillers.admin.catalog.domain.category.CategoryGateway;
import com.wwillers.admin.catalog.domain.category.CategoryID;
import com.wwillers.admin.catalog.domain.exceptions.DomainException;
import com.wwillers.admin.catalog.domain.validation.handler.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidCommandWhenCallsUpdateCategoryThenShouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "Movies category";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Move", null, expectedIsActive);
        final var expectedId = category.getId();
        final var command = UpdateCategoryCommand
                .with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command).get();
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway).findById(eq(expectedId));
        verify(categoryGateway).update(argThat(updatedCategory -> Objects.equals(expectedName, updatedCategory.getName())
                && Objects.equals(expectedDescription, updatedCategory.getDescription())
                && Objects.equals(expectedIsActive, updatedCategory.isActive())
                && Objects.equals(expectedId, updatedCategory.getId())
                && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                && category.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt())
                && Objects.isNull(updatedCategory.getDeletedAt())));
    }

    @Test
    void givenAnInvalidNameWhenCallsUpateCategoryThenShouldThrowDomainException() {
        final var expectedDescription = "Movies category";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Move", null, expectedIsActive);
        final var expectedId = category.getId();
        final var aCommand = UpdateCategoryCommand
                .with(expectedId.getValue(), null, expectedDescription, expectedIsActive);
        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(category.clone()));
        final Notification notification = useCase.execute(aCommand).getLeft();

        assertEquals("Name cannot be null", notification.firstError().message());
        assertEquals(1, notification.getErrors().size());
        verify(categoryGateway, never()).update(any());
    }

    @Test
    void givenAValidInactivateCommandWhenCallsUpdateCategoryThenShouldReturnInactivateCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "Movies category";

        final var category = Category.newCategory("Move", null, true);
        final var expectedId = category.getId();
        final var command = UpdateCategoryCommand
                .with(expectedId.getValue(), expectedName, expectedDescription, false);

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        assertTrue(category.isActive());
        assertNull(category.getDeletedAt());

        final var actualOutput = useCase.execute(command).get();
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway).findById(eq(expectedId));
        verify(categoryGateway).update(argThat(updatedCategory -> Objects.equals(expectedName, updatedCategory.getName())
                && Objects.equals(expectedDescription, updatedCategory.getDescription())
                && Objects.equals(false, updatedCategory.isActive())
                && Objects.equals(expectedId, updatedCategory.getId())
                && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                && category.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt())
                && Objects.nonNull(updatedCategory.getDeletedAt())));
    }

    @Test
    void givenAValidCommandWhenGatewayThrowsRandomExceptionThenShouldReturnAException() {
        final var expectedName = "Movies";
        final var expectedDescription = "Movies category";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Move", null, expectedIsActive);
        final var expectedId = category.getId();
        final var aCommand = UpdateCategoryCommand
                .with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any())).thenThrow(new IllegalStateException("Gateway Error"));

        Notification notification = useCase.execute(aCommand).getLeft();
        assertEquals("Gateway Error", notification.firstError().message());
        assertEquals(1, notification.getErrors().size());
        verify(categoryGateway).update(argThat(updatedCategory -> Objects.equals(expectedName, updatedCategory.getName())
                && Objects.equals(expectedDescription, updatedCategory.getDescription())
                && Objects.equals(expectedIsActive, updatedCategory.isActive())
                && Objects.equals(expectedId, updatedCategory.getId())
                && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                && category.getUpdatedAt().isBefore(updatedCategory.getUpdatedAt())
                && Objects.isNull(updatedCategory.getDeletedAt())));
    }

    @Test
    void givenACommandWithInvalidIDWhenCallsUpdateCategoryThenShouldReturnNotFoundException() {
        final var expectedName = "Movies";
        final var expectedDescription = "Movies category";
        final var expectedId = "1";
        final var command = UpdateCategoryCommand
                .with(expectedId, expectedName, expectedDescription, false);

        CategoryID categoryID = CategoryID.from(expectedId);
        when(categoryGateway.findById(eq(categoryID)))
                .thenReturn(Optional.empty());

        final var actualException = assertThrows(DomainException.class,
                () -> useCase.execute(command));

        assertEquals(1, actualException.getErrors().size());
        assertEquals("Category with id 1 not found.", actualException.getErrors().getFirst().message());
        verify(categoryGateway).findById(eq(categoryID));
        verify(categoryGateway, never()).update(any());
    }
}
