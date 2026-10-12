package com.guhecloud.cube.srv.gen.visitor.management.server.enums;

import lombok.Getter;

/**
 * 证件类型枚举
 */
@Getter
public enum CertificateTypeEnum {
    RESIDENCE_BOOKLET(3, "户口簿"),
    SOLDIERS(6, "士兵证"),
    DWELL(12, "居住证"),
    ID_CARD(111, "身份证"),
    OFFICER(114, "军官证"),
    POLICE(115, "警官证"),
    TEMPORARY_RESIDENCE(116, "暂住证"),
    WORK(131, "工作证"),
    STUDENT(133, "学生证"),
    DRIVING_LICENSE(335, "机动车驾驶证"),
    PASSPORT(414, "护照"),
    TAIWAN_PASS(511, "台湾通行证"),
    GAT_PASS(513, "港澳通行证"),
    OVERSEAS_CHINESE(551, "华侨证"),
    FOREIGNERS_RESIDENCE(554, "外国人居留证"),
    OTHER(-1, "其他"),
    ;

    final Integer type;
    final String typeName;

    CertificateTypeEnum(int type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }
}
