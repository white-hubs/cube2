package com.guhecloud.cube.iot.mgt.device.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备下行命令步骤追踪细则日记出参
 */
@Data
public class CommandLogVo {

    /**
     * 日志子项文字详述
     */
    private String logContent;

    /**
     * 日志子项发生时间点
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
