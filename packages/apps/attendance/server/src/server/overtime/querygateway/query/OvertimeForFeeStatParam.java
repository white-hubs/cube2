package com.guhecloud.cube.srv.gen.attendance.management.server.overtime.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OvertimeForFeeStatParam extends Paging {
    //部门ID
    private Long deptId;
    //考勤组ID
    private Long groupId;
    //月份 2022-11
    private String month;
    //用户姓名
    private String userName;
    //电话号码
    private String mobile;
    //开始时间
    private LocalDateTime startTime;
    //结束时间
    private LocalDateTime endTime;
    //租户ID
    private Integer tenant;
}
