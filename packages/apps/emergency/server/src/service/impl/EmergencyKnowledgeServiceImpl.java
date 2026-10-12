package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyKnowledgeMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyKnowledge;
import com.guhecloud.cube.biz.emergency.server.eumeration.EmergencyFileSceneCodeEnum;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyKnowledgeParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyKnowledgeQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyKnowledgeService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyKnowledgeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：应急知识（技术）管理服务层
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyKnowledgeServiceImpl extends ServiceImpl<EmergencyKnowledgeMapper, EmergencyKnowledge> implements EmergencyKnowledgeService {
    private final FileClient fileClient;
    private final FileRefClient fileRefClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EmergencyKnowledgeParam param) {
        EmergencyKnowledge emergencyKnowledge = ObjectUtils.copying(param, EmergencyKnowledge.class);
        emergencyKnowledge.setId(IdWorker.getId());
        this.baseMapper.insert(emergencyKnowledge);
        if (StringUtils.isNotBlank(param.getFileIds())) {
            FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
            fileRefCreateParam.setFileIdStr(param.getFileIds());
            fileRefCreateParam.setBusinessKey(emergencyKnowledge.getId());
            fileRefCreateParam.setSceneCode(EmergencyFileSceneCodeEnum.EMERGENCY_KNOWLEDGE.getCode());
            fileRefClient.refInduce(fileRefCreateParam);
        }
        return emergencyKnowledge.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EmergencyKnowledgeParam param) {
        EmergencyKnowledge emergencyKnowledge = this.baseMapper.selectById(id);
        Assert.isNotFound(emergencyKnowledge, "未查询到相关信息");
        if (!StringUtils.equals(emergencyKnowledge.getFileIds(), param.getFileIds())) {
            fileRefClient.removeByBusinessKeyAndScene(emergencyKnowledge.getId(), EmergencyFileSceneCodeEnum.EMERGENCY_KNOWLEDGE.getCode());
            if (StringUtils.isNotBlank(param.getFileIds())) {
                FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
                fileRefCreateParam.setFileIdStr(param.getFileIds());
                fileRefCreateParam.setBusinessKey(emergencyKnowledge.getId());
                fileRefCreateParam.setSceneCode(EmergencyFileSceneCodeEnum.EMERGENCY_KNOWLEDGE.getCode());
                fileRefClient.refInduce(fileRefCreateParam);
            }
        }
        ObjectUtils.copying(param, emergencyKnowledge);
        this.baseMapper.updateById(emergencyKnowledge);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public EmergencyKnowledgeVO getById(Long id) {
        EmergencyKnowledge emergencyKnowledge = this.baseMapper.selectById(id);
        return ObjectUtils.copying(emergencyKnowledge, EmergencyKnowledgeVO.class);
    }

    @Override
    public Pagination<EmergencyKnowledgeVO> getPage(EmergencyKnowledgeQueryParam param) {
        //请自行添加查询条件
        Page<EmergencyKnowledge> page = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getName()), EmergencyKnowledge::getName, param.getName())
                .like(StringUtils.isNotEmpty(param.getRemark()), EmergencyKnowledge::getRemark, param.getRemark())
                .page(PagingUtils.convert(param));
        Pagination<EmergencyKnowledgeVO> convert = PagingUtils.convert(page, EmergencyKnowledgeVO.class);
        if (!CollectionUtils.isEmpty(convert.getRecords())) {
            convert.getRecords().forEach(k -> {
                if (StringUtils.isNotEmpty(k.getFileIds())) {
                    List<Long> fileIds = Arrays.stream(k.getFileIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
                    List<FileVO> fileList = fileClient.getByIds(fileIds);
                    if (!CollectionUtils.isEmpty(fileList)) {
                        k.setFilePath(fileList.stream().map(FileVO::getPath).collect(Collectors.joining(",")));
                    }
                }
            });
        }
        return convert;
    }
}