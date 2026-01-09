package ru.stroy1click.attribute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(schema = "attribute", name = "product_type_attribute_values")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeAttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String value;

    @ManyToOne
    @JoinColumn(name = "attribute_id", referencedColumnName = "id")
    private Attribute attribute;

    private Integer productTypeId;
}