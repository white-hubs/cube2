package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import lombok.Data;

import java.time.LocalDateTime;

/** 货位卡字段变更记录。 */
@Data
public class WarehouseLocationCardFieldChangeLogVO {
    /** 变更记录 id。 */
    private Long id;
    /** 粮仓 id。 */
    private Long warehouseId;
    /** 业务类型。 */
    private String bizType;
    /** 业务记录 id。 */
    private Long bizId;
    /** 变更摘要，多个字段名用、拼接。 */
    private String changeSummary;
    /** 变更明细 JSON。 */
    private String changeContent;
    /** 变更人。 */
    private String createBy;
    /** 变更时间。 */
    private LocalDateTime createTime;
}
