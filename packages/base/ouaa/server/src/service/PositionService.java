package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.Position;
import com.guhecloud.cube.biz.ouaa.server.param.PosistionQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.PositionModifyParam;

import java.util.Collection;
import java.util.List;

/**
 * 岗位业务操作接口
 *
 * @author Woody
 * @date 2021/7/2
 */
public interface PositionService extends IService<Position> {
    /**
     * 删除岗位并返回是否删除成功
     *
     * @param id 岗位主键
     *
     * @return true/false
     */
    boolean deletePosition(Long id);

    /**
     * 新增岗位
     *
     * @param param 新增参数
     *
     * @return 岗位实例
     */
    Position addPosition(PositionModifyParam param);

    /**
     * 修改岗位
     *
     * @param id    岗位主键
     * @param param 修改参数
     *
     * @return 岗位实例
     */
    Position updatePosition(Long id, PositionModifyParam param);

    /**
     * 根据主键获取岗位列表
     *
     * @param ids 岗位主键集合
     *
     * @return 岗位列表
     */
    List<Position> listPositions(Collection<Long> ids);

    /**
     * 查询岗位
     *
     * @param param 查询参数
     *
     * @return 岗位列表分页
     */
    Page<Position> listPosistions(PosistionQueryParam param);

    /**
     * 岗位列表
     *
     * @param param
     *
     * @return
     */
    List<Position> list(PosistionQueryParam param);
}
