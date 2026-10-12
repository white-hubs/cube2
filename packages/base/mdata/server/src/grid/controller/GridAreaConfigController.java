package com.guhecloud.cube.biz.mdata.server.grid.controller;

import com.guhecloud.cube.biz.mdata.server.grid.param.GridAreaConfigParam;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridAreaConfigQueryParam;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridAreaMemberBatchSetParam;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridModelConfigParam;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridAreaConfigService;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridModelConfigService;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaConfigDetailVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaConfigScreenVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaConfigVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaObjectCountVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaStatisticsVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaTreeVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridModelConfigVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridStatisticsVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridTotalStatisticsVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.MyGridVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 网格区域配置
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GridAreaConfigController {

    private final GridAreaConfigService gridAreaConfigService;
    private final GridModelConfigService gridModelConfigService;

    /**
     * 设置区域网格配置（管理部门、管理网格长、管理网格员）
     *
     * @param param 配置参数
     */
    @PostMapping("/grid/area/config")
    public void setGridAreaConfig(@Valid @RequestBody GridAreaConfigParam param) {
        gridAreaConfigService.setGridAreaConfig(param);
    }

    /**
     * 批量设置区域网格员（替换指定网格）
     *
     * @param param 参数
     */
    @PostMapping("/grid/area/config/members")
    public void setGridAreaMembers(@Valid @RequestBody List<GridAreaMemberBatchSetParam> param) {
        gridAreaConfigService.setGridAreaMembers(param);
    }

    /**
     * 批量清空指定网格的网格员
     *
     * @param gridAreaIds 网格区域id列表
     */
    @DeleteMapping("/grid/area/config/members")
    public void clearGridAreaMembers(@RequestBody List<Long> gridAreaIds) {
        gridAreaConfigService.clearGridAreaMembers(gridAreaIds);
    }

    /**
     * 网格区域负责人移交(编辑负责人)
     *
     * @param param 参数
     *
     * @return 是否成功
     */
    @PostMapping("/grid/area/config/transfer-manager")
    public Boolean transferGridManager(@RequestBody GridAreaConfigParam param) {
        return gridAreaConfigService.transferGridManager(param);
    }

    /**
     * 分页获取区域网格配置
     *
     * @param param 查询参数，包含网格区域id列表（可选，如果不传则查询所有已绑定网格的区域）
     *
     */
    @PostMapping("/grid/area/config/query")
    public Pagination<GridAreaConfigVO> getGridAreaConfigs(@Valid @RequestBody GridAreaConfigQueryParam param) {
        return gridAreaConfigService.getGridAreaConfigs(param);
    }

    /**
     * 统计网格信息（网格数、当班网格长数量）
     *
     * @return 统计信息
     */
    @GetMapping("/grid/statistics")
    public GridStatisticsVO getGridStatistics() {
        return gridAreaConfigService.getGridStatistics();
    }

    /**
     * 网格总统计（网格数、网格长数、网格员数、设备数、资产数）
     *
     * @return 网格总统计
     */
    @GetMapping("/grid/statistics/total")
    public GridTotalStatisticsVO getGridTotalStatistics() {
        return gridAreaConfigService.getGridTotalStatistics();
    }

    /**
     * 查询指定网格及下级网格统计
     *
     * @param gridAreaId 网格区域id
     *
     * @return 网格统计列表
     */
    @GetMapping("/grid/statistics/children")
    public List<GridAreaStatisticsVO> getGridAreaStatistics(@RequestParam Long gridAreaId,
                                                            @RequestParam("dailyType") String dailyType) {
        return gridAreaConfigService.getGridAreaStatistics(gridAreaId, dailyType);
    }

    /**
     * 根据区域id获取区域的对象数统计
     *
     * @param param 查询参数，包含网格区域id列表（可选，如果不传则查询所有已绑定网格的区域）
     *
     * @return 对象数统计Map，key为区域id，value为对象数统计信息
     */
    @PostMapping("/grid/area/object/count")
    public List<GridAreaObjectCountVO> getGridAreaObjectCount(@RequestBody GridAreaConfigQueryParam param) {
        return gridAreaConfigService.getGridAreaObjectCount(param.getGridAreaIds());
    }

    /**
     * 查询当前用户负责的网格
     *
     * @return 我负责的网格信息（在岗信息、头像、角色、负责的网格列表名称）
     */
    @GetMapping("/grid/my")
    public MyGridVO getMyGrids() {
        return gridAreaConfigService.getMyGrids();
    }

    /**
     * 根据区域id查询区域网格配置
     *
     * @param gridAreaId 网格区域id
     *
     * @return 网格配置信息（与设置配置时的参数格式一致）
     */
    @GetMapping("/grid/area/config/{gridAreaId}")
    public GridAreaConfigDetailVO getGridAreaConfig(@PathVariable Long gridAreaId) {
        return gridAreaConfigService.getGridAreaConfig(gridAreaId);
    }

    /**
     * 获取网格模型全局配置
     *
     * @return 网格模型全局配置
     */
    @GetMapping("/grid/model/config")
    public GridModelConfigVO getGridModelConfig() {
        return gridModelConfigService.getGridModelConfig();
    }

    /**
     * 设置网格模型全局配置
     *
     * @param param 配置参数
     */
    @PostMapping("/grid/model/config")
    public void setGridModelConfig(@Valid @RequestBody GridModelConfigParam param) {
        gridModelConfigService.setGridModelConfig(param.getModelItemId());
    }

    /**
     * 获取存在网格长的网格列表
     */
    @GetMapping("/grid/area/grid-leader")
    public List<GridAreaTreeVO> getGridAreaConfigWithGridLeader() {
        return gridAreaConfigService.getGridAreaList();
    }

    /**
     * 获取指定网格区域负责人
     */
    @PostMapping("/grid/area/grid-leader/list")
    public Map<Long, Set<Long>> getGridAreaConfigWithGridLeaderList(@RequestBody List<Long> areaIdList) {
        return gridAreaConfigService.getGridAreaConfigWithGridLeaderList(areaIdList);
    }

    /**
     * 获取网格区域负责人（网格管理屏使用）
     */
    @GetMapping("/public/grid/area/config/for/screen")
    public GridAreaConfigScreenVO getGridAreaConfigInfoForScreen(@RequestParam Long gridAreaId) {
        return gridAreaConfigService.getGridAreaConfigInfoForScreen(gridAreaId);
    }
}
