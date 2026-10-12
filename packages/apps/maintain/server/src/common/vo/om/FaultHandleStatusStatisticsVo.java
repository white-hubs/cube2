package com.guhecloud.cube.srv.gen.maintain.server.common.vo.om;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * author: ht
 * create: 2025-06-04 15:13
 */
@Data
@Builder
public class FaultHandleStatusStatisticsVo {
    /**
     * 状态名称 WAITING:处理中 REJECT:驳回 UNDO:撤销 PASS:完成
     */
    private String statusType;
    /**
     * 状态比例
     */
    private BigDecimal rate;
    /**
     * 数量
     */
    private Long num;
}
