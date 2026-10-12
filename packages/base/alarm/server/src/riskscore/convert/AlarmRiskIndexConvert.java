package com.guhecloud.cube.base.alarm.server.riskscore.convert;

import com.guhecloud.cube.srv.gen.riskscore.sdk.enumeration.RiskIndexItemEnum;

public class AlarmRiskIndexConvert {

    private AlarmRiskIndexConvert() {

    }

    public static Integer[] getAlarmIndexItem(RiskIndexItemEnum riskIndexItemEnum) {
        if (RiskIndexItemEnum.ONE_LEVEL == riskIndexItemEnum) {
            return new Integer[]{ 1 };
        } else if (RiskIndexItemEnum.TWO_LEVEL == riskIndexItemEnum) {
            return new Integer[]{ 2 };
        } else if (RiskIndexItemEnum.THREE_LEVEL_AND_FOUR_LEVEL == riskIndexItemEnum) {
            return new Integer[]{ 3, 4 };
        }
        return new Integer[0];
    }
}
