package everycoding.nalseebackend.post;

import everycoding.nalseebackend.post.domain.Post;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostSpecification {

    public Specification<Post> hasWeatherIn(List<String> weathers) {
        return ((root, query, criteriaBuilder) -> {
            if (weathers == null || weathers.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("weather").in(weathers);
        });
    }

    public Specification<Post> isTemperatureBetween(Double minTemperature, Double maxTemperature) {
        return ((root, query, criteriaBuilder) -> {
            if (minTemperature == null && maxTemperature == null) {
                return criteriaBuilder.conjunction();
            } else if (minTemperature == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("temperature"), maxTemperature);
            } else if (maxTemperature == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("temperature"), minTemperature);
            } else {
                return criteriaBuilder.between(root.get("temperature"), minTemperature, maxTemperature);
            }
        });
    }

    public Specification<Post> isHeightBetween(Double minHeight, Double maxHeight) {
        return ((root, query, criteriaBuilder) -> {
            if (minHeight == null && maxHeight == null) {
                return criteriaBuilder.conjunction();
            } else if (minHeight == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("height"), maxHeight);
            } else if (maxHeight == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("height"), minHeight);
            } else {
                return criteriaBuilder.between(root.get("height"), minHeight, maxHeight);
            }
        });
    }

    public Specification<Post> isWeightBetween(Double minWeight, Double maxWeight) {
        return ((root, query, criteriaBuilder) -> {
            if (minWeight == null && maxWeight == null) {
                return criteriaBuilder.conjunction();
            } else if (minWeight == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("weight"), maxWeight);
            } else if (maxWeight == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("weight"), minWeight);
            } else {
                return criteriaBuilder.between(root.get("weight"), minWeight, maxWeight);
            }
        });
    }

    public Specification<Post> hasConstitution(String constitution) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("constitution"), constitution));
    }

    public Specification<Post> hasStyleIn(List<String> styles) {
        return ((root, query, criteriaBuilder) -> {
            if (styles == null || styles.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("style").in(styles);
        });
    }

    public Specification<Post> hasGender(String gender) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("gender"), gender));
    }
}
