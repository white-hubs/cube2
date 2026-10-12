package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import java.util.List;

/**
 * 批量更新租户人员工作状态
 */
@Data
public class RenewWorkStatusBatchParam {
    private Integer tenant;
    // 批量更新相应状态
    private List<Long> leaveStaffList;
    private List<Long> awayStaffList;
    private List<Long> postStaffList;
    private List<Long> workStaffList;
    private List<Long> restStaffList;
    // 指定更新
    private List<Long> userIdList;
    private String workStatus;
    /**
     * 更新前是全部重置为无状态
     */
    private Boolean resetAllStatus;
}
