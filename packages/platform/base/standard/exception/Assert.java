package com.guhecloud.platform.base.standard.exception;

import com.guhecloud.platform.base.util.ObjectUtils;

import java.util.Objects;

/**
 * 业务异常快速断言工具类
 */
public final class Assert {
    private Assert() {
    }

    /**
     * 非空资源校验
     *
     * @param resource 资源对象
     */
    public static void isNotFound(Object resource) {
        isNotFound(resource, "resource not found");
    }

    /**
     * 非空资源校验
     *
     * @param resource 资源对象
     * @param message  异常信息
     */
    public static void isNotFound(Object resource, String message) {
        isNotFound(Objects.isNull(resource), message);
    }

    /**
     * 非空资源校验
     *
     * @param expression   验证表达式
     * @param errorMessage 异常信息
     */
    public static void isNotFound(boolean expression, String errorMessage, String errorCode, Object errorDetails) {
        if (expression) {
            throw new ResourceNotFoundException(errorMessage, errorCode, errorDetails);
        }
    }

    public static void isNotFound(boolean expression, String errorMessage, String errorCode) {
        isNotFound(expression, errorMessage, errorCode, null);
    }

    public static void isNotFound(boolean expression, String message) {
        isNotFound(expression, message, null);
    }

    /**
     * 参数错误校验
     *
     * @param expression   验证表达式
     * @param errorMessage 错误描述
     * @param errorCode    错误码
     * @param errorDetails 错误详情
     */
    public static void isParameterError(boolean expression, String errorMessage, String errorCode, Object errorDetails) {
        if (expression) {
            throw new InvalidArgumentException(errorMessage, errorCode, errorDetails);
        }
    }

    public static void isParameterError(boolean expression, String errorMessage, String errorCode) {
        isParameterError(expression, errorMessage, errorCode, null);
    }

    public static void isParameterError(boolean expression, String errorMessage) {
        isParameterError(expression, errorMessage, null);
    }

    public static void isParameterError(boolean expression, ErrorSpec errorSpec) {
        isParameterError(expression, errorSpec.errorMessage(), errorSpec.errorCode());
    }

    /**
     * 非空资源校验
     *
     * @param expression   验证表达式
     * @param errorCode    错误码
     * @param errorMessage 错误描述
     * @param errorDetails 错误详情
     *
     * @deprecated 直接使用 isParameterError 方法
     */
    @Deprecated
    public static void parameterIsNull(Object expression, String errorMessage, String errorCode, Object errorDetails) {
        var error = (expression instanceof Boolean && (Boolean) expression) || ObjectUtils.isEmpty(expression);
        isParameterError(error, errorMessage, errorCode, errorDetails);
    }

    /**
     * @deprecated 直接使用 isParameterError 方法
     */
    @Deprecated
    public static void parameterIsNull(Object expression, String errorMessage, String errorCode) {
        parameterIsNull(expression, errorMessage, errorCode, null);
    }

    /**
     * @deprecated 直接使用 isParameterError 方法
     */
    @Deprecated
    public static void parameterIsNull(Object expression, String message) {
        parameterIsNull(expression, message, null);
    }

    /**
     * @deprecated 直接使用 isParameterError 方法
     */
    @Deprecated
    public static void parameterIsNull(Object expression, ErrorSpec errorSpec) {
        parameterIsNull(expression, errorSpec.errorMessage(), errorSpec.errorCode());
    }

    /**
     * 请求超出范围错误
     *
     * @param expression   验证表达式
     * @param errorMessage 错误描述
     * @param errorCode    错误码
     * @param details      错误详情
     */
    public static void isOutOfRange(boolean expression, String errorMessage, String errorCode, Object details) {
        if (expression) {
            throw new OutOfRangeException(errorMessage, errorCode, details);
        }
    }

    public static void isOutOfRange(boolean expression, String errorMessage, String errorCode) {
        isOutOfRange(expression, errorMessage, errorCode, null);
    }

    public static void isOutOfRange(boolean expression, String errorMessage) {
        isOutOfRange(expression, errorMessage, null);
    }

    public static void isOutOfRange(boolean expression, ErrorSpec errorSpec) {
        isOutOfRange(expression, errorSpec.errorMessage(), errorSpec.errorCode());
    }

    public static void isTooManyRequest(boolean expression, String errorMessage, String errorCode, Object details) {
        if (expression) {
            throw new TooManyRequestException(errorMessage, errorCode, details);
        }
    }

    public static void isTooManyRequest(boolean expression, String errorMessage, String errorCode) {
        isTooManyRequest(expression, errorMessage, errorCode, null);
    }

    public static void isTooManyRequest(boolean expression, String errorMessage) {
        isTooManyRequest(expression, errorMessage, null);
    }

    public static void isTooManyRequest(boolean expression, ErrorSpec errorSpec) {
        isTooManyRequest(expression, errorSpec.errorMessage(), errorSpec.errorCode());
    }

