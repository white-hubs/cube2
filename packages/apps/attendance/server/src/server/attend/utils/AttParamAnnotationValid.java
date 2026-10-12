package com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AttParamAnnotationValid {

    /**
     * 手动验证参数是否符合注解校验，返回是否合格
     *
     * @param t 验证对象
     *
     * @return true 合格
     */
    public static <T> boolean validReturnBool(T t) {
        List<String> errorList = AttParamAnnotationValid.validMessage(t);
        return errorList.isEmpty();
    }

    /**
     * 手动验证参数是否符合注解校验，不合格抛出异常
     *
     * @param t 验证对象
     */
    public static <T> void validThrowError(T t) {
        List<String> errorList = AttParamAnnotationValid.validMessage(t);
        if (!errorList.isEmpty()) {
            throw new ServiceException(errorList.getFirst());
        }
    }

    /**
     * 手动验证参数是否符合注解校验
     *
     * @param t 验证对象
     *
     * @return 不合格的提示语集合
     */
    public static <T> List<String> validMessage(T t) {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            Set<ConstraintViolation<T>> errors = validator.validate(t);
            return errors.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        }
    }
}
