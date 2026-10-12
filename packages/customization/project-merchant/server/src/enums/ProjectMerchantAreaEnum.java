package com.guhecloud.cube.deliver.project.merchant.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-19  20:22
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum ProjectMerchantAreaEnum {
    areaTypeBuildings(1687336862770102273L, "楼栋区域"),
    areaTypeBuildings_building(null, "楼栋"),
    areaTypeBuildings_floor(null, "楼层"),
    areaTypeBuildings_slice(null, "片区"),
    areaTypeOutdoor(1687362335390662658L, "室外区域"),
    areaTypeOutdoor_first(null, "一级"),
    areaTypeOutdoor_second(null, "二级"),
    areaTypeOutdoor_third(null, "三级"),
    questionnaireType_shjy(null,"商户经营"),
    questionnaireType_scgl(null,"市场管理"),
    vehicleType_tricycle(null,"三轮车"),
    vehicleType_motorcycle(null,"摩托车"),
    spaceTradeModel_operation(null,"运营"),
    spaceTradeModel_personal(null,"自用"),
    spaceTradeModel_public(null,"公用"),
    protocolType_foodSafety(null,"食品安全"),
    protocolType_mqsb(null,"门前三包"),

    ;
    private String code;
    private Long id;

    ProjectMerchantAreaEnum(Long id, String code) {
        this.id=id;
        this.code = code;
    }

    public static String getCodeByName(String name) {
        String code = "";
        if(ObjectUtil.isEmpty(name)){
            return code;
        }
        ProjectMerchantAreaEnum[] areaEnum = ProjectMerchantAreaEnum.values();
        for (ProjectMerchantAreaEnum ex : areaEnum) {
            if (ex.name().equals(name)) {
                code = ex.code;
                break;
            }
        }
        return code;
    }

    public static String getCodeById(Long id) {
        String code = "";
        ProjectMerchantAreaEnum[] areaEnum = ProjectMerchantAreaEnum.values();
        for (ProjectMerchantAreaEnum ex : areaEnum) {
            if (ObjectUtil.isNotEmpty(ex.getId()) && ex.id.equals(id)) {
                code = ex.code;
            }
        }
        return code;
    }
}
