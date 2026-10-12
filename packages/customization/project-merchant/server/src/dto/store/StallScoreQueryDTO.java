package com.guhecloud.cube.deliver.project.merchant.dto.store;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 档位总分值（安全生产值+评分）
 * </p>
 *
 * @author bq
 * @since 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StallScoreQueryDTO extends BaseQueryDTO {
    // 倒序
    private boolean reverseOrder;

    private String timeRange;

    private String stallId;

    private Integer year;

    private Integer minScore;

    private Integer maxScore;

    private Integer underStar;

    private String type;
    private String bimId;

}
