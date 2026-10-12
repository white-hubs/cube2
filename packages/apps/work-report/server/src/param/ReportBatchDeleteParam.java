package com.guhecloud.cube.srv.gen.reportmanagement.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * 工作汇报批量删除
 *
 * @author lanchun
 * @date 2025/4/23 14:28
 */
@Data
public class ReportBatchDeleteParam {

    /**
     * 删除的工作汇报单集合
     */
    @NotNull(message = "删除的工作汇报单不能为空")
    private Set<String> processInstanceIds;
}
