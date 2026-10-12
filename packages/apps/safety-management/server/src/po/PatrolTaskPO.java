package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 电子巡更
 * @date 2023/9/7 15:23
 */
@Data
@TableName("safety_patrol_task")
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatrolTaskPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * 任务id
     */
    private Long id;
    /**
     * 巡更任务
     */
    private String taskName;
    /**
     * 任务创建时间
     */
    private LocalDateTime taskCreateTime;
    /**
     * 执行时间段
     */
    private String executeData;
    /**
     * 任务状态
     */
    private String statusCode;
    private String statusName;
    /**
     * 任务结果
     */
    private String resultCode;
    private String resultName;
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
    /**
     * 所属市场
     */
    private Integer marketTenant;
    private String marketName;
}
