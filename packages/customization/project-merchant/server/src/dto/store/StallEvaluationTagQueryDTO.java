package com.guhecloud.cube.deliver.project.merchant.dto.store;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 档位评价标签
 * </p>
 *
 * @author bq
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StallEvaluationTagQueryDTO extends BaseQueryDTO {

    /**
     * 评价维度
     */
    private String evaluation;

    /**
     * 评价分类
     */
    private String category;

}
