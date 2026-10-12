package com.guhecloud.cube.srv.gen.visitor.management.server.vo.result;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 访问时段
 */
@Data
public class VisitAreaResult {
    private Long id;
    private Long areaId;
    private String areaName;
    private Integer deviceNumber;
    private LocalDateTime createTime;
    /**
     * // 访问时间段"09:00-12:00,15:00-18:00"
     */
    private String visitPeriods;
    private String createUserName;
    /**
     * 1正常(0逻辑删除，以前的预约数据删除了还需要能查询到这个设置)
     */
    private Boolean normal;
    /**
     * 访问时段详情
     */
    private List<VisitAreaDetailResult> detailList;
}
