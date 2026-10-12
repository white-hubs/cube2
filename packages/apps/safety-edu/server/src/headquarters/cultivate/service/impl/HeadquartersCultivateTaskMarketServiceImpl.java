package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.dao.HeadquartersCultivateTaskMarketMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity.HeadquartersCultivateTaskMarket;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateTaskMarketParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service.HeadquartersCultivateTaskMarketService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateTaskMarketVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class HeadquartersCultivateTaskMarketServiceImpl extends ServiceImpl<HeadquartersCultivateTaskMarketMapper, HeadquartersCultivateTaskMarket> implements HeadquartersCultivateTaskMarketService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(HeadquartersCultivateTaskMarketParam headquartersCultivateTaskMarketParam) {
        HeadquartersCultivateTaskMarket headquartersCultivateTaskMarket = ObjectUtils.copying(headquartersCultivateTaskMarketParam, HeadquartersCultivateTaskMarket.class);
        this.baseMapper.insert(headquartersCultivateTaskMarket);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HeadquartersCultivateTaskMarketVO> getCultivateMarketsByCultivateId(Long headquartersCultivateTaskId) {
        return getCultivateMarketsByCultivateIds(Collections.singleton(headquartersCultivateTaskId));
    }

    @Override
    public List<HeadquartersCultivateTaskMarketVO> getCultivateMarketsByCultivateIds(Set<Long> headquartersCultivateTaskIds) {
        if (CollectionUtils.isEmpty(headquartersCultivateTaskIds)) {
            return Collections.emptyList();
        }
        List<HeadquartersCultivateTaskMarket> headquartersCultivateTaskMarkets = this.baseMapper.selectList(Wrappers
                                                                                                                    .<HeadquartersCultivateTaskMarket>lambdaQuery()
                                                                                                                    .in(HeadquartersCultivateTaskMarket::getHeadquartersCultivateTaskId, headquartersCultivateTaskIds));
        return ObjectUtils.copying(headquartersCultivateTaskMarkets, HeadquartersCultivateTaskMarketVO.class);
    }
}
