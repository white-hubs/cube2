package com.guhecloud.cube.srv.gen.safety.management.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author wql
 * @describe 小散工程状态
 * @date 2023/9/19 15:29
 */
@Getter
public enum LooseWorksStatusEnum {
    UNDERWAY("进行中"),
    COMPLETED("已完成"),
    WAIT_ACCEPT("待验收"),
    ALL("所有");

    private final String desc;

    LooseWorksStatusEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(LooseWorksStatusEnum.valueOf(name)) ? "" :
               LooseWorksStatusEnum.valueOf(name).getDesc();
    }
}
