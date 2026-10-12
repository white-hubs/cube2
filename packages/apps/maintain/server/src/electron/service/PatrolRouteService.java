package com.guhecloud.cube.srv.gen.maintain.server.electron.service;//package com.guhecloud.cube.srv.gen.maintain.electron.server.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolRoute;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolRoutePageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolRouteParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolRouteVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Set;

public interface PatrolRouteService extends IService<PatrolRoute> {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return nfc信息
     */
    PatrolRouteVO queryById(Long id);

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 分页数据
     */
    Pagination<PatrolRouteVO> queryRoutePage(PatrolRoutePageParam param);

    /**
     * 列表查询
     */
    List<PatrolRouteVO> queryRouteList(PatrolRoutePageParam param);

    /**
     * 查询所有巡更路线
     *
     * @return 巡更路线
     */
    List<PatrolRouteVO> queryRouteAll();

    /**
     * 新增数据
     *
     * @param param 巡更信息
     *
     * @return 新增结果
     */
    Boolean insert(PatrolRouteParam param);

    /**
     * 更新数据
     *
     * @param param nfc信息
     *
     * @return 更新结果
     */
    Boolean update(PatrolRouteParam param);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    Boolean deleteById(Long id);

    Boolean deleteByLocationId(Long id);

    List<PatrolRouteVO> queryByUuids(Set<Long> collect);
}
