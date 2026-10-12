package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-15  11:44
 * @Description:
 * @Version: 1.0
 */
@Data
public class OperatingPriceDTO {
    /**
     * 商户
     */
    private String companyName;
    /**
     * 管理档位
     */
    private String deviceName;
    /**
     * 商户姓名
     */
    private String owner;
    /**
     * 商品质量
     */
    private String zScoreAvg="5";
    /**诚实守信*/
    private String sScoreAvg="5";
    /**服务态度*/
    private String tScoreAvg="5";
    /**环境卫生*/
    private String wScoreAvg="5";
    /**诚信经营*/
    private String honestyScore="5";
}
