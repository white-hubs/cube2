package com.guhecloud.platform.dcm.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.platform.dcm.entity.DcmDeclaration;
import com.guhecloud.platform.dcm.entity.ModuleCommonSpec;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于以整体文件形态呈现DCM 申明数据的参数模型.
 * 表面看来其对应的实体类与之结构相似，但Param版本用于外部传递数据进入，实际会选择弹性更大的格式。（通常情况下实体类可以直接用作Param, 而反之不然）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DcmDeclarationParam {
    private String schemaVersion;
    private String dataVersion;
    private ModuleCommonSpec common;
    private List<ProvidedConfigSpecParam> provides;
    private List<DcmRequirementParam> requests;

    public DcmDeclaration toEntity() {
        DcmDeclaration declaration = new DcmDeclaration();
        declaration.setCommon(common);
        declaration.setSchemaVersion(schemaVersion);
        declaration.setDataVersion(dataVersion);
        declaration.setProvides(provides.stream().map(ProvidedConfigSpecParam::toEntity).collect(Collectors.toList()));
        declaration.setRequests(requests.stream().map(DcmRequirementParam::toEntity).collect(Collectors.toList()));
        return declaration;
    }
}
