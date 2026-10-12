package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 电子巡更详情列表
 * @date 2023/9/7 20:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PatrolTaskVO {
    private Integer marketTenant;
    /**
     * 所属市场
     */
    private String marketName;
    /**
     * 巡更任务
     */
    private String taskName;
    /**
     * 执行时间段
     */
    private String executeData;
    /**
     * 任务状态
     */
    private String status;
    /**
     * 任务结果
     */
    private String result;
    /**
     * 巡更路线
     */
    private String routeName;
    /**
     * 巡更点数
     */
    private Integer locationNum;
    /**
     * 执行人姓名
     */
    private String userName;
}
