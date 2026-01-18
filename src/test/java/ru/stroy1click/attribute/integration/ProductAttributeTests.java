package ru.stroy1click.attribute.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import ru.stroy1click.attribute.dto.ProductAttributeDto;


@Import({TestcontainersConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductAttributeTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void get_ShouldReturnProductAttributeValue_WhenValueExists() {
        ResponseEntity<ProductAttributeDto> response = this.testRestTemplate.getForEntity("/api/v1/product-attributes/1", ProductAttributeDto.class);

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(1, response.getBody().getProductId());
    }

    @Test
    public void create_ShouldCreateProductAttributeValue_WhenDtoIsValid() {
        ProductAttributeDto dto = new ProductAttributeDto(null,1,1);
        ResponseEntity<ProductAttributeDto> response =
                this.testRestTemplate.postForEntity("/api/v1/product-attributes", dto, ProductAttributeDto.class);

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody().getId());
        Assertions.assertNotNull(response.getHeaders().getLocation());

    }

    @Test
    public void update_ShouldUpdateProductAttributeValue_WhenDtoIsValid() {
        ProductAttributeDto dto = new ProductAttributeDto(null,100,1);
        ResponseEntity<String> response = this.testRestTemplate.exchange(
                "/api/v1/product-attributes/2",
                HttpMethod.PATCH,
                new HttpEntity<>(dto),
                String.class
        );

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("Значение атрибута продукта обновлено", response.getBody());

        ResponseEntity<ProductAttributeDto> getResponse = this.testRestTemplate.getForEntity("/api/v1/product-attributes/2", ProductAttributeDto.class);
        Assertions.assertEquals(dto.getProductId(), getResponse.getBody().getProductId());
    }

    @Test
    public void delete_ShouldDeleteProductAttributeValue_WhenValueExists() {
        ResponseEntity<String> response = this.testRestTemplate.exchange(
                "/api/v1/product-attributes/3",
                HttpMethod.DELETE,
                null,
                String.class
        );

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("Значение атрибута продукта удалено", response.getBody());
    }

    @Test
    public void createValidation_ShouldReturnError_WhenDtoIsInvalid() {

        ProductAttributeDto invalidDto = new ProductAttributeDto(null, -11, 1);
        ResponseEntity<ProblemDetail> response = this.testRestTemplate.postForEntity("/api/v1/product-attributes", invalidDto, ProblemDetail.class);

        Assertions.assertTrue(response.getStatusCode().is4xxClientError());
        Assertions.assertEquals("Ошибка валидации", response.getBody().getTitle());
    }

    @Test
    public void updateValidation_ShouldReturnError_WhenDtoIsInvalid() {
        ProductAttributeDto invalidDto = new ProductAttributeDto(null, 1, -11);
        ResponseEntity<ProblemDetail> response = this.testRestTemplate.exchange(
                "/api/v1/product-attributes/1",
                HttpMethod.PATCH,
                new HttpEntity<>(invalidDto),
                ProblemDetail.class
        );

        Assertions.assertTrue(response.getStatusCode().is4xxClientError());
        Assertions.assertEquals("Ошибка валидации", response.getBody().getTitle());
    }

    @Test
    public void get_ShouldReturnNotFound_WhenIdDoesNotExist() {
        ResponseEntity<ProblemDetail> response = this.testRestTemplate.getForEntity("/api/v1/product-attributes/1000000", ProblemDetail.class);

        Assertions.assertTrue(response.getStatusCode().is4xxClientError());
        Assertions.assertEquals("Значение атрибута типа продукта не найдено", response.getBody().getDetail());
    }
}
