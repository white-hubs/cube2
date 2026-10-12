package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.service;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.command.IllegalParkingConfigCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.enumeration.IllegalLevelEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.IllegalParkingConfigVO;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 17:01
 * @Description: 违停配置应用服务接口
 */
public interface IllegalParkingConfigService {
    Boolean saveOrUpdate(List<IllegalParkingConfigCommand> param);

    /**
     * 根据ID获取违停配置
     *
     * @return 违停配置对象
     */
    List<IllegalParkingConfigVO> getConfig();

    /**
     * 查询执行违停程度的扣分细则
     */
    IllegalParkingConfigVO getIllegalParkingConfig(IllegalLevelEnum illegalLevelEnum);

    String getIllegalParkingConfigTimeCycle();
}