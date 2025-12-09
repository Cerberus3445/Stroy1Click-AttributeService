package ru.stroy1click.attribute.client.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import ru.stroy1click.attribute.client.ProductClient;
import ru.stroy1click.attribute.dto.ProductDto;
import ru.stroy1click.attribute.exception.NotFoundException;
import ru.stroy1click.attribute.exception.ServerErrorResponseException;
import ru.stroy1click.attribute.exception.ServiceUnavailableException;
import java.util.Locale;

@Slf4j
@Component
@CircuitBreaker(name = "productClient")
public class ProductClientImpl implements ProductClient {

    private final RestClient restClient;

    private final MessageSource messageSource;

    public ProductClientImpl(@Value(value = "${url.productService}") String url, MessageSource messageSource){
        this.restClient = RestClient.builder()
                .baseUrl(url + "/api/v1/products")
                .build();
        this.messageSource = messageSource;
    }

    @Override
    public ProductDto get(Integer id) {
        try {
            return this.restClient.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new NotFoundException(
                                this.messageSource.getMessage(
                                        "error.product.not_found",
                                        null,
                                        Locale.getDefault()
                                )
                        );
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new ServerErrorResponseException();
                    })
                    .body(ProductDto.class);
        } catch (ResourceAccessException e){
            log.error("get error ", e);
            throw new ServiceUnavailableException();
        }
    }
}
