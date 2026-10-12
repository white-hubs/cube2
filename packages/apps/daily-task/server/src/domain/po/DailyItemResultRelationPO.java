package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 执行项的结果关联表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("daily_item_result_relation")
public class DailyItemResultRelationPO extends BasicEnhancedEntity<Long> {
    private Long id;
    /**
     * 执行项ID
     */
    private Long itemId;
    /**
     * 执行结果ID
     */
    private Long resultId;
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
