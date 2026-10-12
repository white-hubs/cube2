package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 理货片数食安溯源信息
 *
 * @author lzy
 * @date 2021/11/20
 */
@Data
public class SpotCheckResourceAppVO {

    /**
     * 商户主键
     */
    private Long supplierId;
    /**
     * 商户名称
     */
    private String supplierName;

    /**
     * 商户编码
     */
    private String supplierCode;

    /**
     * 来货报备号
     */
    private String porkReportCode;

    /**
     * 报备商品品类主键
     */
    private String goodsCategoryId;

    /**
     * 报备商品品类名称
     */
    private String goodsCategoryName;

    /**
     * 冷热鲜标识
     */
    private MeatGoodsFreshLabelEnum freshLabel;

    /**
     * 电子标签
     */
    private String label;

    /**
     * 脚标
     */
    private String footMarkCode;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 溯源信息
     */
    private List<SpotCheckResourceAppVO.spotCheckResource> resources;

    /**
     * 溯源信息
     */
    @Data
    public static class spotCheckResource {
        /**
         * 标题
         */
        private String title;

        /**
         * 溯源信息值
         */
        private Map<Object, Object> values;
    }

}
