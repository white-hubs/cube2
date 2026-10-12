package com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.producer.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 光明理货报备信息
 *
 * @author lanchun
 * @date 2023/4/13 16:47
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GmTallyReportMessage {

    /**
     * 授权码
     */
    private String appKey;

    /**
     * 批次号，必填
     */
    private String batchNo;

    /**
     * 理货机编号，必填
     */
    private String tallyMachineNo;

    /**
     * 商品数量，必填
     */
    private Integer num;

    /**
     * 商品重量，必填
     */
    private BigDecimal weight;

    /**
     * 理货结束时间，必填
     */
    private String tallyEndTime;

    /**
     * 理货详细信息
     */
    private List<GmTallyDetailMessage> details;
}
