package com.guhecloud.cube.srv.gen.disinfection.server.member.entity;

import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ManualControlCommandEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户设备手动控制设置
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberDeviceManualControlSetting extends BasicEnhancedTenantEntity<Long> {
    private Long id;
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
