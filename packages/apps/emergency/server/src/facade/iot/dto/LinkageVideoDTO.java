package com.guhecloud.cube.biz.emergency.server.facade.iot.dto;

import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteModeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.VideoActionTypeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.VideoStrategyTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 联动摄像机配置
 *
 * @author lanchun
 * @date 2022/6/21 17:06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkageVideoDTO {

    /**
     * 类型
     */
    private VideoStrategyTypeEnum linkageVideoStrategyType;

    /**
     * 设备主键
     */
    private Long id;

    /**
     * 设备编号
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 摄像机token
     */
    private String token;

    /**
     * 动作类型
     */
    private VideoActionTypeEnum type;

    /**
     * 动作时长
     */
    private Integer time;

    /**
     * 动作时长单位
     */
    private String timeUnit;

    /**
     * 间隔时间
     */
    private Integer intervalTime;

    /**
     * 持续次数
     */
    private Integer durationTime;

    /**
     * 执行方式
     */
    @NotNull(message = "执行方式不能为空")
    private ExecuteModeEnum executeMode;
}
