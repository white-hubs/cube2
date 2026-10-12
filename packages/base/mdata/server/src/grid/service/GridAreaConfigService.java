package com.guhecloud.cube.biz.mdata.server.grid.service;

import com.guhecloud.cube.biz.mdata.server.grid.param.GridAreaConfigParam;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridAreaConfigQueryParam;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridAreaMemberBatchSetParam;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaConfigDetailVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaConfigScreenVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaConfigVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaObjectCountVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaStatisticsVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaTreeVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridStatisticsVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridTotalStatisticsVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.MyGridVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 网格区域配置 Service
 */
public interface GridAreaConfigService {

    /**
     * 设置区域网格配置
     *
     * @param param 配置参数
     */
    void setGridAreaConfig(GridAreaConfigParam param);

    /**
     * 批量获取区域网格配置
     *
     * @return 配置信息Map，key为区域id，value为配置信息
     */
    Pagination<GridAreaConfigVO> getGridAreaConfigs(GridAreaConfigQueryParam param);

    /**
     * 统计网格信息（网格数、当班网格长数量）
     *
     * @return 统计信息
     */
    GridStatisticsVO getGridStatistics();

    /**
     * 网格总统计（网格数、网格长数、网格员数、设备数、资产数）
     *
     * @return 网格总统计
     */
    GridTotalStatisticsVO getGridTotalStatistics();

    /**
     * 查询指定网格及下级网格统计
     *
     * @param gridAreaId 网格区域id
     *
     * @return 网格统计列表
     */
    List<GridAreaStatisticsVO> getGridAreaStatistics(Long gridAreaId, String dailyType);

    /**
     * 根据区域id获取区域的对象数统计
     *
     * @param gridAreaIds 网格区域id列表（可选，如果不传则查询所有已绑定网格的区域）
     *
     * @return 对象数统计Map，key为区域id，value为对象数统计信息
     */
    List<GridAreaObjectCountVO> getGridAreaObjectCount(List<Long> gridAreaIds);

    /**
     * 查询当前用户负责的网格
     *
     * @return 我负责的网格信息（在岗信息、头像、角色、负责的网格列表名称）
     */
    MyGridVO getMyGrids();

    /**
     * 根据区域id查询区域网格配置
     *
     * @param gridAreaId 网格区域id
     *
     * @return 网格配置信息（与设置配置时的参数格式一致）
     */
    GridAreaConfigDetailVO getGridAreaConfig(Long gridAreaId);

    /**
     * 获取网格区域负责人（网格管理屏使用）
     *
     * @param gridAreaId 网格区域id
     *
     * @return 网格信息配置
     */
    GridAreaConfigScreenVO getGridAreaConfigInfoForScreen(Long gridAreaId);

    List<GridAreaTreeVO> getGridAreaList();

    Map<Long, Set<Long>> getGridAreaConfigWithGridLeaderList(List<Long> areaIdList);

    Boolean transferGridManager(@RequestBody GridAreaConfigParam param);

    /**
     * 批量设置网格区域网格员（替换）
     *
     * @param param 参数
     */
    void setGridAreaMembers(List<GridAreaMemberBatchSetParam> param);

    /**
     * 批量清空指定网格的网格员
     *
     * @param gridAreaIds 网格区域id列表
     */
    void clearGridAreaMembers(List<Long> gridAreaIds);
}
