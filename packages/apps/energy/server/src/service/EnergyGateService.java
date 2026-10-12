package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyGate;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyGateVO;

import java.util.List;

/**
 * 描述：能耗
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
public interface EnergyGateService extends IService<EnergyGate> {

    List<EnergyGateVO> getList();

    void updateOpenStatus(Long id, Boolean open);

    void updateOccupancyRate(String occupancyRate);

    String getOccupancyRate();

}