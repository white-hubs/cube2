package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeCountDurationEnum;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 加班计算方式
 * @date 2023/6/20 14:01
 */
@Component
public class WorkOvertimeCountDurationParseFunction implements IParseFunction {

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "WORK_OVERTIME_COUNT_DURATION";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        String enumStr = String.valueOf(value);
        WorkOvertimeCountDurationEnum durationEnum = WorkOvertimeCountDurationEnum.valueOf(enumStr);
        return ObjectUtils.isEmpty(durationEnum) ? enumStr : durationEnum.getValue();
    }
}
