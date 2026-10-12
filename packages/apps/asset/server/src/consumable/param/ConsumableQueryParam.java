package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 耗材管理
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableQueryParam extends Paging {
    private String keyword;
    //名称
    private String name;
    //编码
    private String code;
    //类型ID
    private Long categoryId;
    //品牌
    private String brand;
    //型号
    private String model;
    //商品码
    private String goodsCode;
    /**
     * 只查询超过上限或下线的数据
     */
    private String type;

}