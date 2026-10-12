package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-12-28  15:46
 * @Description:
 * @Version: 1.0
 */
@Data
public class CnpInspectItemVO {
    private Long id;
    private Long inspectStandardId;
    private String inspectItemName;
    private String parentItemId;
    private String inspectItemCode;
    private Long inspectResultId;
    private String imageResult;
    private String funCode;
    private Integer textFlag;

    /**
     * 巡检项tag
     */
    private String itemTag;

}
