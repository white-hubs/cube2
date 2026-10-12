package com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.interfaces.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-22  20:15
 * @Description:
 * @Version: 1.0
 */
@Data
@Builder
public class BaiDuCallbackDTO {
    private String log;
    private Integer code;
}
