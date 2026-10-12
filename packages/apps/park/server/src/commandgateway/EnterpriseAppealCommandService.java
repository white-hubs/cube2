package com.guhecloud.cube.srv.gen.park.management.server.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.EnterpriseAppealParam;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterpriseAppealPO;

public interface EnterpriseAppealCommandService extends IService<EnterpriseAppealPO> {

    void save(EnterpriseAppealParam param);

    void update(Long id, EnterpriseAppealParam param);

    void remove(Long id);

    void removeBatch(String ids);
}
