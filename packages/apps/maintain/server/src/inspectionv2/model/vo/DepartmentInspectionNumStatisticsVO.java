package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author Cursor
 * @describe 部门巡检次数统计
 */
@Data
public class DepartmentInspectionNumStatisticsVO {
    /**
     * 部门id
     */
    private Long depId;

    /**
     * 巡检次数
     */
    private Long inspectionNum;
}
