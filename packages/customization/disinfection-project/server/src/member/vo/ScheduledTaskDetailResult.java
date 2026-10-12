package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ScheduleModeEnum;
import lombok.Data;

import java.util.List;

/**
 * 定时任务详情
 */
@Data
public class ScheduledTaskDetailResult {
    private Long id;
    private Long groupId;
    private String name;
    private ScheduleModeEnum scheduleMode;
    /** 绑定的设备信息列表 */
    private List<DeviceBaseDetailsDTO> deviceList;
    private List<ScheduledTaskPlanItemResult> planList;
}
