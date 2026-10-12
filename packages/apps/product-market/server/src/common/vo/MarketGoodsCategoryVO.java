package com.guhecloud.cube.srv.gen.product.market.server.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 商品品类
 * @date 2023-05-10 15:08:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketGoodsCategoryVO {

    //主键
    private Long id;
    //商品分类编号
    private String code;
    //商品分类名称
    private String name;
    //父级名称
    private String parentName;

    private String sequence;

    private Boolean priced;//是否采过价
    //采价记录ID
    private Long pricingRecordId;
    //采价人
    private String userName;
    //采价时间
    private LocalDateTime createTime;

    public MarketGoodsCategoryVO(String name) {
        this.name = name;
    }

    public MarketGoodsCategoryVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}