package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Appet
 * @Description 耗材管理
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableVO {

    //
    private Long id;
    //名称
    private String name;
    //编码
    private String code;
    //类型ID
    private Long categoryId;
    //类型名称
    private String categoryName;
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
    private BigDecimal stockMax;
    //库存下线
    private BigDecimal stockMin;
    //限领数量（人/月）
    private BigDecimal useLimit;
    //当前库存数量
    private Integer stock;
    //金额
    private BigDecimal amount;

    private String fileIds;
    /**
     * 附件
     */
    private List<FileVO> file;
    //数量
    private Integer num;
    //单价
    private BigDecimal price;
    private BigDecimal avgPrice;
    private String status;

}