package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ManualControlCommandEnum;
import lombok.Data;

/**
 * 手动控制时长设置
 */
@Data
public class ManualControlDurationSettingResult {
    /**
     * 设备 id
     */
    private Long deviceId;
    /**
     * 指令枚举
     */
    private ManualControlCommandEnum command;
    /**
     * 控制时长，单位秒
     */
    private Integer duration;
}
