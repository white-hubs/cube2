package com.guhecloud.cube.srv.gen.attendance.management.server.overtime.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OvertimeSituationParam extends Paging {
    //用户ID
    private Long userId;
    //部门ID
    private Long deptId;
    //考勤组ID
    private Long groupId;
    //月份 2022-11
    private String month;
    //用户姓名
    private String userName;
    //开始时间
    private LocalDateTime startTime;
    //结束时间
    private LocalDateTime endTime;
    //补偿方案
    private String compensate;

    private Integer tenant;
}
