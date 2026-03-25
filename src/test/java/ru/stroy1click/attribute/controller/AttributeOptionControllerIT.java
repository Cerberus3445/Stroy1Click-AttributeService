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
import ru.stroy1click.attribute.dto.AttributeOptionDto;

import static org.junit.jupiter.api.Assertions.*;

@Import({TestcontainersConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AttributeOptionControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void get_WhenAttributeOptionExists_ShouldReturnAttributeOption() {
        //Act
        ResponseEntity<AttributeOptionDto> response = this.testRestTemplate
                .getForEntity("/api/v1/attribute-options/1", AttributeOptionDto.class);

        //Act
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("Size", response.getBody().getValue());
    }

    @Test
    public void get_WhenProductAttributeOptionDoesNotExist_ShouldThrowNotFoundException() {
        //Arrange
        int notExistsId = 99999;

        //Act
        ResponseEntity<ProblemDetail> response =
                this.testRestTemplate.getForEntity("/api/v1/attribute-options/" + notExistsId, ProblemDetail.class);

        //Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals("Значение атрибута с id %d не найдено".formatted(notExistsId),response.getBody().getDetail());
    }

    @Test
    void get_WhenAttributeOptionDoesNotExist_ShouldThrowNotFoundException() {
        //Act
        ResponseEntity<ProblemDetail> response = this.testRestTemplate
                .getForEntity("/api/v1/attribute-options/1000000", ProblemDetail.class);

        //Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals("Не найдено", response.getBody().getTitle());
    }

    @Test
    public void create_WhenValidDataProvided_ShouldReturnCreatedAttributeOption() {
        //Arrange
        AttributeOptionDto dto = new AttributeOptionDto(null, 1, 1, "Color");

        //Act
        ResponseEntity<AttributeOptionDto> response = this.testRestTemplate
                .postForEntity("/api/v1/attribute-options", dto, AttributeOptionDto.class);

        //Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertNotNull(response.getHeaders().getLocation());
    }

    @Test
    public void update_WhenValidDataProvidedAndAttributeOptionExists_ShouldUpdateAttributeOption() {
        //Arrange
        AttributeOptionDto dto = new AttributeOptionDto(null, 1, 1, "Material");

        //Act
        ResponseEntity<String> response = this.testRestTemplate.exchange(
                "/api/v1/attribute-options/2",
                HttpMethod.PATCH,
                new HttpEntity<>(dto),
                String.class
        );

        //Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Значение атрибута типа продукта обновлено", response.getBody());

        ResponseEntity<AttributeOptionDto> getResponse = this.testRestTemplate
                .getForEntity("/api/v1/attribute-options/2", AttributeOptionDto.class);
        assertNotNull(getResponse.getBody());
        assertEquals("Material", getResponse.getBody().getValue());
    }

    @Test
    void delete_WhenAttributeOptionExists_ShouldDeleteAttributeOption() {
        //Act
        ResponseEntity<String> response = this.testRestTemplate.exchange(
                "/api/v1/attribute-options/3",
                HttpMethod.DELETE,
                null,
                String.class
        );

        //Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Значение атрибута типа продукта удалено", response.getBody());

        ResponseEntity<ProblemDetail> notFoundResponse = this.testRestTemplate.exchange(
                "/api/v1/attribute-options/3",
                HttpMethod.GET,
                null,
                ProblemDetail.class
        );
        assertTrue(notFoundResponse.getStatusCode().is4xxClientError());
        assertNotNull(notFoundResponse.getBody());
        assertEquals("Не найдено", notFoundResponse.getBody().getTitle());
    }
}

