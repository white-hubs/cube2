package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.Getter;

@Getter
public enum TKEventTypeEnum {
    REMOTE_LOGIN("721681", "远程登录"),
    WATCHDOG_RESET("721691", "看门狗复位"),
    LOGOUT_REMOTE_LOGIN("721696", "注销远程登录"),
    REMOTE_RESTART("721697", "远程重启"),
    REMOTE_UPGRADE("721698", "远程升级"),
    REMOTE_NTP_TIME_SYNC("721704", "远程ntp校时"),
    REMOTE_RESTORE_DEFAULT_PARAMS("721708", "远程恢复默认参数"),
    VISITOR_CALL_ELEVATOR("721748", "访客呼梯"),
    RESIDENT_CALL_ELEVATOR("721749", "住户呼梯"),
    CARD_PERMISSION_EXPIRED("721921", "卡片权限已过期"),
    NO_SUCH_CARD("721922", "无此卡号"),
    PERMISSION_MISMATCH("721923", "权限不符"),
    BLACKLIST("722177", "黑名单"),
    CARD_READER_REMOVED("722183", "读卡器拆除"),
    CARD_READER_REMOVAL_ALARM_CLEARED("722184", "解除读卡器拆除报警"),
    NORMALLY_CLOSED_STATE_START("722436", "常闭状态开始"),
    NORMALLY_CLOSED_STATE_END("722438", "常闭状态结束"),
    NORMALLY_OPEN_PERIOD_START("722689", "常开时段开始"),
    NORMALLY_OPEN_PERIOD_END("722690", "常开时段结束"),
    VALID_CARD("723202", "合法卡"),
    CARD_AUTH_FAILURE_EXCEEDED("723717", "卡号认证失败超次报警"),
    CARD_READER_OFFLINE("723969", "读卡器掉线"),
    CARD_READER_ONLINE("723970", "读卡器掉线恢复"),
    ELEVATOR_CALL_RELAY_OFF("723971", "呼梯继电器断开"),
    ELEVATOR_CALL_RELAY_ON("723972", "呼梯继电器闭合"),
    AUTO_BUTTON_RELAY_OFF("723973", "自动按键继电器断开"),
    AUTO_BUTTON_RELAY_ON("723974", "自动按键继电器闭合"),
    BUTTON_ELEVATOR_RELAY_OFF("723975", "按键梯控继电器断开"),
    BUTTON_ELEVATOR_RELAY_ON("723976", "按键梯控继电器闭合"),
    SUB_CONTROLLER_TAMPER_ALARM("723977", "分控器防拆报警"),
    SUB_CONTROLLER_TAMPER_ALARM_CLEARED("723978", "分控器防拆报警恢复"),
    SUB_CONTROLLER_ONLINE("723979", "分控器在线"),
    SUB_CONTROLLER_OFFLINE("723980", "分控器离线"),
    COERCIVE_DOOR_OPEN_ALARM("723982", "胁迫开门警报"),
    NETWORK_RECOVERY("723999", "网络恢复"),
    LOCAL_RESTORE_DEFAULT_PARAMS("724001", "本地恢复默认参数"),
    OFFLINE_EVENT_90_PERCENT_FULL("724004", "离线事件满90报警"),
    EMERGENCY_BUTTON_TRIGGERED("724225", "紧急按钮触发"),
    EMERGENCY_BUTTON_RESTORED("724226", "紧急按钮恢复"),
    FIRE_BUTTON_TRIGGERED("724227", "消防按钮触发"),
    FIRE_BUTTON_RESTORED("724228", "消防按钮恢复"),
    MAINTENANCE_BUTTON_TRIGGERED("724229", "维护按钮触发"),
    MAINTENANCE_BUTTON_RESTORED("724230", "维护按钮恢复"),
    RELEASE_CODE("724231", "解除码");

    private final String value;

    private final String displayName;

    TKEventTypeEnum(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public static String getDisplayNameByValue(String value) {
        for (TKEventTypeEnum status : TKEventTypeEnum.values()) {
            if (StringUtils.isNotEmpty(value) && status.getValue().equals(value)) {
                return status.getDisplayName();
            }
        }
        return null; // 如果没有匹配的枚举项，则返回 null 或其他适当的默认值
    }
}