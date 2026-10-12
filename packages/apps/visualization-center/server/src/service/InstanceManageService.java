package com.guhecloud.cube.base.visualization.server.service;

import com.guhecloud.cube.base.visualization.server.param.InstanceManagePageParam;
import com.guhecloud.cube.base.visualization.server.param.InstanceManageParam;
import com.guhecloud.cube.base.visualization.server.vo.InstanceManageVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 发布实例服务接口
 *
 * @author : lijie
 * @date : 2023-8-21
 */
public interface InstanceManageService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return 实例对象
     */
    InstanceManageVO queryById(Long id);

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return
     */
    Pagination<InstanceManageVO> queryInstancePage(InstanceManagePageParam param);

    /**
     * 新增数据
     *
     * @param param 实例对象
     *
     * @return 实例对象
     */
    Boolean insert(InstanceManageParam param);

    /**
     * 更新数据
     *
     * @param param 实例对象
     *
     * @return 实例对象
     */
    Boolean update(InstanceManageParam param);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    Boolean deleteByProjectId(Long id);
}
