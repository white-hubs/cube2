package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.srv.gen.content.management.server.announcement.enumeration.ContentFileSceneCode;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.BuildingDataCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command.SubmitBuildingDataCommand;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.dao.BuildingDataMapper;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.BuildingData;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @date 2022/5/19
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 */
@Deprecated
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BuildingDataCommandServiceImpl extends ServiceImpl<BuildingDataMapper, BuildingData> implements BuildingDataCommandService {

    private final FileRefClient fileRefClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitBuildingData(@NonNull SubmitBuildingDataCommand command) {
        // 构建信息
        List<BuildingData> saves = this.buildBuildingData(command);

        // 新增
        this.saveBatch(saves);

        // 新增文件
        this.saveFile(saves.stream().map(BuildingData::getId).collect(Collectors.toList()), command.getFileIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public void batchRemove(List<Long> ids) {
        ids.forEach(s -> {
            DatabaseContextHolder.softDelete(this, s);
        });
    }

    private void saveFile(List<Long> businessKeys, List<Long> fileIds) {
        List<FileRefCreateParam> saves = new ArrayList<>();
        if (businessKeys.size() == fileIds.size()) {
            for (int i = 0; i < businessKeys.size(); i++) {
                FileRefCreateParam param = new FileRefCreateParam();
                param.setFileId(fileIds.get(i));
                param.setBusinessKey(businessKeys.get(i));
                param.setSceneCode(ContentFileSceneCode.CONTENT_BUILDING_DATA.getCode());
                saves.add(param);
            }
            this.fileRefClient.refInduceBatch(saves);
        }
    }

    private List<BuildingData> buildBuildingData(SubmitBuildingDataCommand command) {
        return command.getFileIds().stream().map(fileId -> ObjectUtils.copying(command, BuildingData.class)).collect(Collectors.toList());
    }
}
