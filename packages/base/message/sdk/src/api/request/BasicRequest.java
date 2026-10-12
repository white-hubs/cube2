package com.guhecloud.cube.base.message.sdk.api.request;

import com.guhecloud.cube.base.message.sdk.common.annotation.Validation;
import com.guhecloud.cube.base.message.sdk.common.exception.ModelValidateException;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@SuperBuilder
@NoArgsConstructor
public class BasicRequest {

    public void validate() {
        Field[] fields = this.getClass().getFields();
        Object object;
        Validation validation;
        String pattern;
        int maxLength;
        int minLength;
        double maximum;
        double minimum;
        boolean required;
        try {
            for (int i = 0; i < fields.length; i++) {
                object = fields[i].get(this);
                validation = fields[i].getAnnotation(Validation.class);
                if (null != validation) {
                    required = validation.required();
                } else {
                    required = false;
                }
                if (required && null == object) {
                    throw new ModelValidateException("Field " + fields[i].getName() + " is required");
                }
                if (null != validation && null != object) {
                    pattern = validation.pattern();
                    maxLength = validation.maxLength();
                    minLength = validation.minLength();
                    maximum = validation.maximum();
                    minimum = validation.minimum();
                    if (!"".equals(pattern) || maxLength > 0 || minLength > 0 || maximum != Double.MAX_VALUE ||
                        minimum != Double.MIN_VALUE) {
                        determineType(fields[i].getType(), object, pattern, maxLength, minLength, maximum, minimum, fields[i].getName());
                    }
                }
            }
        } catch (Exception e) {
            throw new ModelValidateException(e.getMessage(), e);
        }
    }

    private void validateMap(String pattern, int maxLength, int minLength, double maximum, double minimum, Map<String, Object> map,
                             String fieldName
                            ) {
        for (Map.Entry entry : map.entrySet()) {
            if (entry.getValue() != null) {
                determineType(entry.getValue().getClass(), entry.getValue(), pattern, maxLength, minLength, maximum, minimum, fieldName);
            }
        }
    }

    private void determineType(Class clazz, Object object, String pattern, int maxLength, int minLength, double maximum, double minimum,
                               String fieldName
                              ) {
        if (Map.class.isAssignableFrom(clazz)) {
            validateMap(pattern, maxLength, minLength, maximum, minimum, (Map<String, Object>) object, fieldName);
        } else if (BasicRequest.class.isAssignableFrom(clazz)) {
            ((BasicRequest) object).validate();
        } else if (List.class.isAssignableFrom(clazz)) {
            List<?> list = (List<?>) object;
            for (int j = 0; j < list.size(); j++) {
                determineType(list.get(j).getClass(), list.get(j), pattern, maxLength, minLength, maximum, minimum, fieldName);
            }
        } else if (clazz.isArray()) {
            Object[] objects = (Object[]) object;
            for (int j = 0; j < objects.length; j++) {
                determineType(clazz.getComponentType(), objects[j], pattern, maxLength, minLength, maximum, minimum, fieldName);
            }
        } else if (Number.class.isAssignableFrom(clazz)) {
            double value = Double.valueOf(object.toString());
            if (value > maximum) {
                throw new ModelValidateException(this.getClass().getName() + "." + fieldName + " exceeds the maximum");
            }
            if (value < minimum) {
                throw new ModelValidateException(this.getClass().getName() + "." + fieldName + " less than minimum");
            }
        } else {
            String value = String.valueOf(object);
            if (maxLength > 0 && value.length() > maxLength) {
                throw new ModelValidateException(this.getClass().getName() + "." + fieldName + " exceeds the maximum length");
            }
            if (minLength > 0 && value.length() < minLength) {
                throw new ModelValidateException(this.getClass().getName() + "." + fieldName + " less than minimum length");
            }
            if (!"".equals(pattern) && !Pattern.matches(pattern, value)) {
                throw new ModelValidateException(this.getClass().getName() + "." + fieldName + " regular match failed");
            }
        }
    }
}
