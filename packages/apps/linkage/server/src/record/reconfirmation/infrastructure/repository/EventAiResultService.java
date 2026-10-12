package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.record.domain.aggregates.EventRecord;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.enumeration.AiReconfirmResultEnum;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.po.EventAiResultPO;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.repository.mapper.EventAiResultMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventAiResultService extends ServiceImpl<EventAiResultMapper, EventAiResultPO> implements IService<EventAiResultPO> {

    /**
     * 新增事件ai分析结果
     *
     * @param eventRecord  事件记录
     * @param originResult 分析原始结果
     * @param result       分析结果
     */
    @Transactional(rollbackFor = Exception.class)
    public void addEventAiResult(EventRecord eventRecord, String originResult, AiReconfirmResultEnum result) {
        var eventAiResultPO = new EventAiResultPO();
        eventAiResultPO.setEventRecordId(eventRecord.getBaseInfo().getId());
        eventAiResultPO.setOriginResult(originResult);
        eventAiResultPO.setResult(result.name());
        this.save(eventAiResultPO);
    }

    public EventAiResultPO getEventAiResult(Long eventRecordId) {
        var eventAiResultPOS = this.baseMapper.selectList(Wrappers.<EventAiResultPO>lambdaQuery()
                                                                  .eq(EventAiResultPO::getEventRecordId, eventRecordId));
        if (CollectionUtils.isEmpty(eventAiResultPOS)) {
            return null;
        }
        return eventAiResultPOS.getFirst();
    }
}
