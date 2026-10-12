package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result;

import lombok.Data;

import java.util.List;

/**
 * 可视化在岗人员
 */
@Data
public class AttWorkingResult {
    /**
     * 总人数
     */
    private Integer totalCount;
    /**
     * 在岗人数
     */
    private Integer workCount;
    /**
     * 在岗人员列表
     */
    private List<AttWorkingStaffResult> staffResultList;
}
