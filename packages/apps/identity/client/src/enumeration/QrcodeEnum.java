package com.guhecloud.cube.srv.gen.identity.client.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 二维码
 *
 * @author bq
 * @date 2022/07/28
 */
@Getter
public enum QrcodeEnum {

    /**
     * 实体类型
     */
    DEVICE("设备"),
    STALL("档位"),
    PROPERTY("资产"),
    SITE_POINT("现场点"),

    /**
     * 实体属性
     */
    NAME("名称"),
    TYPE("类型"),
    AREA("所属区域"),
    USE_DEPT("使用部门"),
    INSTALL_ADD("安装地址"),
    FINANCE_CODE("财务编码"),
    ;

    /**
     * 描述信息
     */
    private final String description;

    QrcodeEnum(@NonNull String description) {
        this.description = description;
    }

}
