package com.guhecloud.cube.sn.meattrade.plugin.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.sn.meattrade.plugin.common.entity.PorkArrange;
import com.guhecloud.cube.sn.meattrade.plugin.common.entity.PorkArrangeDetail;
import com.guhecloud.cube.sn.meattrade.plugin.common.enumeration.PorkArrangePushDataStatusEnum;
import com.guhecloud.cube.sn.meattrade.plugin.common.mapper.PorkArrangePushDataMapper;
import com.guhecloud.cube.sn.meattrade.plugin.common.param.PorkArrangeQueryParam;
import com.guhecloud.cube.sn.meattrade.plugin.common.po.PorkArrangePushDataPO;
import com.guhecloud.cube.sn.meattrade.plugin.common.service.PorkArrangePushDataService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PorkArrangePushDataServiceImpl extends ServiceImpl<PorkArrangePushDataMapper, PorkArrangePushDataPO> implements PorkArrangePushDataService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(PorkArrange porkArrange) {
        PorkArrangePushDataPO porkArrangePushDataPO = convertPorkArrangeToPushData(porkArrange);
        this.baseMapper.insert(porkArrangePushDataPO);
        return porkArrangePushDataPO.getId();
    }

    @Override
    public List<PorkArrange> getPushFailPorkArranges(LocalDateTime maxRetryTime, Integer max, LocalDateTime retryTime) {
        Page<PorkArrangePushDataPO> porkArrangePushDataPOPage = this.baseMapper.selectPage(new Page<>(1, max), Wrappers
                .<PorkArrangePushDataPO>lambdaQuery()
                .ge(PorkArrangePushDataPO::getCreateTime, maxRetryTime)
                .eq(PorkArrangePushDataPO::getStatus, PorkArrangePushDataStatusEnum.PUSH_FAIL)
                .le(PorkArrangePushDataPO::getNextRetryTime, retryTime)
                .orderByAsc(PorkArrangePushDataPO::getEventType));
        if (CollectionUtils.isEmpty(porkArrangePushDataPOPage.getRecords())) {
            return Collections.emptyList();
        }
        return convertPushDataToPorkArranges(porkArrangePushDataPOPage.getRecords());
    }

    @Override
    public List<PorkArrange> getPushPorkArranges(Integer max) {
        Page<PorkArrangePushDataPO> porkArrangePushDataPOPage = this.baseMapper.selectPage(new Page<>(1, max), Wrappers
                .<PorkArrangePushDataPO>lambdaQuery()
                .eq(PorkArrangePushDataPO::getStatus, PorkArrangePushDataStatusEnum.UN_PUSH)
                .orderByAsc(PorkArrangePushDataPO::getCreateTime)
                .orderByAsc(PorkArrangePushDataPO::getEventType));
        if (CollectionUtils.isEmpty(porkArrangePushDataPOPage.getRecords())) {
            return Collections.emptyList();
        }
        return convertPushDataToPorkArranges(porkArrangePushDataPOPage.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePushStatusByIds(Collection<Long> ids, PorkArrangePushDataStatusEnum status) {
        LambdaUpdateWrapper<PorkArrangePushDataPO> updateWrapper = Wrappers.<PorkArrangePushDataPO>lambdaUpdate()
                .in(PorkArrangePushDataPO::getId, ids)
                .set(PorkArrangePushDataPO::getStatus, status);
        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePushStatusByTallIds(Collection<String> ids, PorkArrangePushDataStatusEnum status, Integer pushTimes) {
        LambdaUpdateWrapper<PorkArrangePushDataPO> updateWrapper = Wrappers.<PorkArrangePushDataPO>lambdaUpdate()
                .in(PorkArrangePushDataPO::getTallyId, ids)
                .set(Objects.nonNull(pushTimes), PorkArrangePushDataPO::getPushTimes, pushTimes)
                .set(PorkArrangePushDataPO::getStatus, status);
        this.update(updateWrapper);
    }

    @Override
    public Long getPreEventNotPushSuccessCount(String guid, Long arrangeReportId, Integer eventType) {
        return this.baseMapper.selectCount(Wrappers.<PorkArrangePushDataPO>lambdaQuery()
                                                   .eq(PorkArrangePushDataPO::getPorkArrangeId, arrangeReportId)
                                                   .lt(PorkArrangePushDataPO::getEventType, eventType)
                                                   .lt(PorkArrangePushDataPO::getPushTimes, 10)
                                                   .ne(PorkArrangePushDataPO::getGuid, guid)
                                                   .ne(PorkArrangePushDataPO::getStatus, PorkArrangePushDataStatusEnum.PUSH_SUCCESS)
                                          );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePushStatusAndFailTimes(Long arrangeReportId, String guid, PorkArrangePushDataStatusEnum status, Integer pushTimes,
                                             LocalDateTime nextRetryTime
                                            ) {
        LambdaUpdateWrapper<PorkArrangePushDataPO> updateWrapper = Wrappers.<PorkArrangePushDataPO>lambdaUpdate()
                .eq(PorkArrangePushDataPO::getPorkArrangeId, arrangeReportId)
                .eq(PorkArrangePushDataPO::getGuid, guid)
                .set(PorkArrangePushDataPO::getUpdateTime, LocalDateTime.now())
                .set(PorkArrangePushDataPO::getStatus, status)
                .set(PorkArrangePushDataPO::getPushTimes, pushTimes)
                .set(PorkArrangePushDataPO::getNextRetryTime, nextRetryTime);
        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetInProcessingStatus() {
        LambdaUpdateWrapper<PorkArrangePushDataPO> updateWrapper = Wrappers.<PorkArrangePushDataPO>lambdaUpdate()
                .set(PorkArrangePushDataPO::getUpdateTime, LocalDateTime.now())
                .set(PorkArrangePushDataPO::getStatus, PorkArrangePushDataStatusEnum.UN_PUSH)
                .eq(PorkArrangePushDataPO::getStatus, PorkArrangePushDataStatusEnum.PUSH_PROCESS);
        this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletedExpirePush(LocalDateTime expireTime) {
        this.baseMapper.delete(Wrappers.<PorkArrangePushDataPO>lambdaQuery()
                                       .lt(PorkArrangePushDataPO::getCreateTime, expireTime)
                                       .eq(PorkArrangePushDataPO::getStatus, PorkArrangePushDataStatusEnum.PUSH_SUCCESS));
    }

    @Override
    public Set<String> getTallIds(PorkArrangeQueryParam param) {
        return this.baseMapper.selectTallIds(param);
    }

    @Override
    public Set<String> getFailPushTallIds(Collection<String> tallIds) {
        return this.baseMapper.selectFailPushTallIds(tallIds);
    }

    @Override
    public Set<String> getStayPushTallIds(Collection<String> tallIds) {
        return this.baseMapper.selectStayPushTallIds(tallIds);
    }

    private PorkArrangePushDataPO convertPorkArrangeToPushData(PorkArrange porkArrange) {
        return PorkArrangePushDataPO.builder().porkArrangeId(porkArrange.getId()).guid(porkArrange.getGuid())
                .deviceId(porkArrange.getDeviceId()).tallyId(porkArrange.getTallyId()).eventType(porkArrange.getEventType())
                .startTime(porkArrange.getStartTime()).endTime(porkArrange.getEndTime())
                .arrangeDetails(JsonUtils.serialize(porkArrange.getArrangeDetails()))
                .status(PorkArrangePushDataStatusEnum.UN_PUSH).pushTimes(0).build();
    }

    private List<PorkArrange> convertPushDataToPorkArranges(List<PorkArrangePushDataPO> porkArrangePushDataPOS) {
        return porkArrangePushDataPOS.stream().map(this::convertPushDataToPorkArrange).collect(Collectors.toList());
    }

    private PorkArrange convertPushDataToPorkArrange(PorkArrangePushDataPO porkArrangePushDataPO) {
        return PorkArrange.builder()
                .id(porkArrangePushDataPO.getPorkArrangeId())
                .recordId(porkArrangePushDataPO.getId())
                .guid(porkArrangePushDataPO.getGuid())
                .deviceId(porkArrangePushDataPO.getDeviceId()).tallyId(porkArrangePushDataPO.getTallyId())
                .eventType(porkArrangePushDataPO.getEventType()).startTime(porkArrangePushDataPO.getStartTime())
                .endTime(porkArrangePushDataPO.getEndTime()).pushTimes(porkArrangePushDataPO.getPushTimes())
                .status(porkArrangePushDataPO.getStatus())
                .tenant(porkArrangePushDataPO.getTenant())
                .pushTimes(porkArrangePushDataPO.getPushTimes())
                .nextRetryTime(porkArrangePushDataPO.getNextRetryTime())
                .arrangeDetails(JsonUtils.deserialize(porkArrangePushDataPO.getArrangeDetails(), new TypeReference<List<PorkArrangeDetail>>() {}))
                .build();
    }
}
