package com.guhecloud.cube.biz.daily.task.domain.listener.adapter;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

@Getter
public enum FlowResult {

    /**
     * 通过
     */
    PASS("通过"),
    /**
     * 完成
     */
    FINISHED("完成"),
    /**
     * 处理中
     */
    ACTIVE("处理中"),
    /**
     * 拒绝
     */
    REJECT("拒绝"),
    /**
     * 撤销
     */
    UNDO("撤销");

    private final String desc;

    FlowResult(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(FlowResult.valueOf(name)) ? "" :
               FlowResult.valueOf(name).getDesc();
    }
}
