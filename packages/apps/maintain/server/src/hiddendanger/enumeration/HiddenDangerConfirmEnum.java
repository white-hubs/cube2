package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * @Author wql
 * @Description 隐患单确认结果枚举
 * @Date 19:34 2023/11/8
 **/
@Getter
public enum HiddenDangerConfirmEnum {

    CONFIRMED("确认隐患"),

    NO_PROCESS("暂不处理"),
    ;

    public final String desc;

    HiddenDangerConfirmEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(HiddenDangerConfirmEnum.valueOf(name)) ? "" :
               HiddenDangerConfirmEnum.valueOf(name).getDesc();
    }
}
