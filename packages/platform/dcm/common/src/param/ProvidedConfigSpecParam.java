package com.guhecloud.platform.dcm.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.platform.dcm.entity.ProvidedConfigSpec;
import com.guhecloud.platform.dcm.utils.DcmTools;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProvidedConfigSpecParam {
    private String type;
    private Object versions;

    public ProvidedConfigSpec toEntity() {
        ProvidedConfigSpec spec = new ProvidedConfigSpec();
        spec.setType(type);
        spec.setVersions(versions == null ? null : DcmTools.parseStrColl(versions, ","));
        return spec;
    }
}
