package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command.SubmitBuildingDataCommand;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.BuildingData;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/19
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
public interface BuildingDataCommandService extends IService<BuildingData> {

    /**
     * 新增建筑资料
     *
     * @param command 新增参数
     */
    void submitBuildingData(SubmitBuildingDataCommand command);

    /**
     * 删除建筑资料
     *
     * @param id 主键
     */
    void remove(Long id);

    /**
     * 删除建筑资料批量
     *
     * @param ids 主键
     */
    void batchRemove(List<Long> ids);

}
