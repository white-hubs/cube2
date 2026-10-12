package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author benhuabao
 * @date 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FaqQueryDTO extends BaseQueryDTO {

    private String type;
}
