package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.DeviceDocumentRefProduct;

import java.util.List;
import java.util.Set;

/**
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
public interface DeviceDocumentRefProductService extends IService<DeviceDocumentRefProduct> {

    List<DeviceDocumentRefProduct> getRefProductByDocumentId(Long documentId);

    List<DeviceDocumentRefProduct> getRefProductByDocumentIds(Set<Long> documentIds);
}
