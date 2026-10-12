package com.guhecloud.cube.srv.gen.property.management.server.risk.source.param;

import com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity.RiskSourceLedger;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-19  16:25
 * @Description:
 * @Version: 1.0
 */
@Data
public class RiskSourceLedgerExcelParam extends RiskSourceLedger {
    /**
     * 配件编号
     */
    private String partsCode;
    /**
     * 配件名称
     */
    private String partsName;
    /**
     * 配件到期日期
     */
    private LocalDateTime partsExpireDate;
    /**
     * 配件备注
     */
    private String partsRemarks;
    private String areaName;
}
