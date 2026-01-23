package ru.stroy1click.attribute.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class AttributeOptionDto implements Serializable {

    private final static Long SerialVersionUID= 1L;

    private Integer id;

    @NotNull(message = "{validation.attribute_option_dto.attribute_id.not_null}")
    @Positive(message = "{validation.attribute_option_dto.attribute_id.min}")
    private Integer attributeId;

    @NotNull(message = "{validation.attribute_option_dto.product_type_id.not_null}")
    @Positive(message = "{validation.attribute_option_dto.product_type_id.min}")
    private Integer productTypeId;

    @NotBlank(message = "{validation.attribute_option_dto.value.not_blank}")
    @Length(min = 2, max = 40, message = "{validation.attribute_option_dto.value.length}")
    private String value;
}
