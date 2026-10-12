package com.guhecloud.cube.biz.daily.task.commons.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 记录执行内容表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_record_item")
@EqualsAndHashCode(callSuper = true)
public class DailyRecordItemPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long recordId;
    private String dailyType;
    private Long taskId;
    private String taskName;
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
