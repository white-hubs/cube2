package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 设备建档数查询
 * @date 2023/9/14 11:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CountPropertyFilingNumQuery extends BaseQuery {
    /**
     * 设备类型
     */
    private String deviceType;

}
