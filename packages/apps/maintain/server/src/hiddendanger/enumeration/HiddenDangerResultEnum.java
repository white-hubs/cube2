package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * @Author wql
 * @Description 隐患解决结果
 * @Date 19:34 2023/11/8
 **/
@Getter
public enum HiddenDangerResultEnum {

    UNRESOLVED("未解决"),

    RESOLVED("已解决"),

    NO_PROCESS("暂不处理"),

    CONFIRMED("确认隐患");

    public final String desc;

    HiddenDangerResultEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(HiddenDangerResultEnum.valueOf(name)) ? "" :
               HiddenDangerResultEnum.valueOf(name).getDesc();
    }

}
