package com.wwillers.admin.catalog.application.category.retrieve.get;

import com.wwillers.admin.catalog.domain.category.Category;
import com.wwillers.admin.catalog.domain.category.CategoryGateway;
import com.wwillers.admin.catalog.domain.category.CategoryID;
import com.wwillers.admin.catalog.domain.exceptions.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidIdWhenCallsGetCategoryByIdThenShouldReturnCategory() {
        final var category = Category.newCategory("Movies", "Most Watched", true);
        final var expectedId = category.getId();
        when(categoryGateway.findById(expectedId))
                .thenReturn(Optional.of(category.clone()));

        final var actualCategory = useCase.execute(expectedId.getValue());

        assertEquals(CategoryOutput.from(category), actualCategory);
        assertEquals(expectedId, actualCategory.id());
        assertEquals("Movies", actualCategory.name());
        assertEquals("Most Watched", actualCategory.description());
        assertTrue(actualCategory.isActive());
        assertEquals(category.getCreatedAt(), actualCategory.createdAt());
        assertEquals(category.getUpdatedAt(), actualCategory.updatedAt());
        assertEquals(category.getDeletedAt(), actualCategory.deletedAt());
    }

    @Test
    void givenAnInvalidIdWhenCallsGetCategoryByIdThenShouldReturnNotFound() {
        final var expectedId = CategoryID.from("1");
        when(categoryGateway.findById(expectedId))
                .thenReturn(Optional.empty());

        final var exception = assertThrows(DomainException.class,
                () -> useCase.execute(expectedId.getValue()));

        assertEquals("Category with id 1 not found.", exception.getErrors().getFirst().message());
    }

    @Test
    void givenAValidIdWhenGatewayThrowsExceptionThenShouldReturnException() {
        final var expectedId = CategoryID.from("1");
        when(categoryGateway.findById(expectedId))
                .thenThrow(new IllegalStateException("Gateway Error"));

        final var exception = assertThrows(IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue()));

        assertEquals("Gateway Error", exception.getMessage());
    }
}
