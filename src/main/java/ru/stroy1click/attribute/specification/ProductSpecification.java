package ru.stroy1click.attribute.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.stroy1click.attribute.entity.Attribute;
import ru.stroy1click.attribute.entity.ProductAttributeValue;
import ru.stroy1click.attribute.model.ProductAttributeValueFilter;

import java.util.Map;

public class ProductSpecification {

    public static Specification<ProductAttributeValue> filterByAttributes(ProductAttributeValueFilter filter) {
        return (root, query, cb) -> {
            query.distinct(true); // чтобы не было дубликатов продуктов

            Join<ProductAttributeValue, Attribute> joinAttr = root.join("attribute");

            Predicate predicate = cb.conjunction();

            for (Map.Entry<String, String> entry : filter.getAttributes().entrySet()) {
                predicate = cb.and(predicate,
                        cb.and(
                                cb.equal(joinAttr.get("title"), entry.getKey()),
                                cb.equal(root.get("value"), entry.getValue())
                        )
                );
            }

            return predicate;
        };
    }

}
