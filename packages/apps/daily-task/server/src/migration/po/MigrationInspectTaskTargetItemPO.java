package com.guhecloud.cube.biz.daily.task.migration.po;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 巡检任务对象的巡检标准(项)信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
//@TableName("inspect_task_target_item")
public class MigrationInspectTaskTargetItemPO extends BasicTenantEntity<Long> {
    /**
     *
     */
    private Long id;

    /**
     * 巡检任务ID
     */
    private Long taskId;

    /**
     * 巡检对象ID
     */
    private Long targetId;

    /**
     * 巡检标准ID
     */
    private Long standardId;

    /**
     * 巡检对象名称
     */
    private String standardName;

    /**
     * 标准编码
     */
    private String standardCode;

    /**
     * 巡检项ID
     */
    private Long itemId;

    /**
     * 巡检项名称
     */
    private String itemName;

    /**
     * 巡检项别名
     */
    private String itemAlias;

    /**
     * 巡检结果默认值
     */
    private String resultItemValue;

    /**
     * 要填写描述的结果(多个用逗号隔开)
     */
    private String remarkResult;

    /**
     * 要拍照的结果(多个用逗号隔开)
     */
    private String imageResult;
    /**
     * 巡检项tag
     */
    private String itemTag;

    /**
     * 巡检结果Id
     */
    private Long resultId;

    /**
     * 巡检结果
     */
    private String resultText;
}