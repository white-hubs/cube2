package com.guhecloud.cube.srv.gen.property.management.server.risk.source.ev.vo;

import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 电动车统计分页列表
 *
 * @author lanchun
 * @date 2025/3/18 21:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElectronMobileStatPageVO extends RiskSourceLedgerVO {

    /**
     * 违停次数
     */
    private Integer parkingNum;

    /**
     * 最后一次违规停车巡检项明细
     */
    private String lastParkingInspectItem;

    /**
     * 充电次数
     */
    private Integer chargeNum;

    /**
     * 最后一次违规充电巡检项明细
     */
    private String lastChargeInspectItem;

}
