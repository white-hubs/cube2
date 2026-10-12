package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.valueobject.AttSchedulingWaysEnum;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * 排班方式枚举转换
 */
@Component
public class SchedulingWaysParseFunction implements IParseFunction {
    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "SCHEDULING_WAYS";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        String enumStr = String.valueOf(value);
        return AttSchedulingWaysEnum.valueOf(enumStr).getValue();
    }
}
