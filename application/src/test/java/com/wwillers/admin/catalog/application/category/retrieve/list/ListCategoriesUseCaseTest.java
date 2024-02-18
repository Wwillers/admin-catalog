package com.wwillers.admin.catalog.application.category.retrieve.list;

import com.wwillers.admin.catalog.domain.category.Category;
import com.wwillers.admin.catalog.domain.category.CategoryGateway;
import com.wwillers.admin.catalog.domain.category.CategorySearchQuery;
import com.wwillers.admin.catalog.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;
    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(gateway);
    }

    @Test
    void givenAValidQueryWhenCallsListCategoriesThenShouldReturnCategories() {
        final var query =
                new CategorySearchQuery(0, 10, "", "createdAt", "asc");
        final var categories = List.of(
                Category.newCategory("Movies", "Category 1", true),
                Category.newCategory("Series", "Category 2", true)
        );
        final var pagination = new Pagination<>(0, 10, categories.size(), categories);
        when(gateway.findAll(query))
                .thenReturn(pagination);
        final var expectedResult  = pagination.map(CategoryListOutput::from);

        final var result = useCase.execute(query);

        assertEquals(2, result.items().size());
        assertEquals(expectedResult, result);
        assertEquals(0, result.currentPage());
        assertEquals(10, result.perPage());
        assertEquals(2, result.total());
    }

    @Test
    void givenAValidQueryWhenHasNoResultsThenShouldReturnEmptyCategories() {
        final var categories = List.<Category>of();
        final var pagination = new Pagination<>(0, 10, categories.size(), categories);
        final var query =
                new CategorySearchQuery(0, 10, "", "createdAt", "asc");
        when(gateway.findAll(query))
                .thenReturn(pagination);
        final var expectedResult  = pagination.map(CategoryListOutput::from);

        final var result = useCase.execute(query);

        assertEquals(0, result.items().size());
        assertEquals(expectedResult, result);
        assertEquals(0, result.currentPage());
        assertEquals(10, result.perPage());
        assertEquals(0, result.total());
    }

    @Test
    void givenAValidQueryWhenGatewayThrowsExceptionThenShouldReturnException() {
        final var query =
                new CategorySearchQuery(0, 10, "", "createdAt", "asc");
        when(gateway.findAll(any(CategorySearchQuery.class)))
                .thenThrow(new IllegalStateException("Gateway error"));

        final var exception = Assertions.assertThrows(IllegalStateException.class,
                () -> useCase.execute(query));

        assertEquals("Gateway error", exception.getMessage());
    }

}
