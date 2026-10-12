package com.guhecloud.cube.srv.gen.maintain.server.fault.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * @Author wql
 * @Description 故障报修结果枚举
 * @Date 15:44 2023/11/7
 **/
@Getter
public enum FaultResultEnum {

    UNRESOLVED("未解决"),

    RESOLVED("已解决");

    public final String desc;

    FaultResultEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isNotEmpty(name)) {
            for (FaultResultEnum e : FaultResultEnum.values()) {
                if (e.name().equals(name)) {
                    return e.desc;
                }
            }
        }
        return "";
    }

}
