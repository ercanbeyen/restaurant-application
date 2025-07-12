package com.ercanbeyen.restaurantapplication.validator;

import com.ercanbeyen.restaurantapplication.validation.ValidEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class EnumValidator implements ConstraintValidator<ValidEnum, CharSequence> {
    private List<String> acceptedValues;
    private boolean allowNull;

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(value).isEmpty() ? allowNull : acceptedValues.contains(value.toString());
    }

    @Override
    public void initialize(ValidEnum annotation) {
        allowNull = annotation.allowNull();
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
    }
}
