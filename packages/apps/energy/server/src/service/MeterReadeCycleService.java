package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterReadeCycle;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterReadeCycleParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.MeterReadeCycleQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterReadeCycleVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：抄表周期服务层
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
public interface MeterReadeCycleService extends IService<MeterReadeCycle> {

    Long add(MeterReadeCycleParam param);

    void update(Long id, MeterReadeCycleParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    MeterReadeCycleVO getById(Long id);

    Pagination<MeterReadeCycleVO> getPage(MeterReadeCycleQueryParam param);

    void addOrUpdate(MeterReadeCycleParam param);
}