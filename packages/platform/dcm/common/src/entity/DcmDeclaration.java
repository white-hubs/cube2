/**
 * 模块的DCM申明定义.
 */
package com.guhecloud.platform.dcm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 申明式配置中的配置申明顶层结构。
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DcmDeclaration {
    private String schemaVersion;               // 结构体版本
    private String dataVersion;                 // 数据版本
    private ModuleCommonSpec common;            // 模块通用规格
    private List<ProvidedConfigSpec> provides;  // 提供的能力
    private List<DcmRequirement> requests;     // 需求满足的条件
}

