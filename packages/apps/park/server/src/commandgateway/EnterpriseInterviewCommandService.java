package com.guhecloud.cube.srv.gen.park.management.server.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.EnterpriseInterviewParam;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterpriseInterviewPO;

public interface EnterpriseInterviewCommandService extends IService<EnterpriseInterviewPO> {

    void save(EnterpriseInterviewParam param);

    void update(Long id, EnterpriseInterviewParam param);

    void remove(Long id);

    void removeBatch(String ids);
}
