package com.guhecloud.platform.dcm.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ModuleCommonSpecParam {
    private String appCode;
    private String appDescription;
}
