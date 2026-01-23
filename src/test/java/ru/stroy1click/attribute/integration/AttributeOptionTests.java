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
import ru.stroy1click.attribute.dto.AttributeOptionDto;

@Import({TestcontainersConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AttributeOptionTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void get_ShouldReturnProductTypeAttributeValue_WhenValueExists() {
        ResponseEntity<AttributeOptionDto> response = this.testRestTemplate
                .getForEntity("/api/v1/product-type-attribute-values/1", AttributeOptionDto.class);

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("Size", response.getBody().getValue());
    }

    @Test
    public void create_ShouldCreateProductTypeAttributeValue_WhenDtoIsValid() {
        AttributeOptionDto dto = new AttributeOptionDto(null, 1, 1, "Color");
        ResponseEntity<AttributeOptionDto> response = this.testRestTemplate
                .postForEntity("/api/v1/product-type-attribute-values", dto, AttributeOptionDto.class);

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody().getId());
        Assertions.assertNotNull(response.getHeaders().getLocation());
    }

    @Test
    public void update_ShouldUpdateProductTypeAttributeValue_WhenDtoIsValid() {
        AttributeOptionDto dto = new AttributeOptionDto(null, 1, 1, "Material");
        ResponseEntity<String> response = this.testRestTemplate.exchange(
                "/api/v1/product-type-attribute-values/2",
                HttpMethod.PATCH,
                new HttpEntity<>(dto),
                String.class
        );

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("Значение атрибута типа продукта обновлено", response.getBody());

        ResponseEntity<AttributeOptionDto> getResponse = this.testRestTemplate
                .getForEntity("/api/v1/product-type-attribute-values/2", AttributeOptionDto.class);
        Assertions.assertEquals("Material", getResponse.getBody().getValue());
    }

    @Test
    void delete_ShouldDeleteProductTypeAttributeValue_WhenValueExists() {
        ResponseEntity<String> response = this.testRestTemplate.exchange(
                "/api/v1/product-type-attribute-values/3",
                HttpMethod.DELETE,
                null,
                String.class
        );

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("Значение атрибута типа продукта удалено", response.getBody());
    }

    @Test
    void createValidation_ShouldReturnError_WhenDtoIsInvalid() {
        AttributeOptionDto invalidDto = new AttributeOptionDto(null, 1, 1, "x");
        ResponseEntity<ProblemDetail> response = this.testRestTemplate.postForEntity("/api/v1/product-type-attribute-values", invalidDto, ProblemDetail.class);

        Assertions.assertTrue(response.getStatusCode().is4xxClientError());
        Assertions.assertEquals("Ошибка валидации", response.getBody().getTitle());
    }

    @Test
    void updateValidation_ShouldReturnError_WhenDtoIsInvalid() {
        AttributeOptionDto invalidDto = new AttributeOptionDto(null, 1, 1, "");
        ResponseEntity<ProblemDetail> response = this.testRestTemplate.exchange(
                "/api/v1/product-type-attribute-values/1",
                HttpMethod.PATCH,
                new HttpEntity<>(invalidDto),
                ProblemDetail.class
        );

        Assertions.assertTrue(response.getStatusCode().is4xxClientError());
        Assertions.assertEquals("Ошибка валидации", response.getBody().getTitle());
    }

    @Test
    void get_ShouldReturnNotFound_WhenIdDoesNotExist() {
        ResponseEntity<ProblemDetail> response = this.testRestTemplate
                .getForEntity("/api/v1/product-type-attribute-values/1000000", ProblemDetail.class);

        Assertions.assertTrue(response.getStatusCode().is4xxClientError());
        Assertions.assertEquals("Значение атрибута продукта не найдено", response.getBody().getDetail());
    }
}

