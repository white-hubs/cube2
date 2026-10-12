package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author Cursor
 * @describe 人员维度隐患统计
 */
@Data
public class StaffHiddenDangerStatisticsVO {
    /**
     * 人员id
     */
    private Long staffId;

    /**
     * 异常隐患数
     */
    private Integer abnormalHiddenDangerNum;

    /**
     * 已处理隐患数
     */
    private Integer haveDoneHiddenDangerNum;
}
