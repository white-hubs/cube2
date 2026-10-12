package com.guhecloud.cube.srv.gen.loose.works.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.loose.works.enumeration.ProjectInspectResultEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 工程临时巡查记录
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/11 17:05
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("project_inspect_record")
public class ProjectInspectRecordPO extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 记录名称
     */
    private String name;
    /**
     * 工程id
     */
    private Long projectScheduleId;

    /**
     * 巡查结果
     */
    private ProjectInspectResultEnum result;

    /**
     * 巡查人
     */
    private Long userId;

    /**
     * 备注
     */
    private String remarks;
}

