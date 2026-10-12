package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.TerminalApplicationMapper;
import com.guhecloud.cube.biz.ouaa.server.dao.TerminalMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.entity.TerminalApplication;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalApplicationService;
import com.guhecloud.cube.biz.ouaa.server.vo.BaseInfo;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TerminalApplicationServiceImpl extends ServiceImpl<TerminalApplicationMapper, TerminalApplication> implements TerminalApplicationService {
    private final TerminalMapper terminalMapper;

    @Override
    public Long save(@NonNull Long terminalId, @NonNull Long applicationId) {
        TerminalApplication saved = this.baseMapper.selectOne(Wrappers.<TerminalApplication>lambdaQuery()
                                                                      .eq(TerminalApplication::getTerminalId, terminalId)
                                                                      .eq(TerminalApplication::getApplicationId, applicationId));
        if (Objects.isNull(saved)) {
            saved = TerminalApplication.builder().terminalId(terminalId).applicationId(applicationId).build();
            this.baseMapper.insert(saved);
        }
        return saved.getId();
    }

    @Override
    public void savePath(@NonNull Long terminalId, @NonNull Long applicationId, @NonNull String path) {
        LambdaUpdateWrapper<TerminalApplication> updateWrapper =
                Wrappers.<TerminalApplication>lambdaUpdate()
                        .eq(TerminalApplication::getTerminalId, terminalId)
                        .eq(TerminalApplication::getApplicationId, applicationId)
                        .set(TerminalApplication::getPath, path);
        this.update(updateWrapper);
    }

    @Override
    public void remove(@NonNull Long terminalId, @NonNull Long applicationId) {
        this.baseMapper.delete(Wrappers.<TerminalApplication>lambdaQuery().eq(TerminalApplication::getTerminalId, terminalId)
                                       .eq(TerminalApplication::getApplicationId, applicationId));
    }

    @Override
    public void removeByTerminal(@NonNull Long terminalId) {
        this.baseMapper.delete(Wrappers.<TerminalApplication>lambdaQuery().eq(TerminalApplication::getTerminalId, terminalId));
    }

    @Override
    public void removeByApplication(@NonNull Long applicationId) {
        this.baseMapper.delete(Wrappers.<TerminalApplication>lambdaQuery().eq(TerminalApplication::getApplicationId, applicationId));
    }

    @Override
    public List<TerminalApplication> listByTerminal(Long terminalId) {
        return this.baseMapper.selectList(Wrappers.<TerminalApplication>lambdaQuery().eq(TerminalApplication::getTerminalId, terminalId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateBatch(Long id, List<Long> terminalIds) {
        if (Objects.isNull(terminalIds)) {
            return;
        }
        if (CollectionUtils.isEmpty(terminalIds)) {
            this.removeByApplication(id);
            return;
        }
        List<TerminalApplication> existTerminalApplications = this.lambdaQuery().eq(TerminalApplication::getApplicationId, id).list();
        Set<Long> removeIds = existTerminalApplications.stream()
                .filter(terminalApplication -> !terminalIds.contains(terminalApplication.getTerminalId()))
                .map(TerminalApplication::getId).collect(Collectors.toSet());
        //移除不存在传入的终端
        this.removeBatchByIds(removeIds);
        Set<Long> existTerminalIds = existTerminalApplications.stream()
                .filter(terminalApplication -> terminalIds.contains(terminalApplication.getTerminalId()))
                .map(TerminalApplication::getTerminalId).collect(Collectors.toSet());
        terminalIds.removeAll(existTerminalIds);
        if (CollectionUtils.isEmpty(terminalIds)) {
            return;
        }
        List<TerminalApplication> terminalApplications = terminalIds.stream()
                .map(terminalId -> TerminalApplication.builder()
                        .id(IdWorker.getId())
                        .terminalId(terminalId)
                        .applicationId(id).build())
                .collect(Collectors.toList());
        this.saveBatch(terminalApplications);
    }

    @Override
    public List<BaseInfo> getTerminalList(Long id) {
        List<TerminalApplication> terminalApplications = this.lambdaQuery()
                .select(TerminalApplication::getTerminalId)
                .eq(TerminalApplication::getApplicationId, id)
                .list();
        if (CollectionUtils.isEmpty(terminalApplications)) {
            return Collections.emptyList();
        }
        return Optional.ofNullable(terminalMapper.selectList(Wrappers.<Terminal>lambdaQuery()
                                                                     .in(Terminal::getId, terminalApplications
                                                                             .stream()
                                                                             .map(TerminalApplication::getTerminalId)
                                                                             .collect(Collectors.toSet())
                                                                        )))
                .orElse(new ArrayList<>())
                .stream().map(t -> ObjectUtils.copying(t, BaseInfo.class))
                .collect(Collectors.toList());
    }

}