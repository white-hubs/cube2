package com.guhecloud.subsystem.requester.sdk.param.information;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SendProgramToTerminalParam {

    /**
     * 节目名称
     */
    private String programName;
    /**
     * 开始日期 yyyy-MM-dd
     */
    private LocalDateTime beginDate;
    /**
     * 结束日期 yyyy-MM-dd
     */
    private LocalDateTime endDate;
    /**
     * 开始时间 HH:mm:ss
     */
    private String beginTime;
    /**
     * 结束时间 HH:mm:ss
     */
    private String endTime;

    /**
     * 第三方节目id
     */
    private String thirdProgramId;

    /**
     * 0 表示普通发送,1 表示插播
     */
    private Integer priority;

    /**
     * 终端设备列表
     */
    private List<DeviceDTO> terminals;

    /**
     * 执行周期集合 1：周一 2：周二 3：周三 4：周四 5：周五 6：周六 7：周日
     */
    private List<String> executionCycle;
}
