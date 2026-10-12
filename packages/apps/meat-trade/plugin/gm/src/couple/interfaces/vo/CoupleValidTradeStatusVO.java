package com.guhecloud.cube.gm.meattrade.plugin.couple.interfaces.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 挂钩状态校验返回参数
 *
 * @author lanchun
 * @date 2023/7/26 14:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoupleValidTradeStatusVO {

    private boolean pass;

    @JsonProperty("passed_ids")
    private Set<String> passedIds;

}
