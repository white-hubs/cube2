package com.guhecloud.cube.srv.gen.property.management.server.consumable.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Appet
 * @Description 仓库耗材库存
 * @date 2023-03-16 10:08:36
 */
@Data
public class ConsumableStoreStockExtendVO extends ConsumableVO {
    //部门ID
    private Long deptId;
    //部门名称
    private String deptName;
    //仓库ID
    private Long storeId;
    private String storeName;
    //库存量
    private Integer storeNum;
    //仓库金额
    private BigDecimal storeAmount;
    //均价
    private BigDecimal avgPrice;
    //存放位置（扩展字段）
    private String position;
    //盘点数量
    private Integer checkNum;
    //盘点单ID
    private Long checkId;
    //盘点状态
    private String status;
    //出库日期
    private LocalDateTime reduceDate;
    /**
     * 库存上线
     */
    private BigDecimal stockMax;
    /**
     * 库存下线
     */
    private BigDecimal stockMin;
    //耗材附件
    private List<FileVO> files;
    /**
     * 盘点操作时间
     */
    private LocalDateTime checkTime;
    /**
     * 盘点操作人
     */
    private String checkerName;
}