package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-03  16:14
 * @Description:
 * @Version: 1.0
 */
@Data
public class CnpInspectStandardTypeReferVO {
    private Long inspectStandardId;
    private String referTypeCode;
    private String referTypeId;
    private String defaultStandard;
}
