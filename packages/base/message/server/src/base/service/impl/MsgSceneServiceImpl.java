package com.guhecloud.cube.base.message2.server.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message2.server.base.dao.MsgSceneMapper;
import com.guhecloud.cube.base.message2.server.base.entity.MsgScene;
import com.guhecloud.cube.base.message2.server.base.param.MsgSceneModifyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgSceneQueryParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgSceneUpdateParam;
import com.guhecloud.cube.base.message2.server.base.service.MsgSceneService;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MsgSceneServiceImpl extends ServiceImpl<MsgSceneMapper, MsgScene> implements MsgSceneService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull MsgSceneModifyParam param) {
        MsgScene msgScene = ObjectUtils.copying(param, MsgScene.class);
        this.baseMapper.insert(msgScene);
        return msgScene.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull MsgSceneUpdateParam param) {
        MsgScene msgScene = this.getById(id);
        if (Objects.nonNull(msgScene)) {
            ObjectUtils.copying(param, msgScene);
            this.baseMapper.updateById(msgScene);
        }
    }

    @Override
    public MsgScene getById(@NonNull Long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public MsgScene getByCode(@NonNull String code) {
        return this.baseMapper.selectOne(Wrappers.<MsgScene>lambdaQuery().eq(MsgScene::getCode, code));
    }

    @Override
    public Page<MsgScene> msgScenePage(MsgSceneQueryParam param) {
        return this.baseMapper.selectPage(PagingUtils.convert(param),
                                          Wrappers.<MsgScene>lambdaQuery()
                                                  .eq(StringUtils.isNotEmpty(param.getApplicationCode()), MsgScene::getApplicationCode, param.getApplicationCode())
                                                  .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                                                          .like(MsgScene::getCode, param.getKeyword())
                                                          .or()
                                                          .like(MsgScene::getName, param.getKeyword()))
                                                  .orderByDesc(MsgScene::getCreateTime)
                                         );
    }

    @Override
    public List<MsgScene> msgScenes(MsgSceneQueryParam param) {
        return this.baseMapper.selectList(Wrappers.<MsgScene>lambdaQuery()
                                                  .eq(StringUtils.isNotEmpty(param.getApplicationCode()), MsgScene::getApplicationCode, param.getApplicationCode())
                                                  .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                                                          .like(MsgScene::getCode, param.getKeyword())
                                                          .or()
                                                          .like(MsgScene::getName, param.getKeyword())));
    }

    @Override
    public List<MsgScene> listMsgScenesByApplicationCodes(@NonNull Collection<String> codes) {
        return CollectionUtils.isEmpty(codes) ? Collections.emptyList() : this.baseMapper.selectList(Wrappers.<MsgScene>lambdaQuery()
                                                                                                             .in(MsgScene::getApplicationCode, codes));
    }

    @Override
    public List<MsgScene> listMsgScenesByCodes(@NonNull Collection<String> codes) {
        return CollectionUtils.isEmpty(codes) ? Collections.emptyList()
                                              : this.baseMapper.selectList(Wrappers.<MsgScene>lambdaQuery().in(MsgScene::getCode, codes));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
