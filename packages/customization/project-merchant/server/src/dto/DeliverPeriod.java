package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-15  13:50
 * @Description:
 * @Version: 1.0
 */
@Data
public class DeliverPeriod {
    private int pageNumber = 1;
    private int pageSize = 5;
    private Period beforePeriod;
    private Period period;
    private String code;

}
