package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeCountUnitsEnum;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @description: 加班计算单位
 * @author: lijie
 * @date: 2023/12/25 19:04
 **/
@Component
public class WorkOvertimeOvertimeUnitParseFunction implements IParseFunction {

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "WORK_OVERTIME_OVERTIME_UNIT";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        String enumStr = String.valueOf(value);
        WorkOvertimeCountUnitsEnum timeCheckEnum = WorkOvertimeCountUnitsEnum.valueOf(value.toString());
        return ObjectUtils.isEmpty(timeCheckEnum) ? enumStr : timeCheckEnum.getValue();
    }
}
