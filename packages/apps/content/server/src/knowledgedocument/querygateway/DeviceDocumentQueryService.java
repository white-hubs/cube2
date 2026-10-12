package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.DeviceDocument;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query.DeviceDocumentCollectionQuery;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/30
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
public interface DeviceDocumentQueryService extends IService<DeviceDocument> {

    /**
     * 设备文档分页
     *
     * @param query 查询参数
     *
     * @return 设备文档信息
     */
    Page<DeviceDocument> pageDeviceDocument(DeviceDocumentCollectionQuery query);

    /**
     * 设备文档列表
     *
     * @param query
     *
     * @return
     */
    List<DeviceDocument> deviceDocumentList(DeviceDocumentCollectionQuery query);
}
