package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyCollectConfig;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyCollectConfigParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyCollectConfigQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyCollectConfigVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;

/**
 * 描述：能耗采集配置服务层
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
public interface EnergyCollectConfigService extends IService<EnergyCollectConfig> {

    Long add(EnergyCollectConfigParam param);

    void update(Long id, EnergyCollectConfigParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EnergyCollectConfigVO getById(Long id);

    Pagination<EnergyCollectConfigVO> getPage(EnergyCollectConfigQueryParam param);

    /**
     * key值为仪表的类型
     *
     * @return
     */
    Map<String, EnergyCollectConfig> getCategoryMap();

    List<EnergyCollectConfigVO> getList(EnergyCollectConfigQueryParam param);
}