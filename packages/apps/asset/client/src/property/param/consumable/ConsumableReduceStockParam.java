package com.guhecloud.cube.property.management.client.property.param.consumable;

import com.guhecloud.cube.property.management.client.property.vo.consumable.ConsumableUseInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/2/5 09:33
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumableReduceStockParam {
    //仓库ID
    private Long storeId;

    private Long deptId;
    //申领人
    private Long claimant;
    //出库日期
    private LocalDateTime reduceDate;

    //处理人
    private Long handler;
    //备注
    private String remark;
    //业务类型
    private String businessType;
    //流程ID
    private String processInstanceId;

    private List<ConsumableStockDetailParam> details;

    public void setDetails(List<ConsumableUseInfo> consumableUseInfos) {
        List<ConsumableStockDetailParam> details = new ArrayList<>();
        for (var consumableUseInfo : consumableUseInfos) {
            var param = new ConsumableStockDetailParam();
            param.setConsumableId(consumableUseInfo.getId());
            param.setNum(consumableUseInfo.getNum());
            param.setPrice(consumableUseInfo.getAvgPrice());
            param.setAmount(new BigDecimal(param.getNum()).multiply(param.getPrice()));
            details.add(param);
        }
        this.details = details;
    }
}
