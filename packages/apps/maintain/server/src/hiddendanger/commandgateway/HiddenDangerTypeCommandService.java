package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.command.HiddenDangerTypeCommand;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerType;

/**
 * @author lzy
 * @date 2022/4/26
 */
public interface HiddenDangerTypeCommandService extends IService<HiddenDangerType> {

    /**
     * 隐患类别新增
     *
     * @param command 新增参数
     */
    void save(HiddenDangerTypeCommand command);

    /**
     * 隐患类别编辑
     *
     * @param id      隐患类别参数
     * @param command 编辑参数
     */
    void update(Long id, HiddenDangerTypeCommand command);

    /**
     * 隐患类别删除
     *
     * @param id 隐患类别主键
     */
    void remove(Long id);
}
