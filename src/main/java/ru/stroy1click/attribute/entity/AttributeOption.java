package ru.stroy1click.attribute.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Table(schema = "attribute", name = "attribute_options")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttributeOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String value;

    private Integer productTypeId;

    @ManyToOne
    @JoinColumn(name = "attribute_id", referencedColumnName = "id")
    private Attribute attribute;

    @OneToMany(mappedBy = "attributeOption")
    private List<ProductAttributeAssignment> productAttributeAssignments;
}