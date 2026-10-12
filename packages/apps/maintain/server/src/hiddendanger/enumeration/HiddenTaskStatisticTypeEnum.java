package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author wql
 * @describe 隐患统计类型枚举
 * @date 2023/11/12 13:40
 */
@Getter
public enum HiddenTaskStatisticTypeEnum {

    HIDDEN_DANGER_LEVEL("隐患级别"),
    HIDDEN_DANGER_TYPE("隐患分类");

    private final String desc;

    HiddenTaskStatisticTypeEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(HiddenTaskStatisticTypeEnum.valueOf(name)) ? "" :
               HiddenTaskStatisticTypeEnum.valueOf(name).getDesc();
    }
}
