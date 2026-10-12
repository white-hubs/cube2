package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.command.HiddenDangerLevelCommand;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerLevel;

/**
 * @author lzy
 * @date 2022/4/24
 */
public interface HiddenDangerLevelCommandService extends IService<HiddenDangerLevel> {

    /**
     * 新增隐患程度
     *
     * @param command 新增参数
     */
    void save(HiddenDangerLevelCommand command);

    /**
     * 编辑隐患程度
     *
     * @param id      隐患程度主键
     * @param command 编辑参数
     */
    void update(Long id, HiddenDangerLevelCommand command);

    /**
     * 隐患程度删除
     *
     * @param id 隐患程度主键
     */
    void remove(Long id);
}
