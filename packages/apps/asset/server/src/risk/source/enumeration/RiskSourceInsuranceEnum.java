package com.guhecloud.cube.srv.gen.property.management.server.risk.source.enumeration;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 风险源是否购买保险
 */
@Getter
public enum RiskSourceInsuranceEnum {
    SHI("是", "1"),
    FO("否", "0");
    private final String describe;
    private final String content;

    RiskSourceInsuranceEnum(String describe, String content) {
        this.describe = describe;
        this.content = content;
    }

    /**
     * 根据描述获取内容值
     *
     * @param describe 描述
     *
     * @return content
     */
    public static String getContentByDescribe(String describe) {
        if (StringUtils.isBlank(describe)) {
            return "";
        }
        for (RiskSourceInsuranceEnum value : RiskSourceInsuranceEnum.values()) {
            if (value.getDescribe().equals(describe)) {
                return value.getContent();
            }
        }
        return "";
    }

    /**
     * 获取描述集合
     *
     * @return 描述集合
     */
    public static List<String> getDescribeList() {
        List<String> describeList = new ArrayList<>();
        for (RiskSourceInsuranceEnum value : RiskSourceInsuranceEnum.values()) {
            describeList.add(value.getDescribe());
        }
        return describeList;
    }
}
