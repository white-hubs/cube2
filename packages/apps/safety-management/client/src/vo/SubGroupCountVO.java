package com.guhecloud.cube.base.dataintegrate.client.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/9 16:22
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SubGroupCountVO extends GroupCountVO {
    private String subGroupKey;

    private String subGroupName;
}
