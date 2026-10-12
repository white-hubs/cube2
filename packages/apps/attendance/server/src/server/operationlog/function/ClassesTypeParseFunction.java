package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttClassesTypeEnum;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * 班次类型枚举转换
 */
@Component
public class ClassesTypeParseFunction implements IParseFunction {
    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "CLASS_TYPE";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        String enumStr = String.valueOf(value);
        return AttClassesTypeEnum.valueOf(enumStr).getValue();
    }
}
