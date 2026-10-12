package com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;

/**
 * @author wql
 * @describe 巡更任务状态
 * @date 2023/5/29 10:13
 */
@Getter
public enum PatrolTaskStateEnum {
    IN_EXECUTION("执行中"),
    TO_BE_RECEIVED("待接收"),
    FINISHED("已结束");
    private final String remarks;

    PatrolTaskStateEnum(String remarks) {
        this.remarks = remarks;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(PatrolTaskStateEnum.valueOf(name)) ? "" :
               PatrolTaskStateEnum.valueOf(name).getRemarks();
    }
}
