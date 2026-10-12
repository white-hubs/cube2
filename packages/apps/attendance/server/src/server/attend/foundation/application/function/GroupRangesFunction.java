package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.function;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttGroupRangesEnum;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  10:51
 * @Description:
 * @Version: 1.0
 */
@Component
public class GroupRangesFunction implements IParseFunction {
    @Override
    public String functionName() {
        return "GROUP_RANGES";
    }

    @Override
    public String apply(Object value) {
        return AttGroupRangesEnum.ALL.name().equals(String.valueOf(value)) ? AttGroupRangesEnum.ALL.getValue()
                                                                           : AttGroupRangesEnum.PART.getValue();
    }
}
