package com.guhecloud.platform.database.exception;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;

/**
 * 数据唯一约束异常
 *
 * @author Woody
 * @date 2021/8/9
 */
@Getter
public class UniqueViolationException extends RuntimeException {
    /**
     * 参数名称/异常信息映射表
     */
    private final Map<String, String> errors;

    public UniqueViolationException(String field) {
        this(field, "Duplicate value");
    }

    public UniqueViolationException(@NonNull String field, @NonNull String message) {
        this(ImmutableMap.of(field, message));
    }

    public UniqueViolationException(@NonNull Map<String, String> errors) {
        super(String.valueOf(errors));
        this.errors = errors;
    }
}
