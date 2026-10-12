package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangliang
 * @author 2022/01/10
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WholesalerProtocolTemplateQueryDTO extends BaseQueryDTO {

    private String name;

    private String type;
}
