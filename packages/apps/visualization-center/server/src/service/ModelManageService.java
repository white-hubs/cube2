package com.guhecloud.cube.base.visualization.server.service;

import com.guhecloud.cube.base.visualization.server.param.ModelManagePageParam;
import com.guhecloud.cube.base.visualization.server.param.ModelManageParam;
import com.guhecloud.cube.base.visualization.server.vo.ModelManageVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-08-29  10:41
 * @Description:
 * @Version: 1.0
 */
public interface ModelManageService {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return 实例对象
     */
    ModelManageVO queryById(Long id);

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return
     */
    Pagination<ModelManageVO> queryPage(ModelManagePageParam param);

    List<ModelManageVO> queryAll(String modelType);

    /**
     * 新增数据
     *
     * @param modelManage 实例对象
     *
     * @return 实例对象
     */
    ModelManageVO insert(ModelManageParam modelManage);

    /**
     * 更新数据
     *
     * @param modelManage 实例对象
     *
     * @return 实例对象
     */
    Boolean update(ModelManageParam modelManage);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    Boolean deleteById(Long id);
}
