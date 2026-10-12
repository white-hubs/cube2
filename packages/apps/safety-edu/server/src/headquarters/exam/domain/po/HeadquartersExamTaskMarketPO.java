package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 总部考试任务关联市场
 *
 * @author lanchun
 * @date 2024/4/9 16:52
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName(value = "headquarters_exam_task_market")
public class HeadquartersExamTaskMarketPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 总部考试任务主键
     */
    private Long headquartersExamTaskId;

    /**
     * 市场编码
     */
    private String marketCode;

    /**
     * 市场租户编号
     */
    private Integer marketTenantCode;

    /**
     * 市场名称
     */
    private String marketName;
}
