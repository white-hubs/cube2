package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.dao.DeviceDocumentMapper;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.DeviceDocument;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.DeviceDocumentQueryService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query.DeviceDocumentCollectionQuery;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/30
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceDocumentQueryServiceImpl extends ServiceImpl<DeviceDocumentMapper, DeviceDocument> implements DeviceDocumentQueryService {

    @Override
    public Page<DeviceDocument> pageDeviceDocument(@NonNull DeviceDocumentCollectionQuery query) {
        return this.baseMapper.getDeviceDocumentPage(PagingUtils.convert(query), query);
    }

    @Override
    public List<DeviceDocument> deviceDocumentList(DeviceDocumentCollectionQuery query) {
        return this.baseMapper.deviceDocumentList(query);
    }

}
