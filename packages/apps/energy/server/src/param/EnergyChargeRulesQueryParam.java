package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EnergyChargeRulesQueryParam extends Paging {
    private Long id;

    /**
     * 用能类型code(InstrumentTypeEnum)
     */
    private String instrumentType;

    private List<String> instrumentTypeList;

    /**
     * 生效时间(这个参数是获取生效规则列表getInstrumentEffectiveRule方法独有的参数)
     */
    private LocalDateTime effectiveTime;
}
