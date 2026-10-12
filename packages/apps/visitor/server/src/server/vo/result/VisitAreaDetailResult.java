package com.guhecloud.cube.srv.gen.visitor.management.server.vo.result;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 区域访问时段详情返回对象
 */
@Data
public class VisitAreaDetailResult {
    private static final long serialVersionUID = 1980115841711436027L;
    private Long parentId;
    private Long areaId;
    private String areaName;
    private LocalDateTime createTime;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String startDateStr;
    private String endDateStr;
    private List<VisitAreaDetailTimeResult> timeList;
}
