package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.DeviceDocumentRefProductService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.dao.DeviceDocumentRefProductMapper;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.DeviceDocumentRefProduct;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceDocumentRefProductServiceImpl extends ServiceImpl<DeviceDocumentRefProductMapper, DeviceDocumentRefProduct> implements DeviceDocumentRefProductService {

    @Override
    public List<DeviceDocumentRefProduct> getRefProductByDocumentId(Long documentId) {
        return this.getRefProductByDocumentIds(Collections.singleton(documentId));
    }

    @Override
    public List<DeviceDocumentRefProduct> getRefProductByDocumentIds(Set<Long> documentIds) {
        if (CollectionUtils.isEmpty(documentIds)) {
            return Collections.emptyList();
        }
        return this.list(Wrappers.<DeviceDocumentRefProduct>lambdaQuery()
                                 .in(DeviceDocumentRefProduct::getDeviceDocumentId, documentIds));
    }
}
