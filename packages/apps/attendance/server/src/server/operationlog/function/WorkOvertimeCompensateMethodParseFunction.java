package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeCompensateMethodEnum;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 加班补偿方式转换
 * @date 2023/6/20 13:57
 */
@Component
public class WorkOvertimeCompensateMethodParseFunction implements IParseFunction {
    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "WORK_OVERTIME_COMPENSATE_METHOD";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        String enumStr = String.valueOf(value);
        WorkOvertimeCompensateMethodEnum methodEnum = WorkOvertimeCompensateMethodEnum.valueOf(enumStr);
        return ObjectUtils.isEmpty(methodEnum) ? enumStr : methodEnum.getValue();
    }
}
