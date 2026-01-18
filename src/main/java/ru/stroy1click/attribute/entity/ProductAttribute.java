package ru.stroy1click.attribute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(schema = "attribute", name = "product_attributes")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productId;

    @ManyToOne
    @JoinColumn(name = "product_type_attribute_value_id", referencedColumnName = "id")
    private ProductTypeAttributeValue productTypeAttributeValue;
}