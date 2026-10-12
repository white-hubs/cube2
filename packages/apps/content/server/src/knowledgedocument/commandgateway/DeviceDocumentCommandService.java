package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command.SubmitDeviceDocumentCommand;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.DeviceDocument;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/23
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
public interface DeviceDocumentCommandService extends IService<DeviceDocument> {

    /**
     * 新增设备管理
     *
     * @param command 新增参数
     */
    void save(SubmitDeviceDocumentCommand command);

    /**
     * 删除设备管理
     *
     * @param id 主键
     */
    void remove(Long id);

    /**
     * 删除设备管理批量
     *
     * @param id 主键
     */
    void batchRemove(List<Long> id);

}
