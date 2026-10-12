package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 知识文档关联产品信息
 *
 * @author lanchun
 * @date 2024/1/17 16:01
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceDocumentRefProduct extends BasicTenantEntity<Long> {

    private Long id;

    /**
     * 知识文档主键
     */
    private Long deviceDocumentId;

    /**
     * 设备产品主键
     */
    private Long deviceProdId;

    /**
     * 设备产品名称
     */
    private String deviceProdName;
}
