package com.guhecloud.cube.srv.gen.visitor.management.server.vo.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 区域访问时段详情入参对象
 */
@Data
public class VisitAreaDetailParam {
    private static final long serialVersionUID = 9105094579871663096L;
    private Long parentId;
    private Long areaId;
    private String areaName;
    private LocalDateTime createTime;
    private Period period;
    private String startDateStr;
    private String endDateStr;
    private List<VisitAreaDetailTimeParam> timeList;
}
