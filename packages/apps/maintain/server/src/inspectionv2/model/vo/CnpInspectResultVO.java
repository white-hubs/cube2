package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-12-29  11:52
 * @Description:
 * @Version: 1.0
 */
@Data
public class CnpInspectResultVO {
    private Long id;
    private String resultName;
    private String itemName;
    private String itemValue;
    private Boolean errorFlag;
    private Integer dangerFlag;

}
