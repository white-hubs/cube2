package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.enums.AttDurationCalcWayEnum;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * 离岗时长核算方式转换
 */
@Component
public class PostCalculationWayFunction implements IParseFunction {
    @Override
    public String functionName() {
        return "POST_CALCULATION_WAY";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        String enumStr = String.valueOf(value);
        return AttDurationCalcWayEnum.valueOf(enumStr).getValue();
    }
}
