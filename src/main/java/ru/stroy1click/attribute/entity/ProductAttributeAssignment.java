package ru.stroy1click.attribute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(schema = "attribute", name = "product_attribute_assignments")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productId;

    @ManyToOne
    @JoinColumn(name = "attribute_option_id", referencedColumnName = "id")
    private AttributeOption attributeOption;
}