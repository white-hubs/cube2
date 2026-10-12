package com.guhecloud.cube.customization.xialing.server.liangcang.dto;

import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/** 储量档案报警配置（租户字典 variable）。 */
@Data
public class WarehouseStorageArchiveAlarmConfigDTO {

    private String volumePropCode;
    private String volumeDifferencePropCode;
    private Long volumeDifferenceRuleId;
    private Map<String, Long> volumeDifferenceRuleIds;
    private List<WarehouseRuleConfig> warehouseRules;
    private Map<String, Long> warehouseRuleIds;

    public boolean hasAlarmConfig() {
        return StringUtils.isNotBlank(volumePropCode)
               && (ListUtils.nonEmpty(warehouseRules) || warehouseRuleIds != null && !warehouseRuleIds.isEmpty());
    }

    public void trim() {
        if (StringUtils.isNotBlank(volumePropCode)) {
            volumePropCode = volumePropCode.trim();
        }
        if (StringUtils.isNotBlank(volumeDifferencePropCode)) {
            volumeDifferencePropCode = volumeDifferencePropCode.trim();
        }
    }

    public Long ruleId(Long warehouseId) {
        Long ruleId = null;
        if (ListUtils.nonEmpty(warehouseRules)) {
            ruleId = warehouseRules.stream()
                    .filter(row -> row != null && Objects.equals(row.getWarehouseId(), warehouseId))
                    .map(WarehouseRuleConfig::resolvedRuleId)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        if (ruleId == null && warehouseRuleIds != null) {
            ruleId = warehouseRuleIds.get(String.valueOf(warehouseId));
        }
        return ruleId;
    }

    public Long volumeDifferenceRuleId(Long warehouseId) {
        if (volumeDifferenceRuleIds != null && warehouseId != null) {
            Long ruleId = volumeDifferenceRuleIds.get(String.valueOf(warehouseId));
            if (ruleId != null) {
                return ruleId;
            }
        }
        return volumeDifferenceRuleId;
    }

    @Data
    public static class WarehouseRuleConfig {

        private Long warehouseId;
        private Long emergencyRuleId;
        private Long ruleId;

        public Long resolvedRuleId() {
            return emergencyRuleId == null ? ruleId : emergencyRuleId;
        }
    }
}
