package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import lombok.Data;

import java.util.List;

/**
 * 理货商品明细信息
 *
 * @author lzy
 * @date 2021/11/24
 */
@Data
public class PorkStatArrangeDetailVO {

    /**
     * 理货完成批次数
     */
    private Integer reportArrangeTotal;

    /**
     * 作弊嫌疑批次数
     */
    private Integer reportSuspicionTotal;

    /**
     * 理货批次详情
     */
    private List<PorkStatVO.PorkStatArrange> details;

    /**
     * 页数总数
     */
    private Integer total;

}