    /**
     * 请求的资源的内容特性无法满足请求头中的条件
     *
     * @param expression   验证表达式
     * @param errorMessage 错误描述
     * @param errorCode    错误码
     * @param details      错误详情
     */
    public static void isNotAcceptable(boolean expression, String errorMessage, String errorCode, Object details) {
        if (expression) {
            throw new ResourceNotAcceptableException(errorMessage, errorCode, details);
        }
    }

    public static void isNotAcceptable(boolean expression, String errorMessage, String errorCode) {
        isNotAcceptable(expression, errorMessage, errorCode, null);
    }

    public static void isNotAcceptable(boolean expression, String errorMessage) {
        isNotAcceptable(expression, errorMessage, null);
    }

    public static void isNotAcceptable(boolean expression, ErrorSpec errorSpec) {
        isNotAcceptable(expression, errorSpec.errorMessage(), errorSpec.errorCode());
    }

    /**
     * 资源冲突错误
     *
     * @param expression   验证表达式
     * @param errorMessage 错误描述
     * @param errorCode    错误码
     * @param details      错误详情
     */
    public static void isConflict(boolean expression, String errorMessage, String errorCode, Object details) {
        if (expression) {
            throw new ResourceConflictException(errorMessage, errorCode, details);
        }
    }

    public static void isConflict(boolean expression, String errorMessage, String errorCode) {
        isConflict(expression, errorMessage, errorCode, null);
    }

    public static void isConflict(boolean expression, String errorMessage) {
        isConflict(expression, errorMessage, null);
    }

    public static void isConflict(boolean expression, ErrorSpec errorSpec) {
        isConflict(expression, errorSpec.errorMessage(), errorSpec.errorCode());
    }

    /**
     * 权限拒绝错误
     *
     * @param expression   验证表达式
     * @param errorMessage 错误描述
     * @param errorCode    错误码
     * @param details      错误详情
     */
    public static void isPermissionDenied(boolean expression, String errorMessage, String errorCode, Object details) {
        if (expression) {
            throw new PermissionDeniedException(errorMessage, errorCode, details);
        }
    }

    public static void isPermissionDenied(boolean expression, String errorMessage, String errorCode) {
        isPermissionDenied(expression, errorMessage, errorCode, null);
    }

    public static void isPermissionDenied(boolean expression, String errorMessage) {
        isPermissionDenied(expression, errorMessage, null);
    }

    public static void isPermissionDenied(boolean expression, ErrorSpec errorSpec) {
        isPermissionDenied(expression, errorSpec.errorMessage(), errorSpec.errorCode());
    }

    /**
     * 未知错误
     * 通常代表无法精确定位或尚未精确定位的服务内部错误
     *
     * @param expression   验证表达式
     * @param errorMessage 错误描述
     * @param errorCode    错误码
     * @param errorDetails 错误详情
     */
    public static void isUnknownError(boolean expression, String errorMessage, String errorCode, Object errorDetails) {
        if (expression) {
            throw new ServiceException(errorMessage, errorCode, errorDetails);
        }
    }

    public static void isUnknownError(boolean expression, String errorMessage, String errorCode) {
        isUnknownError(expression, errorMessage, errorCode, null);
    }

    public static void isUnknownError(boolean expression, String errorMessage) {
        isUnknownError(expression, errorMessage, null);
    }

    public static void isUnknownError(boolean expression, ErrorSpec errorSpec) {
        isUnknownError(expression, errorSpec.errorMessage(), errorSpec.errorCode());
    }

    /**
     * 未认证错误
     *
     * @param expression   验证表达式
     * @param errorMessage 错误描述
     * @param errorCode    错误码
     * @param details      错误详情
     */
    public static void isUnauthenticated(boolean expression, String errorMessage, String errorCode, Object details) {
        if (expression) {
            throw new UnauthenticatedException(errorMessage, errorCode, details);
        }
    }

    public static void isUnauthenticated(boolean expression, String errorMessage, String errorCode) {
        isUnauthenticated(expression, errorMessage, errorCode, null);
    }

    public static void isUnauthenticated(boolean expression, String errorMessage) {
        isUnauthenticated(expression, errorMessage, null);
    }

    public static void isUnauthenticated(boolean expression, ErrorSpec errorSpec) {
        isUnauthenticated(expression, errorSpec.errorMessage(), errorSpec.errorCode());
    }

    public static void isUnsupported(boolean expression, String errorMessage, String errorCode, Object details) {
        if (expression) {
            throw new UnimplementedException(errorMessage, errorCode, details);
        }
    }

    public static void isUnsupported(boolean expression, String errorMessage, String errorCode) {
        isUnsupported(expression, errorMessage, errorCode, null);
    }

    public static void isUnsupported(boolean expression, String errorMessage) {
        isUnsupported(expression, errorMessage, null);
    }

    public static void isUnsupported(boolean expression, ErrorSpec errorSpec) {
        isUnsupported(expression, errorSpec.errorMessage(), errorSpec.errorCode());
    }
}
