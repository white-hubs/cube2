package com.guhecloud.cube.srv.gen.maintain.client;

import com.guhecloud.cube.srv.gen.maintain.dto.PatrolLocationIdTypeDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.PatrolLocationPageParam;
import com.guhecloud.cube.srv.gen.maintain.vo.ElectronTaskSafetyReportVO;
import com.guhecloud.cube.srv.gen.maintain.vo.PatrolLocationVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author chx
 * @describe 巡更路线查询
 * @date 2024/03/20 09:49
 */
@FeignClient(ServiceRoute.TAG)
public interface PatrolLocationClient {

    /**
     * @Author chx
     * @Description 巡更点详情查询
     **/
    @GetMapping("/patrol/location/{id}")
    PatrolLocationVO queryById(@PathVariable("id") Long id);

    /**
     * 巡更路线列表(不分页)
     *
     * @param param 查询参数
     *
     * @return 巡更点列表
     */
    @GetMapping("/patrol/location/page")
    Pagination<PatrolLocationVO> queryLocationPage(@SpringQueryMap PatrolLocationPageParam param);

    /**
     * 巡更数据统计-安全管理报表
     *
     * @param period
     *
     * @return
     */
    @GetMapping("/electron/task/safety-report")
    ElectronTaskSafetyReportVO getElectronTaskForSafetyReport(@RequestParam("period") Period period);

    /**
     * 批量获取巡更点详情
     */
    @PostMapping("/patrol/location/batch")
    List<PatrolLocationVO> queryLocationBatch(@RequestBody PatrolLocationPageParam param);

    /**
     * 巡更点分页
     */
    @PostMapping("/patrol/points")
    Pagination<PatrolLocationVO> points(@RequestBody @Valid PatrolLocationPageParam param);

    /**
     * 获取巡更点ID列表
     *
     * @param param 查询参数
     *
     * @return 巡更点ID列表
     */
    @PostMapping("/patrol/location/idTypes")
    List<PatrolLocationIdTypeDTO> getPatrolPointIdTypes(@RequestBody PatrolLocationPageParam param);

    /**
     * 获取指定网格区域下的巡更点数量
     *
     * @param gridAreaIds 网格区域ID列表
     *
     * @return 网格区域ID对应的巡更点数量
     */
    @PostMapping("/patrol/count/by/areaIds")
    Map<Long, Long> countByAreaIds(@RequestBody List<Long> gridAreaIds);
}
