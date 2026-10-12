package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.CollectConfigure;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param.CollectConfigureParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.CollectConfigureVo;

public interface CollectConfigureService extends IService<CollectConfigure> {

    void addCollectConfigure(CollectConfigureParam param);

    CollectConfigureVo getCollectConfigure(String applicationCode);
}
