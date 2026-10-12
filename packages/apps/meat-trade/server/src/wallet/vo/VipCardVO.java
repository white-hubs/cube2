package com.guhecloud.cube.biz.meattrade.server.wallet.vo;

import lombok.Data;

/**
 * 会员卡信息
 *
 * @author Woody
 * @date 2021/8/22
 */
@Data
public class VipCardVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 会员卡号
     */
    private String code;
}
