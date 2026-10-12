package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AppConsumablePageVO {
    private Long id;
    /**
     * 应盘数量
     */
    private Integer checkTotalNumber;
    /**
     * 实盘数量
     */
    private Integer checkCompleteNumber;
    /**
     * 耗材名称
     */
    private String name;
    /**
     * 耗材编码
     */
    private String code;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 单价
     */
    private BigDecimal avgPrice;
    /**
     * 单位
     */
    private String unit;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 总金额
     */
    private BigDecimal amount;
    /**
     * 库存下限
     */
    private BigDecimal stockMin;
    /**
     * 库存上限
     */
    private BigDecimal stockMax;
    /**
     * 附件
     */
    private List<FileVO> file;
    private String fileIds;
    private Integer stock;
}
