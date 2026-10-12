package com.guhecloud.cube.srv.gen.safety.management.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author wql
 * @describe 报警模块
 * @date 2023/10/17 15:33
 */
@Getter
public enum AlarmTypeEnum {
    ELECTRICMANAGE("电气管理"),
    FIRECONTROL("消防管理"),
    GAS_MANAGEMENT("用气管理"),
    SECURITY_MONITORING("安防监控");
    private final String desc;

    AlarmTypeEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(AlarmTypeEnum.valueOf(name)) ? "" :
               AlarmTypeEnum.valueOf(name).getDesc();
    }
}
