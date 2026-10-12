package com.guhecloud.cube.srv.gen.safety.management.education.init;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.srv.gen.safety.management.education.application.EducationMarketTaskUrlService;
import com.guhecloud.cube.srv.gen.safety.management.education.constant.EducationTaskTypeConstants;
import com.guhecloud.cube.srv.gen.safety.management.education.po.EducationMarketTaskUrlPO;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@EventBusListen(order = 1)
public class EducationUrlInitRunner {

    @Autowired
    private MarketConfigService marketConfigService;
    @Autowired
    private EducationMarketTaskUrlService educationMarketTaskUrlService;

    @Subscribe
    @AllowConcurrentEvents
    public void initMarketEducationTaskUrl(SysReadEvent event) throws Exception {
        List<MarketConfigPO> marketConfigPOS = this.marketConfigService.list();
        marketConfigPOS.stream().forEach(marketConfigPO -> {
            SessionContextHolder.requestTenant(marketConfigPO.getTenant());
            EducationMarketTaskUrlPO educationMarketExamTaskUrlPO = this.educationMarketTaskUrlService.getOne(Wrappers
                                                                                                                      .<EducationMarketTaskUrlPO>lambdaQuery()
                                                                                                                      .eq(EducationMarketTaskUrlPO::getMarketTenant, marketConfigPO.getMarketTenant())
                                                                                                                      .eq(EducationMarketTaskUrlPO::getTaskType, EducationTaskTypeConstants.EXAM_TASK)
                                                                                                                      .last("limit 1"));
            if (Objects.isNull(educationMarketExamTaskUrlPO)) {
                educationMarketExamTaskUrlPO = new EducationMarketTaskUrlPO();
                educationMarketExamTaskUrlPO.setMarketId(marketConfigPO.getId());
                educationMarketExamTaskUrlPO.setMarketTenant(marketConfigPO.getMarketTenant());
                educationMarketExamTaskUrlPO.setEnabled(true);
                educationMarketExamTaskUrlPO.setTaskType(EducationTaskTypeConstants.EXAM_TASK);
                this.educationMarketTaskUrlService.save(educationMarketExamTaskUrlPO);
            }
            EducationMarketTaskUrlPO educationMarketCultivateTaskUrlPO = this.educationMarketTaskUrlService.getOne(Wrappers
                                                                                                                           .<EducationMarketTaskUrlPO>lambdaQuery()
                                                                                                                           .eq(EducationMarketTaskUrlPO::getMarketTenant, marketConfigPO.getMarketTenant())
                                                                                                                           .eq(EducationMarketTaskUrlPO::getTaskType, EducationTaskTypeConstants.CULTIVATE_TASK)
                                                                                                                           .last("limit 1"));
            if (Objects.isNull(educationMarketCultivateTaskUrlPO)) {
                educationMarketCultivateTaskUrlPO = new EducationMarketTaskUrlPO();
                educationMarketCultivateTaskUrlPO.setMarketId(marketConfigPO.getId());
                educationMarketCultivateTaskUrlPO.setMarketTenant(marketConfigPO.getMarketTenant());
                educationMarketCultivateTaskUrlPO.setEnabled(true);
                educationMarketCultivateTaskUrlPO.setTaskType(EducationTaskTypeConstants.CULTIVATE_TASK);
                this.educationMarketTaskUrlService.save(educationMarketCultivateTaskUrlPO);
            }
            SessionContextHolder.clearSessionContext();
        });
    }
}
