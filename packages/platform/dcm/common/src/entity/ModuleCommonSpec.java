package com.guhecloud.platform.dcm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 提供的模块通用说明. 目前仅占位, 未做特殊处理.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ModuleCommonSpec {
    private String appCode;
    private String appDescription;
}
