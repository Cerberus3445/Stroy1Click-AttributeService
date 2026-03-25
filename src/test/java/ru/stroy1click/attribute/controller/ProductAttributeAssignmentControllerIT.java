package ru.stroy1click.attribute.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import ru.stroy1click.attribute.config.TestcontainersConfiguration;
import ru.stroy1click.attribute.dto.ProductAttributeAssignmentDto;

import static org.junit.jupiter.api.Assertions.*;


@Import({TestcontainersConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductAttributeAssignmentControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void get_WhenProductAttributeAssignmentExists_ShouldReturnProductAttributeAssignmentExists() {
        //Act
        ResponseEntity<ProductAttributeAssignmentDto> response =
                this.testRestTemplate.getForEntity("/api/v1/product-attribute-assignments/1", ProductAttributeAssignmentDto.class);

        //Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getProductId());
    }

    @Test
    public void get_WhenProductAttributeAssignmentDoesNotExist_ShouldThrowNotFoundException() {
        //Arrange
        int notExistsId = 99999;

        //Act
        ResponseEntity<ProblemDetail> response =
                this.testRestTemplate.getForEntity("/api/v1/product-attribute-assignments/" + notExistsId, ProblemDetail.class);

        //Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals("Назначение атрибута к продукту с id %d не найдено".formatted(notExistsId),response.getBody().getDetail());
    }

    @Test
    public void create_WhenValidDataProvided_ShouldReturnProductAttributeAssignment() {
        //Arrange
        ProductAttributeAssignmentDto dto = new ProductAttributeAssignmentDto(null,1,1);

        //Act
        ResponseEntity<ProductAttributeAssignmentDto> response =
                this.testRestTemplate.postForEntity("/api/v1/product-attribute-assignments", dto, ProductAttributeAssignmentDto.class);

        //Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertNotNull(response.getHeaders().getLocation());
    }

    @Test
    public void update_WhenValidDataProvidedAndProductAttributeAssignmentExists_ShouldUpdateProductAttributeAssignment() {
        //Arrange
        ProductAttributeAssignmentDto dto = new ProductAttributeAssignmentDto(null,100,1);

        //Act
        ResponseEntity<String> response = this.testRestTemplate.exchange(
                "/api/v1/product-attribute-assignments/2",
                HttpMethod.PATCH,
                new HttpEntity<>(dto),
                String.class
        );

        //Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Значение атрибута продукта обновлено", response.getBody());

        ResponseEntity<ProductAttributeAssignmentDto> getResponse = this.testRestTemplate.getForEntity("/api/v1/product-attribute-assignments/2", ProductAttributeAssignmentDto.class);
        assertNotNull(getResponse.getBody());
        assertEquals(dto.getProductId(), getResponse.getBody().getProductId());
    }

    @Test
    public void delete_WhenProductAttributeAssignmentExists_ShouldDeleteProductAttributeAssignment() {
        //Act
        ResponseEntity<String> response = this.testRestTemplate.exchange(
                "/api/v1/product-attribute-assignments/3",
                HttpMethod.DELETE,
                null,
                String.class
        );

        //Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Значение атрибута продукта удалено", response.getBody());

        ResponseEntity<ProblemDetail> notFoundResponse = this.testRestTemplate.exchange(
                "/api/v1/product-attribute-assignments/3",
                HttpMethod.GET,
                null,
                ProblemDetail.class
        );
        assertTrue(notFoundResponse.getStatusCode().is4xxClientError());
        assertNotNull(notFoundResponse.getBody());
        assertEquals("Не найдено", notFoundResponse.getBody().getTitle());
    }
}
