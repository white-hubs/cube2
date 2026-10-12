package com.guhecloud.cube.base.message2.server.sat.process.pipeline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 流程上下文信息
 *
 * @author lanchun
 * @date 2023/5/8 11:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class ProcessContext<T extends ProcessModel> {

    /**
     * 标识责任链的code
     */
    private String code;

    /**
     * 存储责任链上下文数据的模型
     */
    private T processModel;

    /**
     * 责任链中断的标识
     */
    private Boolean needBreak;

}
