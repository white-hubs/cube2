package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author wql
 * @describe 隐患级别
 * @date 2023/11/8 19:29
 */
@Getter
public enum HiddenDangerLevelEnum {
    SEVERITY("严重"),

    NORMAL("一般"),
    ;

    public final String desc;

    HiddenDangerLevelEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(HiddenDangerLevelEnum.valueOf(name)) ? "" :
               HiddenDangerLevelEnum.valueOf(name).getDesc();
    }
}
