package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangliang
 * @author 2022/01/18
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WholesalerQueryDTO extends BaseQueryDTO {
    private String name;

    private String owner;

    private String mobile;
}
