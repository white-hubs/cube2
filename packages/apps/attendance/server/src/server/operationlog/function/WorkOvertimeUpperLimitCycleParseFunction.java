package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeUpperLimitCycleEnum;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @author lijie
 * @describe 加班限制周期
 * @date 2024/1/17 14:01
 */
@Component
public class WorkOvertimeUpperLimitCycleParseFunction implements IParseFunction {

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "WORK_OVERTIME_UPPER_LIMIT_CYCLE";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        String enumStr = String.valueOf(value);
        WorkOvertimeUpperLimitCycleEnum durationEnum = WorkOvertimeUpperLimitCycleEnum.valueOf(enumStr);
        return ObjectUtils.isEmpty(durationEnum) ? enumStr : durationEnum.getValue();
    }
}
