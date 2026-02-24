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
import ru.stroy1click.attribute.dto.ProductAttributeAssignmentDto;
import ru.stroy1click.attribute.service.ProductAttributeAssignmentService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(controllers = ProductAttributeAssignmentController.class)
public class ProductAttributeAssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductAttributeAssignmentService productAttributeAssignment;

    @Test
    public void create_WhenProductAttributeAssignmentDtoProductIdIsNegative_ShouldThrowValidationException() throws Exception {
        //Arrange
        ProductAttributeAssignmentDto dtoWithNegativeProductId = new ProductAttributeAssignmentDto(null, -11, 1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/product-attribute-assignments")
                .content(new ObjectMapper().writeValueAsString(dtoWithNegativeProductId))
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
        assertTrue(problemDetail.getDetail().contains("Id продукта не может быть меньше 1"));
    }

    @Test
    public void create_WhenProductAttributeAssignmentDtoProductIdIsNull_ShouldThrowValidationException() throws Exception {
        //Arrange
        ProductAttributeAssignmentDto dtoWithNegativeProductId = new ProductAttributeAssignmentDto(null, null, 1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/product-attribute-assignments")
                .content(new ObjectMapper().writeValueAsString(dtoWithNegativeProductId))
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
        assertTrue(problemDetail.getDetail().contains("Id продукта не может быть пустым"));
    }

    @Test
    public void create_WhenProductAttributeAssignmentDtoAttributeIdIsNegative_ShouldThrowValidationException() throws Exception {
        //Arrange
        ProductAttributeAssignmentDto dtoWithNegativeProductId = new ProductAttributeAssignmentDto(null, 11, -1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/product-attribute-assignments")
                .content(new ObjectMapper().writeValueAsString(dtoWithNegativeProductId))
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
        System.out.println(problemDetail.getDetail());

        assertTrue(problemDetail.getDetail().contains("Id атрибута не может быть меньше 1"));
    }

    @Test
    public void create_WhenProductAttributeAssignmentDtoAttributeIdIsNull_ShouldThrowValidationException() throws Exception {
        //Arrange
        ProductAttributeAssignmentDto dtoWithNegativeProductId = new ProductAttributeAssignmentDto(null, 11, null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/product-attribute-assignments")
                .content(new ObjectMapper().writeValueAsString(dtoWithNegativeProductId))
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
    public void update_WhenProductAttributeAssignmentDtoProductIdIsNegative_ShouldThrowValidationException() throws Exception {
        //Arrange
        ProductAttributeAssignmentDto dtoWithNegativeProductId = new ProductAttributeAssignmentDto(null, -11, 1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/v1/product-attribute-assignments/1")
                .content(new ObjectMapper().writeValueAsString(dtoWithNegativeProductId))
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
        assertTrue(problemDetail.getDetail().contains("Id продукта не может быть меньше 1"));
    }

    @Test
    public void update_WhenProductAttributeAssignmentDtoProductIdIsNull_ShouldThrowValidationException() throws Exception {
        //Arrange
        ProductAttributeAssignmentDto dtoWithNegativeProductId = new ProductAttributeAssignmentDto(null, null, 1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/v1/product-attribute-assignments/1")
                .content(new ObjectMapper().writeValueAsString(dtoWithNegativeProductId))
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
        assertTrue(problemDetail.getDetail().contains("Id продукта не может быть пустым"));
    }

    @Test
    public void update_WhenProductAttributeAssignmentDtoAttributeIdIsNegative_ShouldThrowValidationException() throws Exception {
        //Arrange
        ProductAttributeAssignmentDto dtoWithNegativeProductId = new ProductAttributeAssignmentDto(null, 11, -1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/v1/product-attribute-assignments/1")
                .content(new ObjectMapper().writeValueAsString(dtoWithNegativeProductId))
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
    public void update_WhenProductAttributeAssignmentDtoAttributeIdIsNull_ShouldThrowValidationException() throws Exception {
        //Arrange
        ProductAttributeAssignmentDto dtoWithNegativeProductId = new ProductAttributeAssignmentDto(null, 11, null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/v1/product-attribute-assignments/1")
                .content(new ObjectMapper().writeValueAsString(dtoWithNegativeProductId))
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
}
