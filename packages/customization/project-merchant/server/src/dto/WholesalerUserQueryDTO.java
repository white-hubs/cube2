package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangliang
 * @author 2022/01/05
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WholesalerUserQueryDTO extends BaseQueryDTO {

    private String name;

    private Integer gender;

    private String mobile;

    private String identityCard;

    private Boolean owner;

    private String companyName;

    private String companyId;

    private String stallName;

    private String stallId;
}
