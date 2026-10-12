package com.guhecloud.cube.base.flow.flowable.common.resovler;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/7/23 15:33
 * @description
 */
@Data
@AllArgsConstructor
public class ResolvedTypeData {
    private String name;
    private String type;
    private Long id;

    public ResolvedTypeData(String type, Long id) {
        this.type = type;
        this.id = id;
    }
}
