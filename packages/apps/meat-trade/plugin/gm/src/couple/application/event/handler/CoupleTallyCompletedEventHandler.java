package com.guhecloud.cube.gm.meattrade.plugin.couple.application.event.handler;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.gm.meattrade.plugin.couple.application.CoupleService;
import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.entity.Couple;
import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.enumeration.CoupleStatusEnum;
import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.valueobj.CoupleStatus;
import com.guhecloud.cube.gm.meattrade.plugin.tally.domain.event.TallyEvent;
import com.guhecloud.cube.gm.meattrade.plugin.tally.infrastructure.rocketmq.message.PorkArrangeMessage;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CoupleTallyCompletedEventHandler {

    private final CoupleService coupleService;

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void tallyCompletedUpdateCoupleStatus(final TallyEvent tallyEvent) {
        PorkArrangeMessage porkArrangeFinishedMessage = JsonUtils.deserialize(tallyEvent.getTallyContent(),
                                                                              PorkArrangeMessage.class);
        List<Couple> couples = new ArrayList<>();
        porkArrangeFinishedMessage.getPorkArrangeDetailMessages().stream().forEach(porkArrangeDetailMessage -> {
            Couple couple = new Couple(IdWorker.getId(), porkArrangeDetailMessage.getLabel(),
                                       new CoupleStatus(CoupleStatusEnum.NON_TRADE), porkArrangeFinishedMessage.getPorkArrangeTime());
            couples.add(couple);
        });
        this.coupleService.saveCouples(couples);
    }
}
