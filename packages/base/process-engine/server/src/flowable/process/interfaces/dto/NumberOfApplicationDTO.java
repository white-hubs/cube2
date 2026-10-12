package com.guhecloud.cube.base.flow.flowable.process.interfaces.dto;

import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.ApplicationId;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumberOfApplicationDTO {
    private String applicationId;

    private Long count;

    public ApplicationId getApplicationId() {
        return JsonUtils.deserialize(applicationId, ApplicationId.class);
    }
}
