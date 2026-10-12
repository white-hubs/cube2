package com.guhecloud.cube.base.message2.server.sat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.message2.server.sat.entity.SatUserMsgIgnoredConfig;
import com.guhecloud.cube.base.message2.server.sat.param.SatUserMsgIgnoredConfigParam;

public interface SatUserMsgIgnoredConfigService extends IService<SatUserMsgIgnoredConfig> {

    Long save(Long userId, SatUserMsgIgnoredConfigParam param);

    SatUserMsgIgnoredConfig getSatUserMsgIgnoredConfig(Long userId);

    void updateEnabled(Long userId, Boolean enabled);

    void remove(Long userId);
}
