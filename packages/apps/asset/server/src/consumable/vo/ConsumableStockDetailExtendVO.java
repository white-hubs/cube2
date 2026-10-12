package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Appet
 * @Description 耗材出入库明细
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableStockDetailExtendVO extends ConsumableVO {
    //仓库ID
    private Long storeId;
    private String storeName;
    //入库数量
    private Integer num;
    //入库金额
    private BigDecimal storeAmount;
    //入库价格
    private BigDecimal price;

    //初始数量
    private BigDecimal initNum;
    //初始金额
    private BigDecimal initAmount;
    //初始价格
    private BigDecimal initPrice;

    //结余数量
    private BigDecimal endNum;
    //结余金额
    private BigDecimal endAmount;
    //结余价格
    private BigDecimal endPrice;
    //处理人
    private String handler;
    //处理时间
    private LocalDateTime handleDate;
    //出入库标志
    private String stockFlag; //ADD 入库 REDUCE 出库
    //耗材附件
    private List<FileVO> files;

    private String fileIds;
    private String createBy;
    private String creatorId;
    private ActorTypeEnum creatorType;
}