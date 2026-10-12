package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.DeviceDocumentRefProduct;
import org.apache.ibatis.annotations.Mapper;

/**
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@Mapper
public interface DeviceDocumentRefProductMapper extends BaseMapper<DeviceDocumentRefProduct> {
}
