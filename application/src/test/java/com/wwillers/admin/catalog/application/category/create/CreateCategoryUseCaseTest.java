package com.wwillers.admin.catalog.application.category.create;

import com.wwillers.admin.catalog.domain.category.CategoryGateway;
import com.wwillers.admin.catalog.domain.validation.handler.Notification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidCommandWhenCallsCreateCategoryThenShouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "Movies category";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway)
                .create(argThat(category ->
                        Objects.equals(expectedName, category.getName())
                                && Objects.equals(expectedDescription, category.getDescription())
                                && Objects.equals(expectedIsActive, category.isActive())
                                && Objects.nonNull(category.getId())
                                && Objects.nonNull(category.getCreatedAt())
                                && Objects.nonNull(category.getUpdatedAt())
                                && Objects.isNull(category.getDeletedAt())
                ));
    }

    @Test
    void givenAnInvalidNameWhenCallsCreateCategoryThenShouldThrowDomainException() {
        final var expectedDescription = "Movies category";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand
                .with(null, expectedDescription, expectedIsActive);
        final Notification notification = useCase.execute(aCommand).getLeft();

        assertEquals("Name cannot be null", notification.firstError().message());
        assertEquals(1, notification.getErrors().size());
        verify(categoryGateway, never()).create(any());
    }

    @Test
    void givenAValidCommandWithInactiveCategoryWhenCallsCreateCategoryThenShouldReturnInactiveCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "Movies category";
        final var expectedIsActive = false;

        final var aCommand = CreateCategoryCommand
                .with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway)
                .create(argThat(category ->
                        Objects.equals(expectedName, category.getName())
                                && Objects.equals(expectedDescription, category.getDescription())
                                && Objects.equals(expectedIsActive, category.isActive())
                                && Objects.nonNull(category.getId())
                                && Objects.nonNull(category.getCreatedAt())
                                && Objects.nonNull(category.getUpdatedAt())
                                && Objects.nonNull(category.getDeletedAt())
                ));
    }

    @Test
    void givenAValidCommandWhenGatewayThrowsRandomExceptionThenShouldReturnAException() {
        final var expectedName = "Movies";
        final var expectedDescription = "Movies category";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand
                .with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(any())).thenThrow(new IllegalStateException("Gateway Error"));

        Notification notification = useCase.execute(aCommand).getLeft();
        assertEquals("Gateway Error", notification.firstError().message());
        assertEquals(1, notification.getErrors().size());
        verify(categoryGateway)
                .create(argThat(category ->
                        Objects.equals(expectedName, category.getName())
                                && Objects.equals(expectedDescription, category.getDescription())
                                && Objects.equals(expectedIsActive, category.isActive())
                                && Objects.nonNull(category.getId())
                                && Objects.nonNull(category.getCreatedAt())
                                && Objects.nonNull(category.getUpdatedAt())
                                && Objects.isNull(category.getDeletedAt())
                ));
    }
}
