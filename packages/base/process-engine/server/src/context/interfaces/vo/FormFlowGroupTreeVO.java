package com.guhecloud.cube.base.flow.context.interfaces.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程表单分组树节点
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FormFlowGroupTreeVO extends Tree.Simple<Long, FormFlowGroupTreeVO> {

    /**
     * 分组名称
     */
    private String name;

    /**
     * 应用
     */
    private String application;
}
