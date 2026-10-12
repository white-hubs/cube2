package com.guhecloud.cube.biz.meattrade.server.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.order.entity.SettlementConfig;
import com.guhecloud.cube.biz.meattrade.server.order.param.SettlementConfigCreateParam;
import com.guhecloud.cube.biz.meattrade.server.order.param.SettlementConfigUpdateParam;

import java.util.List;

public interface SettlementConfigService extends IService<SettlementConfig> {

    void save(List<SettlementConfigCreateParam> param);

    void update(List<SettlementConfigUpdateParam> param);

    void remove(Long id);

    List<SettlementConfig> listActiveSettlementConfig(Integer tenant);

}
