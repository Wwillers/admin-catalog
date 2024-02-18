package com.wwillers.admin.catalog.application.category.delete;

import com.wwillers.admin.catalog.domain.category.Category;
import com.wwillers.admin.catalog.domain.category.CategoryGateway;
import com.wwillers.admin.catalog.domain.category.CategoryID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidIdWhenCallsDeleteCategoryThenShouldBeOk() {
        final var category = Category.newCategory("Movies", "Most Watched", true);
        final var expectedId = category.getId();
        doNothing()
                .when(categoryGateway).deleteBy(expectedId);
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(categoryGateway).deleteBy(expectedId);
    }

    @Test
    void givenAnInvalidIdWhenCallsDeleteCategoryThenShouldBeOk() {
        final var expectedId = CategoryID.from("1");
        doNothing()
                .when(categoryGateway).deleteBy(expectedId);
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(categoryGateway).deleteBy(expectedId);
    }

    @Test
    void givenAValidIdWhenGatewayThrowsErrorThenShouldReturnException() {
        final var category = Category.newCategory("Movies", "Most Watched", true);
        final var expectedId = category.getId();
        doThrow(new IllegalStateException("Gateway Error"))
                .when(categoryGateway).deleteBy(expectedId);
        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        verify(categoryGateway).deleteBy(expectedId);
    }
}
