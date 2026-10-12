package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
/**商铺查询*/
public class CompanyQueryDTO {

    /**项目ID*/
    private String projectId;

    /**搜索关键字：名称、编号、所属人名称*/
    private String search;

    /**是否签订安全协议*/
    private Boolean securitySignd;

}
