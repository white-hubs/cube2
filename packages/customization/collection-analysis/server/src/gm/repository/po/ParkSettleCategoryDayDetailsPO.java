package com.guhecloud.cube.biz.collection.analysis.gm.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-16  18:39
 * @Description:
 * @Version: 1.0
 */
@Data
@TableName("park_settle_category_day_details")
public class ParkSettleCategoryDayDetailsPO {
    private Long id;
    private Long categoryId;
    private String mainCateGory;
    private String mainCateGoryName;
    private String netWeight;
    private String percent;

}
