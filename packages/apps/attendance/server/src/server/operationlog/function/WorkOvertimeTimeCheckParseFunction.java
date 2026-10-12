package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeTimeCheckEnum;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @description: 加班审批条件
 * @author: lijie
 * @date: 2023/12/25 19:04
 **/
@Component
public class WorkOvertimeTimeCheckParseFunction implements IParseFunction {

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "WORK_OVERTIME_TIME_CHECK";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        String enumStr = String.valueOf(value);
        WorkOvertimeTimeCheckEnum timeCheckEnum = WorkOvertimeTimeCheckEnum.valueOf(value.toString());
        return ObjectUtils.isEmpty(timeCheckEnum) ? enumStr : timeCheckEnum.getValue();
    }
}
