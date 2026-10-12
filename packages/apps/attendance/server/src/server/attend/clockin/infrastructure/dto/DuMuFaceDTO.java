package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-07-03  15:17
 * @Description:
 * @Version: 1.0
 */
@Data
public class DuMuFaceDTO {
    /**
     * code码
     */
    private Integer code;
    /**
     * 结果说明
     */
    private String log;
    /**
     * 用户信息
     */
    private List<DuMuFaceUserDTO> user_list;

}
