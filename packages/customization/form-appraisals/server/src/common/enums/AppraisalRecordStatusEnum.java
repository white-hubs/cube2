package com.guhecloud.cube.srv.gen.appraisals.server.common.enums;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;

/**
 * 考核记录状态枚举
 * <p>
 * 用于标识考核记录在流程中的不同状态阶段，从生成到确认的完整生命周期。
 * </p>
 *
 * @author system
 */
@Getter
public enum AppraisalRecordStatusEnum {
    /**
     * 已生成 - 考核记录已创建，但尚未进行调整或确认
     */
    GENERATED("已生成"),

    /**
     * 已调整 - 考核记录已经过调整修改，等待最终确认
     */
    ADJUSTED("已调整"),

    /**
     * 已确认 - 考核记录已完成确认，流程结束
     */
    CONFIRMED("已确认"),
    ;

    /**
     * 状态描述信息
     */
    private final String description;

    /**
     * 构造函数
     *
     * @param description 状态描述信息
     */
    AppraisalRecordStatusEnum(String description) {
        this.description = description;
    }

    /**
     * 根据状态名称获取对应的描述信息
     * <p>
     * 遍历所有枚举值，查找与给定名称匹配的枚举项，并返回其描述信息。
     * 如果未找到匹配项或输入为空，则返回空字符串。
     * </p>
     *
     * @param name 状态名称（枚举常量名）
     *
     * @return 对应的状态描述信息，未找到时返回空字符串
     */
    public static String getDescription(String name) {
        // 参数校验：如果输入为空，直接返回空字符串
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        // 遍历所有枚举值，查找匹配的名称
        for (AppraisalRecordStatusEnum status : AppraisalRecordStatusEnum.values()) {
            if (status.name().equals(name)) {
                return status.getDescription();
            }
        }
        // 未找到匹配项，返回空字符串
        return "";
    }

    /**
     * 根据状态名称获取对应的枚举对象
     * <p>
     * 遍历所有枚举值，查找与给定名称匹配的枚举项并返回。
     * 如果未找到匹配项或输入为空，则返回 null。
     * </p>
     *
     * @param name 状态名称（枚举常量名）
     *
     * @return 对应的枚举对象，未找到时返回 null
     */
    public static AppraisalRecordStatusEnum getByCode(String name) {
        // 参数校验：如果输入为空，直接返回 null
        if (ObjectUtils.isEmpty(name)) {
            return null;
        }
        // 遍历所有枚举值，查找匹配的名称
        for (AppraisalRecordStatusEnum status : AppraisalRecordStatusEnum.values()) {
            if (status.name().equals(name)) {
                return status;
            }
        }
        // 未找到匹配项，返回 null
        return null;
    }

    /**
     * 判断给定的状态名称是否为有效的枚举值
     * <p>
     * 通过尝试获取对应的枚举对象来判断名称是否有效。
     * </p>
     *
     * @param name 待验证的状态名称
     *
     * @return 如果是有效的枚举值返回 true，否则返回 false
     */
    public static boolean isValid(String name) {
        return getByCode(name) != null;
    }
}
