package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDeviceChangeLog;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterDeviceChangeLogParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterDeviceChangeLogQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterDeviceChangeLogVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：仪表设备更换记录服务层
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
public interface MeterDeviceChangeLogService extends IService<MeterDeviceChangeLog> {

    Long add(MeterDeviceChangeLogParam param);

    void update(Long id, MeterDeviceChangeLogParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    MeterDeviceChangeLogVO getById(Long id);

    Pagination<MeterDeviceChangeLogVO> getPage(MeterDeviceChangeLogQueryParam param);

}