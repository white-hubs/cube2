package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class BuildingData extends BasicTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 资料名称
     */
    private String name;

    /**
     * 资料类型
     */
    private Long type;

    /**
     * 所属区域主键
     * 区域已废弃 <a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
     */
    @Deprecated
    private Long areaId;

    /**
     * 所属区域名称
     * 区域已废弃 <a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
     */
    @Deprecated
    private String areaName;

    /**
     * 所属部门主键
     */
    private Long deptId;

    /**
     * 所属部门名称
     * 改版后不再维护内容(原本冗余就没有用，查询的时候还是现查询出来的)
     */
    @Deprecated
    private String deptName;

    /**
     * 所属产品id
     */
    private Long productId;
}
