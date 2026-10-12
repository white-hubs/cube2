package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.service;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.command.BlacklistConfigCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.BlacklistConfigVO;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 16:44
 * @Description:
 */
public interface BlacklistConfigService {
    Boolean saveOrUpdate(BlacklistConfigCommand param);

    BlacklistConfigVO getConfig();

}
