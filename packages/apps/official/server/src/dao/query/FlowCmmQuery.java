package com.guhecloud.cube.srv.gen.official.management.server.dao.query;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 流程通用查询条件
 */
@Data
@Builder
public class FlowCmmQuery {
    private String businessType;
    //开始时间
    private LocalDateTime startTime;
    //结束时间
    private LocalDateTime endTime;
    //部门ID
    private Long deptId;
    //申请人姓名
    private String applicantName;
    //通用名称
    private String name;
    //id
    private Long id;
    //状态
    private String status;
}
