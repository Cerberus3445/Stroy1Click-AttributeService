package ru.stroy1click.attribute.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.stroy1click.attribute.dto.AttributeOptionDto;
import ru.stroy1click.attribute.service.AttributeOptionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(controllers = AttributeOptionController.class)
public class AttributeOptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AttributeOptionService attributeOptionService;

    @Test
    void create_AttributeOptionDtoValueIsShort_ShouldThrowValidationException() throws Exception {
        //Arrange
        AttributeOptionDto dtoWithShorValue = new AttributeOptionDto(null, 1, 1, "x");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/attribute-options")
                .content(new ObjectMapper().writeValueAsString(dtoWithShorValue))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //Act
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        ProblemDetail problemDetail = new ObjectMapper().readValue(response,ProblemDetail.class);
        int status = result.getResponse().getStatus();

        //Assert
        assertEquals(400, status);
        Assertions.assertNotNull(problemDetail.getDetail());
        assertTrue(problemDetail.getDetail().contains("Минимальная длина значения составляет 2 символа, а максимальная - 40"));
        assertEquals("Ошибка валидации", problemDetail.getTitle());
    }

    @Test
    void create_AttributeOptionDtoValueIsEmpty_ShouldThrowValidationException() throws Exception {
        //Arrange
        AttributeOptionDto dtoWithShorValue = new AttributeOptionDto(null, 1, 1, "");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/attribute-options")
                .content(new ObjectMapper().writeValueAsString(dtoWithShorValue))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //Act
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        ProblemDetail problemDetail = new ObjectMapper().readValue(response,ProblemDetail.class);
        int status = result.getResponse().getStatus();

        //Assert
        assertEquals(400, status);
        Assertions.assertNotNull(problemDetail.getDetail());
        assertEquals("Ошибка валидации", problemDetail.getTitle());
        assertTrue(problemDetail.getDetail().contains("Значение не может быть пустым"));
    }

    @Test
    void create_AttributeOptionDtoAttributeIdIsNull_ShouldThrowValidationException() throws Exception {
        //Arrange
        AttributeOptionDto dtoWithShorValue = new AttributeOptionDto(null, null, 1, "Value");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/attribute-options")
                .content(new ObjectMapper().writeValueAsString(dtoWithShorValue))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //Act
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        ProblemDetail problemDetail = new ObjectMapper().readValue(response,ProblemDetail.class);
        int status = result.getResponse().getStatus();

        //Assert
        assertEquals(400, status);
        Assertions.assertNotNull(problemDetail.getDetail());
        assertEquals("Ошибка валидации", problemDetail.getTitle());
        assertTrue(problemDetail.getDetail().contains("Id атрибута не может быть пустым"));
    }

    @Test
    void create_AttributeOptionDtoAttributeIdIsLessThanOne_ShouldThrowValidationException() throws Exception {
        //Arrange
        AttributeOptionDto dtoWithShorValue = new AttributeOptionDto(null, -1, 1, "Value");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/attribute-options")
                .content(new ObjectMapper().writeValueAsString(dtoWithShorValue))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //Act
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        ProblemDetail problemDetail = new ObjectMapper().readValue(response,ProblemDetail.class);
        int status = result.getResponse().getStatus();

        //Assert
        assertEquals(400, status);
        Assertions.assertNotNull(problemDetail.getDetail());
        assertEquals("Ошибка валидации", problemDetail.getTitle());
        assertTrue(problemDetail.getDetail().contains("Id атрибута не может быть меньше 1"));
    }

    @Test
    void create_AttributeOptionDtoProductTypeIdIsNull_ShouldThrowValidationException() throws Exception {
        //Arrange
        AttributeOptionDto dtoWithShorValue = new AttributeOptionDto(null, 1, null, "Value");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/attribute-options")
                .content(new ObjectMapper().writeValueAsString(dtoWithShorValue))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //Act
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        ProblemDetail problemDetail = new ObjectMapper().readValue(response,ProblemDetail.class);
        int status = result.getResponse().getStatus();

        //Assert
        assertEquals(400, status);
        Assertions.assertNotNull(problemDetail.getDetail());
        assertEquals("Ошибка валидации", problemDetail.getTitle());
        assertTrue(problemDetail.getDetail().contains("Id типа продукта не может быть пустым"));
    }

    @Test
    void create_AttributeOptionDtoProductTypeIdIsLessThanOne_ShouldThrowValidationException() throws Exception {
        //Arrange
        AttributeOptionDto dtoWithShorValue = new AttributeOptionDto(null, 1, -1, "Value");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/attribute-options")
                .content(new ObjectMapper().writeValueAsString(dtoWithShorValue))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //Act
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        ProblemDetail problemDetail = new ObjectMapper().readValue(response,ProblemDetail.class);
        int status = result.getResponse().getStatus();

        //Assert
        assertEquals(400, status);
        Assertions.assertNotNull(problemDetail.getDetail());
        assertEquals("Ошибка валидации", problemDetail.getTitle());
        assertTrue(problemDetail.getDetail().contains("Id типа продукта не может быть меньше 1"));
    }

    @Test
    void update_AttributeOptionDtoValueIsShort_ShouldThrowValidationException() throws Exception {
        //Arrange
        AttributeOptionDto dtoWithShorValue = new AttributeOptionDto(null, 1, 1, "C");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/v1/attribute-options/1")
                .content(new ObjectMapper().writeValueAsString(dtoWithShorValue))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //Act
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        ProblemDetail problemDetail = new ObjectMapper().readValue(response,ProblemDetail.class);
        int status = result.getResponse().getStatus();

        //Assert
        assertEquals(400, status);
        Assertions.assertNotNull(problemDetail.getDetail());
        assertTrue(problemDetail.getDetail().contains("Минимальная длина значения составляет 2 символа, а максимальная - 40"));
        assertEquals("Ошибка валидации", problemDetail.getTitle());
    }

    @Test
    void update_AttributeOptionDtoValueIsEmpty_ShouldThrowValidationException() throws Exception {
        //Arrange
        AttributeOptionDto dtoWithShorValue = new AttributeOptionDto(null, 1, 1, "");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/v1/attribute-options/1")
                .content(new ObjectMapper().writeValueAsString(dtoWithShorValue))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //Act
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        ProblemDetail problemDetail = new ObjectMapper().readValue(response,ProblemDetail.class);
        int status = result.getResponse().getStatus();

        //Assert
        assertEquals(400, status);
        Assertions.assertNotNull(problemDetail.getDetail());
        assertEquals("Ошибка валидации", problemDetail.getTitle());
        assertTrue(problemDetail.getDetail().contains("Значение не может быть пустым"));
    }

    @Test
    void update_AttributeOptionDtoAttributeIdIsNull_ShouldThrowValidationException() throws Exception {
        //Arrange
        AttributeOptionDto dtoWithShorValue = new AttributeOptionDto(null, null, 1, "Value");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/v1/attribute-options/1")
                .content(new ObjectMapper().writeValueAsString(dtoWithShorValue))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //Act
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        ProblemDetail problemDetail = new ObjectMapper().readValue(response,ProblemDetail.class);
        int status = result.getResponse().getStatus();

        //Assert
        assertEquals(400, status);
        Assertions.assertNotNull(problemDetail.getDetail());
        assertEquals("Ошибка валидации", problemDetail.getTitle());
        assertTrue(problemDetail.getDetail().contains("Id атрибута не может быть пустым"));
    }

    @Test
    void update_AttributeOptionDtoAttributeIdIsLessThanOne_ShouldThrowValidationException() throws Exception {
        //Arrange
        AttributeOptionDto dtoWithShorValue = new AttributeOptionDto(null, -1, 1, "Value");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/v1/attribute-options/1")
                .content(new ObjectMapper().writeValueAsString(dtoWithShorValue))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //Act
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        ProblemDetail problemDetail = new ObjectMapper().readValue(response,ProblemDetail.class);
        int status = result.getResponse().getStatus();

        //Assert
        assertEquals(400, status);
        Assertions.assertNotNull(problemDetail.getDetail());
        assertEquals("Ошибка валидации", problemDetail.getTitle());
        assertTrue(problemDetail.getDetail().contains("Id атрибута не может быть меньше 1"));
    }

    @Test
    void update_AttributeOptionDtoProductTypeIdIsNull_ShouldThrowValidationException() throws Exception {
        //Arrange
        AttributeOptionDto dtoWithShorValue = new AttributeOptionDto(null, 1, null, "Value");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/attribute-options")
                .content(new ObjectMapper().writeValueAsString(dtoWithShorValue))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //Act
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        ProblemDetail problemDetail = new ObjectMapper().readValue(response,ProblemDetail.class);
        int status = result.getResponse().getStatus();

        //Assert
        assertEquals(400, status);
        Assertions.assertNotNull(problemDetail.getDetail());
        assertEquals("Ошибка валидации", problemDetail.getTitle());
        assertTrue(problemDetail.getDetail().contains("Id типа продукта не может быть пустым"));
    }

    @Test
    void update_AttributeOptionDtoProductTypeIdIsLessThanOne_ShouldThrowValidationException() throws Exception {
        //Arrange
        AttributeOptionDto dtoWithShorValue = new AttributeOptionDto(null, 1, -1, "Value");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/v1/attribute-options/1")
                .content(new ObjectMapper().writeValueAsString(dtoWithShorValue))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //Act
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        ProblemDetail problemDetail = new ObjectMapper().readValue(response,ProblemDetail.class);
        int status = result.getResponse().getStatus();

        //Assert
        assertEquals(400, status);
        Assertions.assertNotNull(problemDetail.getDetail());
        assertEquals("Ошибка валидации", problemDetail.getTitle());
        assertTrue(problemDetail.getDetail().contains("Id типа продукта не может быть меньше 1"));
    }
}