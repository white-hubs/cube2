package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.FuelDischargeStandard;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.FuelSelectListVO;

import java.util.List;

/**
 * 燃料类型服务
 */
public interface FuelDischargeStandardService extends IService<FuelDischargeStandard> {
    List<FuelSelectListVO> getSelectList(String keyword);

    void insertDefaultFuel();
}
