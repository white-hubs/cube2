package com.guhecloud.cube.base.alarm.server.riskscore.init;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.guhecloud.cube.base.alarm.server.riskscore.po.WeightAllotPO;
import com.guhecloud.cube.base.alarm.server.riskscore.service.MarketWeightAllotService;
import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.RiskScoreCache;
import com.guhecloud.cube.srv.gen.riskscore.sdk.cache.dto.WeightAllotDTO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.event.src.annotation.EventBusListen;
import com.guhecloud.platform.event.src.common.SysReadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EventBusListen(order = 1)
public class WeightAllotInit {

    @Autowired
    private RiskScoreCache riskScoreCache;

    @Autowired
    private MarketWeightAllotService marketWeightAllotService;

    @Subscribe
    @AllowConcurrentEvents
    public void loadWeightAllotConfigCache(SysReadEvent event) throws Exception {
        try {
            List<WeightAllotPO> weightAllotPOS = this.marketWeightAllotService.list();
            if (CollectionUtils.isEmpty(weightAllotPOS)) {
                return;
            }
            this.riskScoreCache.setWeightAllotConfigs(ObjectUtils.copying(weightAllotPOS, WeightAllotDTO.class));
        } catch (Exception e) {
            log.error("初始化权重配置到Redis失败,失败异常原因:", e);
        }
    }
}
