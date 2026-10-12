package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 结算结果匹配率
 * @date 2023/5/5 11:50
 */
@NoArgsConstructor
@Data
public class CountRatioParam {
    Long totalItem;
    Long notMatchItem;

    public CountRatioParam(Long totalItem, Long notMatchItem) {
        this.totalItem = totalItem;
        this.notMatchItem = notMatchItem;
    }
}
