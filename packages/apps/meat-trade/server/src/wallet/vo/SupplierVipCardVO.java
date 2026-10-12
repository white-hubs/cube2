package com.guhecloud.cube.biz.meattrade.server.wallet.vo;

import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商户会员卡信息
 *
 * @author Woody
 * @date 2021/8/22
 */
@Data
public class SupplierVipCardVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 会员卡号
     */
    private String code;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 商户信息
     */
    private SupplierDTO supplier;
}
