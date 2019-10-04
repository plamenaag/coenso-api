package com.primeholding.coenso.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CustomSpecificationsBuilder<T> {
    private final List<SearchCriteria> params;

    public CustomSpecificationsBuilder(String searchQuery) {
        params = new ArrayList<>();
        String regExPattern = "(\\w+?)(:|<|>|~|==)(\\w+?|[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)" +
                "*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?),";
        Pattern pattern = Pattern.compile(regExPattern);
        Matcher matcher = pattern.matcher(searchQuery + ",");
        while (matcher.find()) {
            this.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
    }

    public CustomSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<T> build() {
        if (params.isEmpty()) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(CustomSpecification::new)
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }

        return result;
    }
}
