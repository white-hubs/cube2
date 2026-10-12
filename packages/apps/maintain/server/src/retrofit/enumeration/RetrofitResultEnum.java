package com.guhecloud.cube.srv.gen.maintain.server.retrofit.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * 更新改造结果枚举
 *
 * @author lanchun
 * @date 2024/1/11 9:52
 */
@Getter
public enum RetrofitResultEnum {

    UNRESOLVED("未解决"),

    RESOLVED("已解决");

    public final String desc;

    RetrofitResultEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(RetrofitResultEnum.valueOf(name)) ? "" :
               RetrofitResultEnum.valueOf(name).getDesc();
    }

}
