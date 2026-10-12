package com.guhecloud.platform.dcm.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.platform.dcm.entity.DcmRequirement;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DcmRequirementParam {
    private String provider;

    private Boolean isPartialMode;
    private Boolean useBuiltinConfig;
    private Boolean isFailureOnMissingProvider;

    private List<RequestedConfigItemParam> requirements;

    public DcmRequirement toEntity() {
        DcmRequirement dcmRequirement = new DcmRequirement();
        dcmRequirement.setProvider(provider);
        dcmRequirement.setIsPartialMode(isPartialMode == null || isPartialMode);
        dcmRequirement.setIsFailureOnMissingProvider(isFailureOnMissingProvider == null || isFailureOnMissingProvider);
        dcmRequirement.setRequirements(requirements == null ? Collections.emptyList() :
                                       requirements.stream().map(RequestedConfigItemParam::toEntity).collect(Collectors.toList()));
        return dcmRequirement;
    }

    public static DcmRequirementParam fromEntity(DcmRequirement entity) {
        DcmRequirementParam param = new DcmRequirementParam();
        param.setProvider(entity.getProvider());
        param.setIsPartialMode(entity.getIsPartialMode());
        param.setIsFailureOnMissingProvider(entity.getIsFailureOnMissingProvider());
        param.setRequirements(entity.getRequirements() == null ? null :
                              entity.getRequirements().stream()
                                      .map(RequestedConfigItemParam::fromEntity)
                                      .collect(Collectors.toList()));
        return param;
    }
}
