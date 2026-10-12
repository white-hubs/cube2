package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-12-28  15:46
 * @Description:
 * @Version: 1.0
 */
@Data
public class CnpInspectStandardVO {
    private Long id;
    //	标准名称
    private String standardName;
    //	标准编码
    private String standardCode;
    //	标准类型
    private String standardTypeId;
    //	标准分类： 1 实体 2 商铺
    private String standardCategory;
    //	巡检类型ID
    private Long inspectCategoryId;

}
