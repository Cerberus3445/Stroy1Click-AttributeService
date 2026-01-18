package ru.stroy1click.attribute.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.stroy1click.attribute.entity.Attribute;
import ru.stroy1click.attribute.entity.ProductAttribute;
import ru.stroy1click.attribute.entity.ProductTypeAttributeValue;
import ru.stroy1click.attribute.model.ProductAttributeValueFilter;

import java.util.Map;

public class ProductSpecification {

    public static Specification<ProductAttribute> filterByAttributes(ProductAttributeValueFilter filter) {
        return (root, query, cb) -> {
            if (filter.getAttributes() == null || filter.getAttributes().isEmpty()) {
                return cb.conjunction();
            }

            query.distinct(true);

            Join<ProductAttribute, ProductTypeAttributeValue> typeValueJoin = root.join("productTypeAttributeValue");

            Join<ProductTypeAttributeValue, Attribute> attributeJoin = typeValueJoin.join("attribute");

            Predicate predicate = cb.conjunction();

            for (Map.Entry<String, String> entry : filter.getAttributes().entrySet()) {
                Predicate currentAttrPredicate = cb.and(
                        cb.equal(attributeJoin.get("title"), entry.getKey()),
                        cb.equal(typeValueJoin.get("value"), entry.getValue())
                );

                predicate = cb.and(predicate, currentAttrPredicate);
            }

            return predicate;
        };
    }
}
