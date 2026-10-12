package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author lzy
 * @date 2022/5/19
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@EqualsAndHashCode(callSuper = true)
@Data
public class BuildingDataCollectionQuery extends Paging {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 资料类型
     */
    private Long type;

    /**
     * 部门主键
     */
    private Long deptId;

    /**
     * 区域主键
     */
    private Long areaId;

    /**
     * 区域主键集合
     */
    private Set<Long> areaIds;
}
