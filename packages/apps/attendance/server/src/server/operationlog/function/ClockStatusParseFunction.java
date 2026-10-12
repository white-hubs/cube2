package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInEnum;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 打卡异常枚举转换
 * @date 2023/6/19 15:52
 */
@Component
public class ClockStatusParseFunction implements IParseFunction {
    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "CLOCK_STATUS";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        String enumStr = String.valueOf(value);
        return ClockInEnum.valueOf(enumStr).getRemarks();
    }
}
