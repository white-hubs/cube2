package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangliang
 * @author 2022/01/12
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WholesalerQuesTempQueryDTO extends BaseQueryDTO {

    private String code;

    private String type;

    private String name;

    private String createBy;
}
