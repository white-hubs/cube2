package com.guhecloud.cube.gm.meattrade.plugin.tally.application;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.gm.meattrade.plugin.tally.domain.enumeration.TallyPushStatusEnum;
import com.guhecloud.cube.gm.meattrade.plugin.tally.domain.event.TallyEvent;
import com.guhecloud.cube.gm.meattrade.plugin.tally.domain.po.TallyPushPO;
import com.guhecloud.cube.gm.meattrade.plugin.tally.infrastructure.repository.mapper.TallyPushMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TallyPushService extends ServiceImpl<TallyPushMapper, TallyPushPO> {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional(rollbackFor = Exception.class)
    public Long save(String tallyNo, String pushContent, TallyPushStatusEnum pushStatus) {
        TallyPushPO tallyPushPO = TallyPushPO
                .builder()
                .tallyNo(tallyNo)
                .pushContent(pushContent)
                .pushStatus(pushStatus)
                .build();
        this.baseMapper.insert(tallyPushPO);
        //发送事件
        this.applicationEventPublisher.publishEvent(new TallyEvent(this, pushContent));
        return tallyPushPO.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, TallyPushStatusEnum tallyPushStatus) {
        LambdaUpdateWrapper<TallyPushPO> updateWrapper = Wrappers.<TallyPushPO>lambdaUpdate()
                .set(TallyPushPO::getPushStatus, tallyPushStatus)
                .eq(TallyPushPO::getId, id);
        this.update(updateWrapper);
    }

    public List<TallyPushPO> getNoTallyPushed(Integer size) {
        return this.baseMapper.selectPage(new Page<>(0, size), Wrappers.<TallyPushPO>lambdaQuery()
                        .ne(TallyPushPO::getPushStatus, TallyPushStatusEnum.PUSHED))
                .getRecords();
    }
}
