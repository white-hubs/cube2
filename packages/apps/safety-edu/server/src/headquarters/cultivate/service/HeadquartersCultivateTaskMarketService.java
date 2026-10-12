package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity.HeadquartersCultivateTaskMarket;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateTaskMarketParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateTaskMarketVO;

import java.util.List;
import java.util.Set;

public interface HeadquartersCultivateTaskMarketService extends IService<HeadquartersCultivateTaskMarket> {

    void add(HeadquartersCultivateTaskMarketParam headquartersCultivateTaskMarketParam);

    List<HeadquartersCultivateTaskMarketVO> getCultivateMarketsByCultivateId(Long headquartersCultivateTaskId);

    List<HeadquartersCultivateTaskMarketVO> getCultivateMarketsByCultivateIds(Set<Long> headquartersCultivateTaskIds);
}
