package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务来源信息
 *
 * @author lanchun
 * @date 2024/4/10 16:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CultivateTaskSourceParam {

    /**
     * 任务来源编号
     */
    private String sourceCode;
    /**
     * 任务来源名称
     */
    private String sourceName;
    /**
     * 任务来源键
     */
    private String sourceKey;
}
