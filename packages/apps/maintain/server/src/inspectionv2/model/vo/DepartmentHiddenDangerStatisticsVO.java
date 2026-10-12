package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author Cursor
 * @describe 部门维度隐患统计
 */
@Data
public class DepartmentHiddenDangerStatisticsVO {
    /**
     * 部门id
     */
    private Long depId;

    /**
     * 异常隐患数
     */
    private Integer abnormalHiddenDangerNum;

    /**
     * 已处理隐患数
     */
    private Integer haveDoneHiddenDangerNum;
}
