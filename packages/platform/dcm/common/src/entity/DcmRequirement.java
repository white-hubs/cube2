package com.guhecloud.platform.dcm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 申明式配置中 发送到服务的配置需求规格。
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DcmRequirement {
    private String provider;
    private Boolean isPartialMode;
    private Boolean isFailureOnMissingProvider;
    private List<RequestedConfigItem> requirements;

    public DcmRequirement filterRequirementsByTenant(String tenant) {
        List<RequestedConfigItem> filterdRequirements = requirements.stream()
                .filter(x -> x.matchedWithTenant(tenant))
                .collect(Collectors.toList());

        if (filterdRequirements.size() != requirements.size()) {
            DcmRequirement ret = new DcmRequirement();
            ret.setProvider(provider);
            ret.setIsPartialMode(isPartialMode);
            ret.setIsFailureOnMissingProvider(isFailureOnMissingProvider);
            ret.setRequirements(filterdRequirements);
            return ret;
        } else {
            return this;
        }
    }
}
