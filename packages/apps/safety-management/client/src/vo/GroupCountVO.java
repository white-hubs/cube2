package com.guhecloud.cube.base.dataintegrate.client.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/9 16:21
 * @Description:
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GroupCountVO {
    private String groupKey;

    private Integer count;
}
