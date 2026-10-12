package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import lombok.Data;

import java.util.List;

/**
 * @author Appet
 * @Description 耗材管理
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableParam {

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
    //单位
    private String unit;
    //质保期限
    private String qualityPeriod;
    //库存上线
    private Integer stockMax;
    //库存下线
    private Integer stockMin;
    //限领数量（人/月）
    private Integer useLimit;
    //当前库存数量
    private Integer stock;
    //文件ID
    private List<Long> file;

}