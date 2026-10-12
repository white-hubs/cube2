package com.guhecloud.cube.iot.mgt.device.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备下行命令历史概览出参
 */
@Data
public class CommandHistoryVo {

    /**
     * 子命令实例ID
     */
    private String instanceId;

    /**
     * 群组命令序号
     */
    private String cmdSeq;

    /**
     * 指令类别类型
     */
    private String downCmd;

    /**
     * 人类可读的翻译指令描述
     */
    private String translatedCmdContent;

    /**
     * 执行综合结果状态
     */
    private String singleCmdStatus;
    
    /**
     * 当前记录设备ID
     */
    private Long deviceId;

    /**
     * 设备SN (对应 deviceNumber)
     */
    private String deviceNumber;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 执行产生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 执行可能终结时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

}
