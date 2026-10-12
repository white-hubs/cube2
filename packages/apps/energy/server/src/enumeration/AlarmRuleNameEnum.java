package com.guhecloud.cube.srv.gen.energymanagement.server.enumeration;

import com.guhecloud.cube.srv.gen.energymanagement.server.vo.AlarmRuleNameEnumVO;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 报警规则名称枚举
 * @author: lw
 * @date: 2022/7/7
 */
@Getter
public enum AlarmRuleNameEnum {
    FLOATING_TOPLIMIT("floating_toplimit", "周期浮动上限超标"),
    RATED_CONSUMPTION("rated_consumption", "周期额定用量超标"),
    ZERO_DOSAGE_DURATION("zero_dosage_duration", "零用量持续时间过长"),
    ;

    private final String code;
    private final String name;

    AlarmRuleNameEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getFireinspEnum(String code) {
        for (InstrumentTypeEnum fireinspEnum : InstrumentTypeEnum.values()) {
            if (fireinspEnum.getCode().equals(code)) {
                return fireinspEnum.getName();
            }
        }
        return "";
    }

    public static List<String> getEnumTypeName() {
        ArrayList<String> enumTypeNameArr = new ArrayList<>();
        for (InstrumentTypeEnum fireinspEnum : InstrumentTypeEnum.values()) {
            String enumName = fireinspEnum.getName();
            enumTypeNameArr.add(enumName);
        }
        return enumTypeNameArr;
    }

    public static List<AlarmRuleNameEnumVO> getEnumTypeVo() {
        ArrayList<AlarmRuleNameEnumVO> enumTypeArr = new ArrayList<>();
        for (AlarmRuleNameEnum fireinspEnum : AlarmRuleNameEnum.values()) {
            enumTypeArr.add(new AlarmRuleNameEnumVO(fireinspEnum.getCode(), fireinspEnum.getName()));
        }
        return enumTypeArr;
    }

}
