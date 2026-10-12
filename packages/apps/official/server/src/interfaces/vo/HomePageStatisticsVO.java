package com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/31 10:46
 * @Description: 行政管理统计返回信息
 */
@Data
public class HomePageStatisticsVO {

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务名称
     */
    private String businessTypeName;

    /**
     * 申请数
     */
    private Integer applyNum;

}
