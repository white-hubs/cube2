package com.guhecloud.cube.srv.gen.property.management.server.risk.source.enumeration;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 风险源使用人类型
 */
@Getter
public enum RiskSourceReceiveRserTypeEnum {
    MERCHANT("商户"),
    MARKET("市场人员");
    private final String describe;

    RiskSourceReceiveRserTypeEnum(String describe) {
        this.describe = describe;
    }

    /**
     * 根据描述获取name
     *
     * @param describe 描述
     *
     * @return name
     */
    public static String getNameByDescribe(String describe) {
        if (StringUtils.isBlank(describe)) {
            return "";
        }
        for (RiskSourceReceiveRserTypeEnum value : RiskSourceReceiveRserTypeEnum.values()) {
            if (value.getDescribe().equals(describe)) {
                return value.name();
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
        for (RiskSourceReceiveRserTypeEnum value : RiskSourceReceiveRserTypeEnum.values()) {
            describeList.add(value.getDescribe());
        }
        return describeList;
    }
}
