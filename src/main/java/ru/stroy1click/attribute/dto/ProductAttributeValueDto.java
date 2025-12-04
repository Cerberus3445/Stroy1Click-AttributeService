package ru.stroy1click.attribute.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeValueDto implements Serializable {

    private final static Long SerialVersionUID= 1L;

    private Integer id;

    @NotNull(message = "{validation.product_attribute_value_dto.attribute_id.not_null}")
    @Min(value = 1, message = "{validation.product_attribute_value_dto.attribute_id.min}")
    private Integer attributeId;

    @NotNull(message = "{validation.product_attribute_value_dto.product_id.not_null}")
    @Min(value = 1, message = "{validation.product_attribute_value_dto.product_id.min}")
    private Integer productId;

    @NotBlank(message = "{validation.product_attribute_value_dto.value.not_blank}")
    @Length(min = 2, max = 40, message = "{validation.product_attribute_value_dto.value.length}")
    private String value;
}
