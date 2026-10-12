package com.guhecloud.cube.srv.gen.maintain.server.fault.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * 故障单状态
 *
 * @author lzy
 * @date 2022/5/5
 */
@Getter
public enum FaultStatusEnum {

    WAITING_FOR_CONFIRM("待确认"),

    CONFIRMED("已确认"),

    FINISHED("已结束"),

    ;

    private final String desc;

    FaultStatusEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(FaultStatusEnum.valueOf(name)) ? "" :
               FaultStatusEnum.valueOf(name).getDesc();
    }
}
