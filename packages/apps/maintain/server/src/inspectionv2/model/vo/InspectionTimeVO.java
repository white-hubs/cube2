package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author wql
 * @describe按巡检记录统计巡检耗时
 * @date 2023/4/19 13:51
 */
@Data
public class InspectionTimeVO {
    /**
     * 巡检记录id
     */
    private Long recordId;
    /**
     * 巡检人员
     */
    private String username;
    /**
     * 巡检对象id
     */
    private Long targetId;
    /**
     * 巡检对象名称
     */
    private String targetName;
    /**
     * 巡检结果
     */
    private String result;
    /**
     * 巡检耗时
     */
    private Integer elapsedTime;
}
