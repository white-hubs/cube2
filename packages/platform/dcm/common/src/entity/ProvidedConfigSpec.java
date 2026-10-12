package com.guhecloud.platform.dcm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 提供的配置规格定义.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProvidedConfigSpec {
    private String type;
    private List<String> versions;
}
