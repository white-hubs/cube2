package com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.command.FaultCaseCommand;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultCase;

/**
 * @author lzy
 * @date 2022/5/6
 */
public interface FaultCaseCommandService extends IService<FaultCase> {

    /**
     * 故障案例新增
     *
     * @param command 新增参数
     */
    void save(FaultCaseCommand command);

    /**
     * 故障案例编辑
     *
     * @param id      主键
     * @param command 编辑参数
     */
    void update(Long id, FaultCaseCommand command);

    /**
     * 故障案例删除
     *
     * @param id 主键
     */
    void remove(Long id);

}
