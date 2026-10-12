package com.guhecloud.cube.srv.gen.energymanagement.server.entity;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.srv.gen.energymanagement.server.dto.StairsEnergyContentsDTO;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 用能计费规则
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnergyChargeRules extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String ruleName;
    /**
     * 用能类型code(InstrumentTypeEnum)
     */
    private String instrumentType;
    /**
     * 用能类型名称
     */
    private String instrumentTypeName;
    /**
     * 生效时间
     */
    private LocalDateTime effectiveTime;
    /**
     * 是否复费率(true为付费率，其他为单费率)
     */
    private Boolean complexFeeRate;
    /**
     * 阶梯用量类型(StairsEnergyType)
     */
    private String stairsEnergyType;
    /**
     * 阶梯用量内容(StairsEnergyContentsDTO)
     */
    private String stairsEnergyContents;

    /**
     * 获取解析后的阶梯用量内容
     *
     * @return 阶梯用量
     */
    public List<StairsEnergyContentsDTO> getStairsEnergyContentsList() {
        if (StringUtils.isNotBlank(stairsEnergyContents)) {
            return JSON.parseArray(stairsEnergyContents, StairsEnergyContentsDTO.class);
        }
        return Collections.emptyList();
    }
}
