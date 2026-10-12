package com.guhecloud.cube.biz.meattrade.server.wallet.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 交易业务信息
 *
 * @author Woody
 * @date 2021/9/18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionBusinessVO {
    /**
     * 业务编号
     */
    private String code;

    /**
     * 业务名称
     */
    private String name;
}
