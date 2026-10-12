package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangliang
 * @author 2022/01/13
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WholesalerContractTypeQueryDTO extends BaseQueryDTO {

    private Integer enabled;
}
