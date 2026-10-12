package com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration;

import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Arrays;

/**
 * 安全履历记录类型。
 */
public enum ResumeRecordTypeEnum {

    PATROL("patrol", "巡查"),

    TRAINING("training", "培训"),

    APPROVAL("approval", "审批"),

    ALARM("alarm", "告警"),

    MEETING("meeting", "会议"),

    OTHER("other", "其它任务");

    private final String code;
    private final String description;

    ResumeRecordTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static String normalize(String value) {
        Assert.isParameterError(StringUtils.isBlank(value), "履历类型不能为空");
        String normalized = value.trim().toLowerCase();
        boolean supported = Arrays.stream(values()).anyMatch(item -> item.code.equals(normalized));
        Assert.isParameterError(!supported, "履历类型不合法");
        return normalized;
    }

    public static String getDescription(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        String normalized = code.trim().toLowerCase();
        return Arrays.stream(values())
                .filter(item -> item.code.equals(normalized))
                .findFirst()
                .map(ResumeRecordTypeEnum::getDescription)
                .orElse(code);
    }
}
