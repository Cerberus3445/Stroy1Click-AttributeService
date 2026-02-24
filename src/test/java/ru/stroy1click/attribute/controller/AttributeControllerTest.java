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
import ru.stroy1click.attribute.dto.AttributeDto;
import ru.stroy1click.attribute.service.AttributeService;
import ru.stroy1click.attribute.validator.AttributeCreateValidator;
import ru.stroy1click.attribute.validator.AttributeUpdateValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(controllers = AttributeController.class)
public class AttributeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AttributeService attributeService;

    @MockitoBean
    private AttributeCreateValidator createValidator;

    @MockitoBean
    private AttributeUpdateValidator updateValidator;

    @Test
    public void create_WhenAttributeTitleIsEmpty_ShouldThrowValidationException() throws Exception {
        //Arrange
        AttributeDto dtoWithEmptyTitle = new AttributeDto(null, "");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/attributes")
                .content(new ObjectMapper().writeValueAsString(dtoWithEmptyTitle))
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
        assertTrue(problemDetail.getDetail().contains("Название атрибута не может быть пустым"));
        assertEquals("Ошибка валидации", problemDetail.getTitle());
    }

    @Test
    public void create_WhenAttributeTitleIsShort_ShouldThrowValidationException() throws Exception {
        //Arrange
        AttributeDto dtoWithShortTitle = new AttributeDto(null, "g");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/attributes")
                .content(new ObjectMapper().writeValueAsString(dtoWithShortTitle))
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
        assertTrue(problemDetail.getDetail().contains("Минимальная длина названия составляет 2 символа, а максимальная - 40"));
        assertEquals("Ошибка валидации", problemDetail.getTitle());
    }


    @Test
    public void update_WhenAttributeTitleIsEmpty_ShouldReturnValidationException() throws Exception {
        //Arrange
        AttributeDto dtoWithEmptyTitle = new AttributeDto(null, "");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/v1/attributes/1")
                .content(new ObjectMapper().writeValueAsString(dtoWithEmptyTitle))
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
        assertTrue(problemDetail.getDetail().contains("Название атрибута не может быть пустым"));
        assertEquals("Ошибка валидации", problemDetail.getTitle());
    }

    @Test
    public void update_WhenAttributeTitleIsShort_ShouldThrowValidationException() throws Exception {
        //Arrange
        AttributeDto dtoWithShortTitle = new AttributeDto(null, "g");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/v1/attributes/1")
                .content(new ObjectMapper().writeValueAsString(dtoWithShortTitle))
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
        assertTrue(problemDetail.getDetail().contains("Минимальная длина названия составляет 2 символа, а максимальная - 40"));
        assertEquals("Ошибка валидации", problemDetail.getTitle());
    }
}