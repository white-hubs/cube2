package com.guhecloud.cube.srv.gen.maintain.server.common.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author lzy
 * @date 2022/5/5
 * 故障,隐患,维保,委外等来源枚举
 */
@Getter
public enum ResourceEnum {

    MANUAL_SUBMISSION("手动提交"),

    SYS_GENERATION("系统生成"),

    INSPECTION("日常巡检"),

    ELECTRONIC_PATROL("电子巡更"),

    PROPERTY_MANAGEMENT("资产管理"),

    SECURITY_MONITORING("安防监控"),

    EXTERNAL_CHAIN_APPROVAL("外链审批");

    public final String desc;

    ResourceEnum(@NonNull String desc) {
        this.desc = desc;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(ResourceEnum.valueOf(name)) ? "" :
               ResourceEnum.valueOf(name).getDesc();
    }
}
