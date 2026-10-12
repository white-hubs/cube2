package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.ScheduleModeEnum;
import lombok.Data;

import java.util.List;

/**
 * 定时任务分页行
 */
@Data
public class ScheduledTaskPageResult {
    private Long id;
    private Long groupId;
    private String name;
    private ScheduleModeEnum scheduleMode;
    /**
     * 绑定的设备名称列表
     */
    private List<String> deviceNames;
}
