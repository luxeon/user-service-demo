package com.example.user.repository.specification;

import com.example.user.domain.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> search(String country) {
        return (root, query, criteriaBuilder) -> {
            if (country != null && !country.isBlank()) {
                return criteriaBuilder.equal(root.get("country"), country);
            }
            return criteriaBuilder.conjunction();
        };
    }
} 