package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/15
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerIdBatchDTO {
    private List<String> ids;
}
