package com.lin.bili.common.validation.validator;

import com.lin.bili.common.validation.annotation.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!Optional.of(value).isPresent()) return false;
        String phoneNum = value.toString();
        String regx = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
        return Pattern.matches(regx, phoneNum);
    }
}
