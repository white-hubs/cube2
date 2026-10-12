package com.guhecloud.cube.gm.meattrade.plugin.couple.application.querygateway.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * 挂钩状态查询条件
 *
 * @author lanchun
 * @date 2023/7/26 14:18
 */
@Data
public class CoupleStatusQuery {

    /**
     * 挂钩集合
     */
    @NotNull(message = "标签号码不能为空")
    @Size(min = 1, message = "标签号码不能为空")
    @JsonProperty("tag_ids")
    private Set<String> tagIds;
}
