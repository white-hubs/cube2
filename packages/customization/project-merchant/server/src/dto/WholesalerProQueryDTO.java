package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangliang
 * @author 2022/01/17
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WholesalerProQueryDTO extends BaseQueryDTO {

    private String name;

    private Integer processingStatus;

    private String type;

    private String createBy;

    private Integer status;

    private String companyId;

}
