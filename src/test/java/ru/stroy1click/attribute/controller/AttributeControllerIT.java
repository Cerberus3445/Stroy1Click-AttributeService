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
import ru.stroy1click.attribute.dto.AttributeDto;

import static org.junit.jupiter.api.Assertions.*;

@Import({TestcontainersConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AttributeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void get_WhenAttributeExists_ShouldReturnAttribute() {
        //Act
        ResponseEntity<AttributeDto> responseEntity = this.testRestTemplate.getForEntity(
                "/api/v1/attributes/1", AttributeDto.class
        );

        //Act
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(responseEntity.getBody());
        assertEquals("Color", responseEntity.getBody().getTitle());
    }

    @Test
    public void get_WhenAttributeDoesNotExist_ShouldThrowNotFoundException() {
        //Arrange
        int notExistsId = 99999;

        //Act
        ResponseEntity<ProblemDetail> response =
                this.testRestTemplate.getForEntity("/api/v1/attributes/" + notExistsId, ProblemDetail.class);

        //Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals("Атрибут с id %d не найден".formatted(notExistsId),response.getBody().getDetail());
    }

    @Test
    public void create_WhenValidDataProvided_ShouldReturnCreatedAttribute() {
        //Arrange
        AttributeDto dto = new AttributeDto(null, "Material");
        HttpEntity<AttributeDto> request = new HttpEntity<>(dto);

        //Act
        ResponseEntity<AttributeDto> responseEntity = this.testRestTemplate.postForEntity(
                "/api/v1/attributes", request, AttributeDto.class
        );

        //Assert
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getId());
        assertEquals("Material", responseEntity.getBody().getTitle());
        assertNotNull(responseEntity.getHeaders().getLocation());
    }

    @Test
    public void create_WhenAttributeAlreadyExists_ShouldThrowAlreadyExistsException() {
        //Arrange
        AttributeDto dto = new AttributeDto(null, "Color");
        HttpEntity<AttributeDto> request = new HttpEntity<>(dto);

        //Act
        ResponseEntity<ProblemDetail> responseEntity = this.testRestTemplate.postForEntity(
                "/api/v1/attributes", request, ProblemDetail.class
        );

        //Assert
        assertTrue(responseEntity.getStatusCode().is4xxClientError());
        assertNotNull(responseEntity.getBody());
        assertEquals("Объект уже существует", responseEntity.getBody().getTitle());
    }

    @Test
    public void update_WhenValidDataProvidedAndAttributeExists_ShouldUpdateAttribute() {
        //Arrange
        AttributeDto dto = new AttributeDto(2, "New Size");
        HttpEntity<AttributeDto> request = new HttpEntity<>(dto);

        //Act
        ResponseEntity<String> responseEntity = this.testRestTemplate.exchange(
                "/api/v1/attributes/2", HttpMethod.PATCH, request, String.class
        );

        //Assert
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("Атрибут обновлён", responseEntity.getBody());

        ResponseEntity<AttributeDto> getResponse = this.testRestTemplate.getForEntity(
                "/api/v1/attributes/2", AttributeDto.class
        );
        assertNotNull(getResponse.getBody());
        assertEquals("New Size", getResponse.getBody().getTitle());
    }

    @Test
    public void update_WhenAttributeAlreadyExists_ShouldThrowAlreadyExistsException() {
        //Arrange
        AttributeDto dto = new AttributeDto(null, "Color");
        HttpEntity<AttributeDto> request = new HttpEntity<>(dto);

        //Act
        ResponseEntity<ProblemDetail> responseEntity = this.testRestTemplate.exchange(
                "/api/v1/attributes/2",
                HttpMethod.PATCH,
                request,
                ProblemDetail.class
        );

        //Assert
        assertTrue(responseEntity.getStatusCode().is4xxClientError());
        assertNotNull(responseEntity.getBody());
        assertEquals("Объект уже существует", responseEntity.getBody().getTitle());
    }

    @Test
    public void delete_WhenAttributeExists_ShouldDeleteAttribute() {
        //Act
        ResponseEntity<String> responseEntity = this.testRestTemplate.exchange(
                "/api/v1/attributes/3", HttpMethod.DELETE, null, String.class
        );

        //Assert
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("Атрибут удалён", responseEntity.getBody());

        ResponseEntity<ProblemDetail> notFoundResponse = this.testRestTemplate.getForEntity(
                "/api/v1/attributes/3",
                ProblemDetail.class
        );
        assertNotNull(notFoundResponse.getBody());
        assertTrue(notFoundResponse.getStatusCode().is4xxClientError());
        assertEquals("Не найдено", notFoundResponse.getBody().getTitle());
    }

    @Test
    public void get_WhenAttributeDoesNotExist_ShouldReturnNotFoundException() {
        //Act
        ResponseEntity<ProblemDetail> responseEntity = this.testRestTemplate.getForEntity(
                "/api/v1/attributes/1000000", ProblemDetail.class
        );

        //Assert
        assertTrue(responseEntity.getStatusCode().is4xxClientError());
        assertNotNull(responseEntity.getBody());
        assertEquals("Не найдено", responseEntity.getBody().getTitle());
    }
}

