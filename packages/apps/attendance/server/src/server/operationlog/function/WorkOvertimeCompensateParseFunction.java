package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeCompensateEnum;
import com.mzt.logapi.service.IParseFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 加班补偿枚举转换
 * @date 2023/6/20 13:53
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkOvertimeCompensateParseFunction implements IParseFunction {
    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "WORK_OVERTIME_COMPENSATE";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        String enumStr = String.valueOf(value);
        WorkOvertimeCompensateEnum anEnum = WorkOvertimeCompensateEnum.valueOf(enumStr);
        return ObjectUtils.isEmpty(anEnum) ? enumStr : anEnum.getValue();
    }
}
