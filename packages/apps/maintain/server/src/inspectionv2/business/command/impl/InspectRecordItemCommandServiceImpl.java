package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordItemCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectRecordItemMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordItemCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordItemPO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectRecordItemCommandServiceImpl extends ServiceImpl<InspectRecordItemMapper, InspectRecordItemPO> implements InspectRecordItemCommandService {

    private final FileRefClient fileRefClient;

    @Override
    public int saveByRecord(Long recordId, List<InspectRecordItemCommandModel> commandModels) {
        AtomicInteger abnormalCount = new AtomicInteger(0);

        Optional.ofNullable(commandModels).orElse(Collections.emptyList())
                .forEach(commandModel -> {

                    InspectRecordItemPO recordItemPO = ObjectUtils.copying(commandModel, InspectRecordItemPO.class);
                    recordItemPO.setRecordId(recordId);
                    if (recordItemPO.getErrorValueFlag()) {
                        abnormalCount.getAndIncrement();
                    }

                    // 数据保存
                    this.save(recordItemPO);

                    // 生成图片
                    this.buildFiles(recordItemPO.getId(), commandModel.getFileIds());

                });

        return abnormalCount.get();
    }

    private void buildFiles(Long id, List<Long> fileIds) {
        List<FileRefCreateParam> params = Optional.ofNullable(fileIds).orElse(Collections.emptyList())
                .stream().map(fileId -> {
                    FileRefCreateParam param = new FileRefCreateParam();
                    param.setFileId(fileId);
                    param.setBusinessKey(id);
                    return param;
                }).collect(Collectors.toList());

        if (params.size() > 0) {
            this.fileRefClient.saveBatch(params);
        }
    }
}
