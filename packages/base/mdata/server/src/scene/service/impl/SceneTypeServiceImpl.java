package com.guhecloud.cube.biz.mdata.server.scene.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.biz.mdata.server.scene.dao.SceneTypeMapper;
import com.guhecloud.cube.biz.mdata.server.scene.entity.SceneType;
import com.guhecloud.cube.biz.mdata.server.scene.param.SceneTypeModifyParam;
import com.guhecloud.cube.biz.mdata.server.scene.service.SceneTypeService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTreeEntity;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.guhecloud.cube.biz.mdata.server.constants.FileSceneConstants.BUSINESS_MENU;

/**
 * @author lzy
 * @date 2021/11/16
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SceneTypeServiceImpl extends ServiceImpl<SceneTypeMapper, SceneType> implements SceneTypeService {

    private final FileRefClient fileRefClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull SceneTypeModifyParam param) {
        SceneType sceneTypeByName = this.getByName(param.getProjectId(), param.getName());
        Assert.isConflict(sceneTypeByName != null, "存在场景类型信息!");
        SceneType sceneType = ObjectUtils.copying(param, SceneType.class);
        sceneType.setCode(this.getCode(param.getProjectId()));
        this.baseMapper.insert(sceneType);
        //关联图标附件
        if (Objects.nonNull(param.getFileId())) {
            this.fileRefClient.refInduceSingle(BUSINESS_MENU, sceneType.getId(), param.getFileId(), null);
        }
        return sceneType.getId();
    }

    @Override
    public void update(@NonNull Long id, @NonNull SceneTypeModifyParam param) {
        SceneType sceneType = this.getById(id);
        Assert.isNotFound(sceneType, "场景不存在");
        ObjectUtils.copying(param, sceneType);
        this.fileRefClient.removeByBusinessKeyAndScene(sceneType.getId(), BUSINESS_MENU);
        //关联图标附件
        if (Objects.nonNull(param.getFileId())) {
            this.fileRefClient.refInduceSingle(BUSINESS_MENU, sceneType.getId(), param.getFileId(), null);
        }
        this.baseMapper.updateById(sceneType);
    }

    @Override
    public SceneType getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<SceneType> list(@NonNull Long projectId) {
        return this.lambdaQuery()
                .eq(SceneType::getProjectId, projectId)
                .orderByAsc(SceneType::getSort).list();
    }

    @Override
    public List<SceneType> getByIds(Long projectId, @NonNull Collection<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.lambdaQuery().in(SceneType::getId, ids)
                       .eq(Objects.nonNull(projectId), SceneType::getProjectId, projectId)
                       .orderByAsc(SceneType::getSort)
                       .list();
    }

    @Override
    public List<SceneType> listByLevel(@NonNull Long projectId, @NonNull Integer level) {
        return this.lambdaQuery()
                .eq(SceneType::getProjectId, projectId)
                .eq(BasicEnhancedTreeEntity::getLevel, level)
                .orderByAsc(SceneType::getSort).list();
    }

    @Override
    public List<SceneType> getSequence(@NonNull Long projectId, @NonNull List<String> sequences) {
        return CollectionUtils.isEmpty(sequences) ? Collections.emptyList() : this.baseMapper.getBySequences(sequences, projectId);
    }

    private String getCode(Long projectId) {
        String code = String.valueOf(RedisUtils.generateIdentity());
        while (true) {
            SceneType sceneTypeByCode = this.getByCode(projectId, code);
            if (sceneTypeByCode == null) {
                break;
            }
            code = String.valueOf(RedisUtils.generateIdentity());
        }
        return code;
    }

    @Override
    public SceneType getByName(@NonNull Long projectId, @NonNull String name) {
        return this.lambdaQuery()
                .eq(SceneType::getProjectId, projectId)
                .eq(SceneType::getName, name).one();
    }

    @Override
    public SceneType getByCode(@NonNull Long projectId, @NonNull String code) {
        return this.lambdaQuery()
                .eq(SceneType::getProjectId, projectId)
                .eq(SceneType::getCode, code).one();
    }

    @Override
    public void remove(@NonNull Long id) {
        SceneType sceneType = this.getById(id);
        if (Objects.nonNull(sceneType)) {
            //查询子级
            long subCount = this.count(Wrappers.<SceneType>lambdaQuery().eq(SceneType::getParentId, id));
            Assert.isNotAcceptable(subCount != 0, "存在子级无法删除");
            DatabaseContextHolder.softDelete(this, sceneType);
        }
    }
}
