package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.srv.gen.energymanagement.server.dto.StairsEnergyContentsDTO;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EnergyChargeRulesVO {
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
    private LocalDateTime createTime;
    private String createBy;
    List<StairsEnergyContentsDTO> stairsEnergyContentsList;

    /**
     * 获取阶梯用量集合
     *
     * @return 阶梯用量集合
     */
    public List<StairsEnergyContentsDTO> getStairsEnergyContentsList() {
        if (StringUtils.isNotBlank(stairsEnergyContents) && CollectionUtils.isEmpty(stairsEnergyContentsList)) {
            return JSON.parseArray(stairsEnergyContents, StairsEnergyContentsDTO.class);
        }
        return stairsEnergyContentsList;
    }
}
