package com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.application.function;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.enums.AttDurationCalcWayEnum;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  10:51
 * @Description:
 * @Version: 1.0
 */
@Component
public class AwayCalculationWayFunction implements IParseFunction {
    @Override
    public String functionName() {
        return "AWAY_CALCULATION_WAY";
    }

    @Override
    public String apply(Object value) {
        String name = "";
        if (ObjectUtils.isNotEmpty(value)) {
            AttDurationCalcWayEnum[] durationCalcWayEnums = AttDurationCalcWayEnum.values();
            for (AttDurationCalcWayEnum ex : durationCalcWayEnums) {
                if (ex.name().equals(String.valueOf(value))) {
                    return ex.getValue();
                }
            }
        }
        return name;
    }
}
