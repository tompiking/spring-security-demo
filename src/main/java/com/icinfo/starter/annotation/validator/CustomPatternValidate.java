 package com.icinfo.starter.annotation.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.ObjectUtils;

import com.icinfo.starter.annotation.CustomPattern;

public class CustomPatternValidate implements ConstraintValidator<CustomPattern, Object> {

    private String regexp;

    @Override
    public void initialize(CustomPattern constraintAnnotation) {
        this.regexp = constraintAnnotation.regexp();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // 为空默认通过验证
        if (ObjectUtils.isEmpty(value)) {
            return true;
        }
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(value.toString());
        return m.matches();
    }

}
