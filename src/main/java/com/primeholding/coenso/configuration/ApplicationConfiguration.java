package com.primeholding.coenso.configuration;

import com.primeholding.coenso.entity.Field;
import com.primeholding.coenso.entity.FieldValue;
import com.primeholding.coenso.model.FieldPostModel;
import com.primeholding.coenso.model.FieldValuePostModel;
import com.primeholding.coenso.security.filter.JwtAuthorizationFilter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(FieldPostModel.class, Field.class)
                .addMappings(mapper -> mapper.skip(Field::setId));

        modelMapper.createTypeMap(FieldValuePostModel.class, FieldValue.class)
                .addMappings(mapper -> mapper.skip(FieldValue::setId));

        return modelMapper;
    }
}