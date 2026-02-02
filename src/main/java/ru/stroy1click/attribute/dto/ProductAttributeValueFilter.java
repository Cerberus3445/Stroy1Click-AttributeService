package ru.stroy1click.attribute.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeValueFilter {

    @Size(min = 1, message = "validation.product_attribute_value_filter.size")
    @NotNull(message = "${validation.product_attribute_value_filter.not_null}")
    private Map<String, String> attributes;
}
