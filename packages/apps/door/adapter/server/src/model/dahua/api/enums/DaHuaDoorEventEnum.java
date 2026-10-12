package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.enums;

import lombok.Getter;

/**
 * 大华开门类型枚举类
 */
@Getter
public enum DaHuaDoorEventEnum {
    PASSWORD(42, "合法密码开门"),
    ILLEGAL_PASSWORD(43, "非法密码开门"),
    FINGER_PRINT(44, "合法指纹开门"),
    // 以下是您要求添加的内容
    ILLEGAL_FINGER_PRINT(45, "非法指纹开门"),
    REMOTE_OPEN(48, "远程开门"),
    BUTTON_OPEN(49, "按钮开门"),
    KEY_OPEN(50, "钥匙开门"),
    LEGAL_CARD_SWIPE(51, "合法刷卡开门"),
    ILLEGAL_CARD_SWIPE(52, "非法刷卡开门"),
    MAGNETIC_DOOR_EVENT(53, "门磁事件"),
    ABNORMAL_OPEN(54, "异常开门"),
    ABNORMAL_CLOSE(55, "异常关门"),
    NORMAL_CLOSE(56, "正常关门"),
    NORMAL_OPEN(57, "正常开门"),
    INTERCOM_REQUEST(59, "对讲请求事件"),
    FACE_LEGAL_OPEN(61, "人脸合法开门"),
    FACE_ILLEGAL_OPEN(62, "人脸非法开门"),
    RFID_ACTIVE_LEGAL(1421, "RFID有源合法"),
    RFID_PASSIVE_LEGAL(1422, "RFID无源合法"),
    RFID_ACTIVE_ILLEGAL(1423, "RFID有源非法"),
    RFID_PASSIVE_ILLEGAL(1424, "RFID无源非法"),
    BLACKLIST_EVENT(1433, "黑名单事件"),
    IDENTITY_LEGAL_OPEN(1436, "人证合法开门"),
    IDENTITY_ILLEGAL_OPEN(1437, "人证非法开门"),
    IDENTITY_ID_ILLEGAL_OPEN(1438, "人证和身份证非法开门"),
    IDENTITY_ID_LEGAL_OPEN(1439, "人证和身份证合法开门"),
    RFID_SENSOR_EVENT(1448, "RFID感应事件"),
    RFID_ILLEGAL_SENSOR_EVENT(1449, "RFID非法感应事件"),
    RFID_BUTTON_EVENT(1450, "RFID按键事件"),
    RFID_ILLEGAL_BUTTON_EVENT(1451, "RFID非法按键事件"),
    CARD_PASSWORD_LEGAL_OPEN(1455, "先刷卡后密码合法开门"),
    CARD_PASSWORD_ILLEGAL_OPEN(1456, "先刷卡后密码非法开门"),
    CARD_FINGERPRINT_LEGAL_OPEN(1461, "刷卡+指纹组合合法开门"),
    CARD_FINGERPRINT_ILLEGAL_OPEN(1462, "刷卡+指纹组合非法开门"),
    MULTIPLE_PEOPLE_LEGAL_OPEN(1463, "多人合法开门"),
    MULTIPLE_PEOPLE_ILLEGAL_OPEN(1464, "多人非法开门"),
    PERSON_NUMBER_PASSWORD_LEGAL_OPEN(1467, "人员编号+密码合法开门"),
    PERSON_NUMBER_PASSWORD_ILLEGAL_OPEN(1468, "人员编号+密码非法开门"),
    FACE_PASSWORD_LEGAL_OPEN(1469, "人脸+密码合法开门"),
    FACE_PASSWORD_ILLEGAL_OPEN(1470, "人脸+密码非法开门"),
    FINGERPRINT_PASSWORD_LEGAL_OPEN(1471, "指纹+密码合法开门"),
    FINGERPRINT_PASSWORD_ILLEGAL_OPEN(1472, "指纹+密码非法开门"),
    FINGERPRINT_FACE_LEGAL_OPEN(1473, "指纹+人脸合法开门"),
    FINGERPRINT_FACE_ILLEGAL_OPEN(1474, "指纹+人脸非法开门"),
    CARD_FACE_LEGAL_OPEN(1475, "刷卡+人脸合法开门"),
    CARD_FACE_ILLEGAL_OPEN(1476, "刷卡+人脸非法开门"),
    FINGERPRINT_FACE_PASSWORD_LEGAL_OPEN(1487, "指纹+人脸+密码合法开门"),
    FINGERPRINT_FACE_PASSWORD_ILLEGAL_OPEN(1488, "指纹+人脸+密码非法开门"),
    CARD_FACE_PASSWORD_LEGAL_OPEN(1489, "刷卡+人脸+密码合法开门"),
    CARD_FACE_PASSWORD_ILLEGAL_OPEN(1490, "刷卡+人脸+密码非法开门"),
    CARD_FINGERPRINT_PASSWORD_LEGAL_OPEN(1491, "刷卡+指纹+密码合法开门"),
    CARD_FINGERPRINT_PASSWORD_ILLEGAL_OPEN(1492, "刷卡+指纹+密码非法开门"),
    CARD_FINGERPRINT_FACE_LEGAL_OPEN(1493, "卡+指纹+人脸组合合法开门"),
    CARD_FINGERPRINT_FACE_ILLEGAL_OPEN(1494, "卡+指纹+人脸组合非法开门"),
    CARD_FINGERPRINT_FACE_PASSWORD_LEGAL_OPEN(4603, "卡+指纹+人脸+密码组合合法开门"),
    CARD_FINGERPRINT_FACE_PASSWORD_ILLEGAL_OPEN(4604, "卡+指纹+人脸+密码组合非法开门"),
    FACE_SAFETY_HAT_LEGAL_OPEN(4626, "人脸+安全帽合法开门"),
    FACE_SAFETY_HAT_ILLEGAL_OPEN(4627, "人脸+安全帽非法开门"),
    HEALTH_CODE_LEGAL_OPEN(10001, "健康码合法开门"),
    ABNORMAL_HEALTH_CODE_OPEN(10002, "异常健康码开门");;

    private final Integer typeCode;
    private final String name;

    DaHuaDoorEventEnum(Integer typeCode, String name) {
        this.typeCode = typeCode;
        this.name = name;
    }

    public static DaHuaDoorEventEnum getByCode(Integer typeCode) {
        for (DaHuaDoorEventEnum daHuaDoorEventEnum : DaHuaDoorEventEnum.values()) {
            if (daHuaDoorEventEnum.getTypeCode().equals(typeCode)) {
                return daHuaDoorEventEnum;
            }
        }
        return null;
    }
}
