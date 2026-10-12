package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WorkOvertimeTypeEnum;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 加班类型枚举转换
 * @date 2023/6/20 14:06
 */
@Component
public class WorkOvertimeTypeParseFunction implements IParseFunction {
    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "WORK_OVERTIME_TYPE";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        String enumStr = String.valueOf(value);
        WorkOvertimeTypeEnum workOvertimeTypeEnum = WorkOvertimeTypeEnum.valueOf(enumStr);
        return ObjectUtils.isEmpty(workOvertimeTypeEnum) ? enumStr : workOvertimeTypeEnum.getValue();
    }
}
