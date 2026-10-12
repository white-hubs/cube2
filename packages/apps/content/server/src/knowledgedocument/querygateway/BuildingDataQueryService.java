package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.BuildingDataVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.BuildingData;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query.BuildingDataCollectionQuery;

/**
 * @author lzy
 * @date 2022/5/19
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
public interface BuildingDataQueryService extends IService<BuildingData> {

    /**
     * 建筑资料分页
     *
     * @param query 查询参数
     *
     * @return 建筑资料信息
     */
    Page<BuildingDataVO> pageBuildingData(BuildingDataCollectionQuery query);
}
