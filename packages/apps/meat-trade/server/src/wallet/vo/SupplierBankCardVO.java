package com.guhecloud.cube.biz.meattrade.server.wallet.vo;

import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 商户银行卡信息
 *
 * @author Woody
 * @date 2021/8/22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierBankCardVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 银行卡号
     */
    private String code;

    /**
     * 所属银行
     */
    private String bank;

    /**
     * 持卡人姓名
     */
    private String owner;

    /**
     * 开户行名称
     */
    private String issuer;

    /**
     * 身份证号
     */
    private String idcard;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 商户信息
     */
    private SupplierDTO supplier;
}
