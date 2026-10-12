package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * 离岗前发起审批转换
 */
@Component
public class PostBeforeApplyFunction implements IParseFunction {
    @Override
    public String functionName() {
        return "POST_BEFORE_APPLY";
    }

    @Override
    public String apply(Object value) {
        String name = "离岗前发起审批";
        if (ObjectUtils.isNotEmpty(value)) {
            name = "true".equals(String.valueOf(value)) ? "离岗前发起审批" : "无限制";
        }
        return name;
    }
}
