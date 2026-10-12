package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.Getter;

@Getter
public enum KSDJEventTypeEnum {
    DEVICE_TAMPER_ALARM("983302", "设备防拆报警"),
    HOSTAGE_ALARM("983303", "劫持报警"),
    PASSWORD_ERROR_ALARM("983304", "密码错误报警"),
    DOOR_NOT_OPEN_ALARM("983305", "门没开报警"),
    DOOR_NOT_CLOSE_ALARM("983306", "门没关报警"),
    SOS_ALARM("983553", "SOS报警"),
    WATER_SENSOR_ALARM("983554", "水感报警"),
    ACTIVE_INFRARED_ALARM("983555", "主动红外报警"),
    SMOKE_ALARM("983556", "烟感报警"),
    GAS_ALARM("983557", "煤气报警"),
    PASSIVE_INFRARED_ALARM("983558", "被动红外报警"),
    DOOR_MAGNETIC_ALARM("983559", "门磁报警"),
    EMERGENCY_SWITCH_ALARM("983560", "紧急开关报警"),
    SMART_LOCK_HOSTAGE_FINGERPRINT("983561", "智能锁劫持指纹"),
    SMART_LOCK_HOSTAGE_PASSWORD("983562", "智能锁劫持密码"),
    SMART_LOCK_PRY_DOOR_ALARM("983563", "智能锁撬门报警"),
    SMART_LOCK_LOCKED("983564", "智能锁锁定"),
    SMART_LOCK_LOW_BATTERY("983565", "智能锁电量不足"),
    OWNER_PASSWORD_UNLOCK("983297", "业主密码开锁"),
    HOSTAGE_PASSWORD_UNLOCK("983298", "劫持密码开锁"),
    CARD_UNLOCK("983299", "刷卡开锁"),
    INDOOR_MACHINE_UNLOCK("983300", "室内机开锁"),
    CENTER_PLATFORM_UNLOCK("983301", "中心平台开锁"),
    BLUETOOTH_UNLOCK("983307", "蓝牙开锁"),
    QR_CODE_UNLOCK("983308", "二维码开锁"),
    FACE_UNLOCK("983309", "人脸开锁"),
    FINGERPRINT_UNLOCK("983310", "指纹开锁");

    private final String value;

    private final String displayName;

    KSDJEventTypeEnum(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public static String getDisplayNameByValue(String value) {
        for (KSDJEventTypeEnum status : KSDJEventTypeEnum.values()) {
            if (StringUtils.isNotEmpty(value) && status.getValue().equals(value)) {
                return status.getDisplayName();
            }
        }
        return null; // 如果没有匹配的枚举项，则返回 null 或其他适当的默认值
    }
}
