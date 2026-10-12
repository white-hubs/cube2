package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/14 15:06
 * @Description: 巡检业务类型和应用映射
 */

@Getter
public enum InspectionMappingEnum {

    SMALL_ENGINEERING("工程巡检", "SMALLENFINEERING"),
    INSPECTION("日常巡检", "INSPECTION"),
    ;
    private final String description;
    private final String applicationCode;

    InspectionMappingEnum(String description, String applicationCode) {
        this.description = description;
        this.applicationCode = applicationCode;
    }

    /**
     * 是否包含当前枚举
     *
     * @param businessType
     *
     * @return
     */
    public static boolean contains(String businessType) {
        return Arrays.stream(values()).anyMatch(t -> t.name().equals(businessType));
    }

    /**
     * 根据业务类型获取映射关系
     *
     * @param businessType
     *
     * @return
     */
    public static InspectionMappingEnum getInspectionType(String businessType) {
        if (contains(businessType)) {
            return valueOf(businessType);
        } else {
            return InspectionMappingEnum.INSPECTION;
        }
    }

    /**
     * 获取消息场景编码
     *
     * @param msgSceneCode
     *
     * @return
     */
    public String getMsgSceneCode(String msgSceneCode) {
        if (this == InspectionMappingEnum.INSPECTION) {
            return InspectNoticeModuleTypeEnum.valueOf(msgSceneCode).name();
        } else {
            return this.name() + "_" + InspectNoticeModuleTypeEnum.valueOf(msgSceneCode).name();
        }
    }
}
