package com.guhecloud.cube.biz.mdata.server.area.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.biz.mdata.server.area.dao.AreaExtendMapper;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaExtend;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaExtendParam;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaExtendService;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaExtendServiceImpl extends ServiceImpl<AreaExtendMapper, AreaExtend> implements AreaExtendService {

    private final FileOperateClient fileOperateClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull Long areaId, @NonNull AreaExtendParam areaExtendParam) {
        AreaExtend areaExtend = this.baseMapper.selectById(areaId);
        if (Objects.nonNull(areaExtend)) {
            //修改
            convertAreaExtend(areaId, areaExtendParam, areaExtend);
            this.baseMapper.updateById(areaExtend);
        } else {
            //新增
            areaExtend = new AreaExtend();
            convertAreaExtend(areaId, areaExtendParam, areaExtend);
            this.baseMapper.insert(areaExtend);
        }
        // 文件持久化
        this.fileOperate(areaId, areaExtendParam);
        return areaExtend.getId();
    }

    private void convertAreaExtend(Long areaId, AreaExtendParam areaExtendParam, AreaExtend areaExtend) {
        areaExtend.setId(areaId);
        areaExtend.setIntroduce(areaExtendParam.getIntroduce());
        areaExtend.setPictureFileIds(CollectionUtils.isEmpty(areaExtendParam.getPictureFiles()) ? null
                                                                                                : areaExtendParam.getPictureFiles().stream()
                                             .collect(Collectors.joining(",")));
        areaExtend.setVideoFileIds(CollectionUtils.isEmpty(areaExtendParam.getVideoFiles()) ? null
                                                                                            : areaExtendParam.getVideoFiles().stream()
                                           .collect(Collectors.joining(",")));
    }

    @Override
    public AreaExtend getById(@NonNull Long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public Set<AreaExtend> getByIds(@NonNull Set<Long> areaIds) {
        return CollectionUtils.isEmpty(areaIds) ? Collections.emptySet() : this.baseMapper.selectList(Wrappers.<AreaExtend>lambdaQuery()
                                                                                                              .in(AreaExtend::getId, areaIds)
                                                                                                     ).stream().collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long areaId) {
        DatabaseContextHolder.softDelete(this, areaId);
    }

    /**
     * 文件持久化
     *
     * @param areaId 区域id(关联业务主键)
     * @param param  附件对象
     */
    private void fileOperate(Long areaId, AreaExtendParam param) {
        String fileSceneCode = "MDATA::SPACE_LIST";
        List<Long> fileIdList = new ArrayList<>();
        if (param != null && param.getPictureFiles() != null && !CollectionUtils.isEmpty(param.getPictureFiles())) {
            param.getPictureFiles().forEach(file -> fileIdList.add(Long.valueOf(file)));
        }
        if (param != null && param.getVideoFiles() != null && !CollectionUtils.isEmpty(param.getVideoFiles())) {
            param.getVideoFiles().forEach(file -> fileIdList.add(Long.valueOf(file)));
        }
        if (!CollectionUtils.isEmpty(fileIdList)) {
            FileInduceParam fileInduceParam = new FileInduceParam();
            fileInduceParam.setFileIdList(fileIdList);
            fileInduceParam.setBusinessKey(areaId);
            fileInduceParam.setSceneCode(fileSceneCode);

            fileOperateClient.fileInduceSingleBody(fileInduceParam);
        }
    }
}
