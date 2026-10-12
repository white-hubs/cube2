package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * @author lzy
 * @date 2022/5/5
 */
@Getter
public enum HiddenDangerMgmtTypeEnum {

    MANAGEMENT("管理人员"),

    DEPARTMENT("指定部门"),

    POST("指定岗位职责"),

    PERSONNEL("指定人员");

    public final String desc;

    HiddenDangerMgmtTypeEnum(@NonNull String desc) {
        this.desc = desc;
    }

}
