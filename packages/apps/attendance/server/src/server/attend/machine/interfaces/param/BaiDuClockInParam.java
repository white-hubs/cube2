package com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.interfaces.param;

import lombok.Data;

import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-22  20:21
 * @Description:
 * @Version: 1.0
 */
@Data
public class BaiDuClockInParam {
    /**
     * 设备id
     */
    private String device_id;
    /**
     * 识别记录的时间(时间戳)
     */
    private String timestamp;
    /**
     * 触发本次识别记录的人员 ID
     */
    private String user_id;
    private String msg_id;
    /**
     * 用户信息
     */
    private Map user_info;
}
