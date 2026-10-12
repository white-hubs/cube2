package com.guhecloud.cube.iot.mgt.device.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 分页查询设备命令历史参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommandHistoryPageParam extends Paging {

    /**
     * 所属设备ID (可选，非空优先精确查)
     */
    private Long deviceId;

    /**
     * 设备SN或名称关键字检索
     */
    private String deviceNameOrNumber;

    /**
     * 命令分类类型 (例如: DEV_PROP_SET，METHOD_INVOKE 等)
     */
    private String downCmd;

    /**
     * 目标标识 (属性 Code / 方法 Code / 事件 Code 等)
     */
    private String targetIdentifier;

    /**
     * 单记录执行状态 (可选)
     */
    private String singleCmdStatus;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
