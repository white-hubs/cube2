package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyCollectPeriod;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyCollectPeriodBatchParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyCollectPeriodParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyCollectPeriodQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyCollectPeriodVO;
import com.guhecloud.platform.base.model.Pagination;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 描述：能耗采集时段定义服务层
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
public interface EnergyCollectPeriodService extends IService<EnergyCollectPeriod> {

    Long add(EnergyCollectPeriodParam param);

    void addBatch(EnergyCollectPeriodBatchParam batchParam);

    void update(Long id, EnergyCollectPeriodParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EnergyCollectPeriodVO getById(Long id);

    Pagination<EnergyCollectPeriodVO> getPage(EnergyCollectPeriodQueryParam param);

    String getPeriodFlagInFPG(String instrumentType, LocalDateTime dateTime);
}