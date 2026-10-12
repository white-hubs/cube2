package com.guhecloud.cube.biz.daily.task.commons.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 记录对象执行内容表
 * (item为最小维度即冗余标准id,对象内容,记录id)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_record_target_item")
@EqualsAndHashCode(callSuper = true)
public class DailyRecordTargetItemPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long taskId;
    private String taskName;
    /**
     * 记录id
     */
    private Long recordId;
    /**
     * 对象类别
     * (设备/档位/现场点/资产/风险源/小散工程)
     */
    private Long targetId;
    private String targetCode;
    private String targetCategory;
    private String targetName;
    private Long standardId;
    private String standardName;
    private Long itemId;
    private String itemName;
    private String itemTag;
    private String remark;
    private Long resultId;
    private String resultName;
    private Long resultItemId;
    private String resultItemName;
    private Boolean errorFlag;
    private Long fileRefKey;

    /**
     * 默认结果项
     */
    private String defaultResultItem;
    /**
     * 必须拍照的结果项
     */
    private String mustTakePhotoResultItems;
    /**
     * 必须备注的结果项
     */
    private String mustRemarkResultItems;

}
