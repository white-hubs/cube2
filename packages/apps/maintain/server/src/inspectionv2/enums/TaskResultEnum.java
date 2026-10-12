package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author wql
 * @describe 任务巡检结果枚举
 * @date 2023/5/9 16:12
 */
@Getter
public enum TaskResultEnum {

    ABNORMAL("异常"),
    NORMAL("正常");

    private final String description;

    TaskResultEnum(@NonNull String description) {
        this.description = description;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(TaskResultEnum.valueOf(name)) ? "" :
               TaskResultEnum.valueOf(name).getDescription();
    }
}
