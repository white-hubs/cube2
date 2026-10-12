package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-15  11:26
 * @Description:
 * @Version: 1.0
 */
@Data
public class MerchantCountDTO {
    /**
     * 商户总数（个）
     */
    private Integer merchantTotal;
    /**
     * 档位总数（个）
     */
    private Integer gearTotal;
}
