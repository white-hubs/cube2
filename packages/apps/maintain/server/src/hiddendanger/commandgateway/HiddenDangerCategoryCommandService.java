package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.command.HiddenDangerCategoryCommand;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerCategory;

/**
 * @author lzy
 * @date 2022/4/26
 */
public interface HiddenDangerCategoryCommandService extends IService<HiddenDangerCategory> {

    /**
     * 隐患分类新增
     *
     * @param command 新增参数
     */
    void save(HiddenDangerCategoryCommand command);

    /**
     * 隐患分类编辑
     *
     * @param id      主键
     * @param command 编辑参数
     */
    void update(Long id, HiddenDangerCategoryCommand command);

    /**
     * 隐患分类删除
     *
     * @param id 隐患分类主键
     */
    void remove(Long id);

}
