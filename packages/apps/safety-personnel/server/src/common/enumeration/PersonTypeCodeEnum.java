package com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration;

import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Arrays;

/**
 * 安全人员类型编码；与前端 Tab 对齐。
 */
public enum PersonTypeCodeEnum {

    /**
     * 仅查询条件，不入库。
     */
    ALL("全部"),

    SAFETY("安全管理人员"),

    SPECIAL("特种作业人员"),

    EMERGENCY("应急人员");

    private final String description;

    PersonTypeCodeEnum(String description) {
        this.description = description;
    }

    public String getCode() {
        return name();
    }

    public String getDescription() {
        return description;
    }

    /**
     * 校验关联/筛选类型编码。
     *
     * @param value 类型编码
     *
     * @return 规范化大写编码
     */
    public static String normalizeForQuery(String value) {
        Assert.isParameterError(StringUtils.isBlank(value), "人员类型不能为空");
        String normalized = value.trim().toUpperCase();
        boolean supported = Arrays.stream(values()).anyMatch(item -> item.name().equals(normalized));
        Assert.isParameterError(!supported, "人员类型不合法");
        return normalized;
    }

    /**
     * 校验写入库的类型编码（排除 ALL）。
     *
     * @param value 类型编码
     *
     * @return 规范化大写编码
     */
    public static String normalizeForPersist(String value) {
        String normalized = normalizeForQuery(value);
        Assert.isParameterError(ALL.name().equals(normalized), "不能将 ALL 写入人员类型关系");
        return normalized;
    }

    public static boolean isAll(String value) {
        return value != null && ALL.name().equalsIgnoreCase(value.trim());
    }
}
