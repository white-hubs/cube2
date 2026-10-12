package com.guhecloud.cube.base.alarm.server.riskscore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.riskscore.dao.MarketWeightAllotMapper;
import com.guhecloud.cube.base.alarm.server.riskscore.po.WeightAllotPO;
import com.guhecloud.cube.base.alarm.server.riskscore.service.MarketWeightAllotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketWeightAllotServiceImpl extends ServiceImpl<MarketWeightAllotMapper, WeightAllotPO> implements MarketWeightAllotService {
}
