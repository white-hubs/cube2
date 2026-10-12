package com.guhecloud.cube.biz.meattrade.server.pork.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeCheat;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeCheatModifyParam;

public interface PorkArrangeCheatService extends IService<PorkArrangeCheat> {

    Long save(PorkArrangeCheatModifyParam param);
}
