package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangliang
 * @author 2022/01/08
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WholesalerAuthQueryDTO extends BaseQueryDTO {

    private String createBy;

    private Integer status;

    private String companyId;

    private String stallId;

}
