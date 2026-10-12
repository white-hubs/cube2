package com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.command.FaultTypeCommand;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultType;

/**
 * @author lzy
 * @date 2022/5/3
 */
public interface FaultTypeCommandService extends IService<FaultType> {

    /**
     * 故障类型新增
     *
     * @param command 新增参数
     */
    void save(FaultTypeCommand command);

    /**
     * 故障类型编辑
     *
     * @param id      主键
     * @param command 编辑参数
     */
    void update(Long id, FaultTypeCommand command);

    /**
     * 故障类型删除
     *
     * @param id 主键
     */
    void remove(Long id);

}
