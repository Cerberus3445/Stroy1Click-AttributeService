package ru.stroy1click.attribute.dto;

import jakarta.validation.constraints.NotBlank;
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
public class AttributeDto implements Serializable {

    private final static Long SerialVersionUID= 1L;

    private Integer id;

    @NotBlank(message = "{validation.attribute_dto.title.not_blank}")
    @Length(min = 2, max = 40, message = "{validation.attribute_dto.title.length}")
    private String title;
}
