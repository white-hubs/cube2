package com.guhecloud.cube.srv.gen.visitor.management.server.vo.param;

import lombok.Data;

import java.util.List;

@Data
public class VisitAreaParam {
    private Long id;
    private Long areaId;
    private String areaName;
    private Integer deviceNumber;
    /**
     * 列表展示的访问时间段"09:00-12:00,15:00-18:00"
     */
    private String visitPeriods;
    /**
     * 新增或编辑的集合
     */
    private List<VisitAreaDetailParam> detailList;
}
