package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.BuildingDataVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.BuildingData;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query.BuildingDataCollectionQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lzy
 * @date 2022/5/19
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@Mapper
public interface BuildingDataMapper extends BaseMapper<BuildingData> {
    Page<BuildingDataVO> getBuildingPage(Page<?> page, @Param("query") BuildingDataCollectionQuery query);
}
