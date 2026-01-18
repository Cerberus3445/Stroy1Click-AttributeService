package ru.stroy1click.attribute.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeDto implements Serializable {

    private final static Long SerialVersionUID= 1L;

    private Integer id;

    @NotNull(message = "{validation.product_attribute_value_dto.product_id.not_null}")
    @Min(value = 1, message = "{validation.product_attribute_value_dto.product_id.min}")
    private Integer productId;

    @NotNull(message = "{validation.product_attribute_value_dto.product_id.not_null}")
    @Min(value = 1, message = "{validation.product_attribute_value_dto.product_id.min}")
    private Integer productTypeAttributeValueId;
}
