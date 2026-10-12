package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ManualControlCommandEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 设备手动控制入参：设备 id、设备名称、指令枚举、时长（驱蚊/雾灌时必填，单位秒）
 */
@Data
public class ManualControlParam {
    /** 设备 id */
    @NotNull(message = "设备id不能为空")
    private Long deviceId;
    /** 设备名称 */
    private String deviceName;
    /** 指令枚举：驱蚊指令、雾灌指令、设备关闭、定时任务 */
    @NotNull(message = "指令不能为空")
    private ManualControlCommandEnum command;
    /** 时长，单位秒；驱蚊指令或雾灌指令时必填 */
    private Integer duration;

    /**
     * 驱蚊/雾灌指令时 duration 必填
     */
    @jakarta.validation.constraints.AssertTrue(message = "驱蚊指令或雾灌指令时时长（秒）必填")
    public boolean isDurationValid() {
        if (command == null || !command.isNeedDuration()) {
            return true;
        }
        return duration != null;
    }
}
