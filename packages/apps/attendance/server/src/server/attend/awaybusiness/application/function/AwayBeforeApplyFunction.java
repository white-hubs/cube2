package com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.application.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  10:51
 * @Description:
 * @Version: 1.0
 */
@Component
public class AwayBeforeApplyFunction implements IParseFunction {
    @Override
    public String functionName() {
        return "AWAY_BEFORE_APPLY";
    }

    @Override
    public String apply(Object value) {
        String name = "出差前发起审批";
        if (ObjectUtils.isNotEmpty(value)) {
            name = "true".equals(String.valueOf(value)) ? "出差前发起审批" : "无限制";
        }
        return name;
    }
}
