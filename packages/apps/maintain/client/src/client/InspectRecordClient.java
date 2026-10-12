package com.guhecloud.cube.srv.gen.maintain.client;

import com.guhecloud.cube.srv.gen.maintain.dto.InspectRecordItemQueryParam;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectScenePointQueryParam;
import com.guhecloud.cube.srv.gen.maintain.dto.InspectionRecordNumMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.TargetMaxRecordDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.TotalNumberRateDTO;
import com.guhecloud.cube.srv.gen.maintain.param.InspectionCommonParam;
import com.guhecloud.cube.srv.gen.maintain.vo.InspectRecordItemStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.vo.ScenePointVO;
import com.guhecloud.cube.srv.gen.maintain.vo.TargetMaxRecordVO;
import com.guhecloud.platform.base.model.Period;
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
 * @author wql
 * @describe 现场点查询
 * @date 2023/11/22 17:20
 */
@FeignClient(ServiceRoute.TAG)
public interface InspectRecordClient {
    /**
     * 电动车最新违规巡检
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/inspect-record/postTargetMaxError")
    Map<Long, Map<String, TargetMaxRecordVO>> postTargetMaxError(@RequestBody TargetMaxRecordDTO param);

    /**
     * @Author wql
     * @Description 现场点详情查询
     * @Date 17:20 2023/11/22
     **/
    @GetMapping("/inspect/detail/{id}")
    ScenePointVO scenePointDetail(@PathVariable("id") Long id);

    /**
     * 现场点列表(不分页)
     *
     * @param param 查询参数
     *
     * @return 现场点列表
     */
    @GetMapping("/inspect/pointListAll")
    List<ScenePointVO> scenePointListAll(@SpringQueryMap InspectScenePointQueryParam param);

    /**
     * 根据名称查询现场点
     *
     * @param name 现场点名称
     *
     * @return 现场点详情
     */
    @GetMapping("/inspect/detailByName")
    ScenePointVO scenePointDetailByName(@RequestParam("name") String name);

    /**
     * 巡检记录异常率
     *
     * @param param 时间段
     *
     * @return 异常率
     */
    @GetMapping("/inspectRecord/errRate")
    TotalNumberRateDTO getRecordErrRate(@SpringQueryMap InspectionCommonParam param);

    /**
     * 巡检记录异常率
     *
     * @param param 时间段
     *
     * @return 异常率
     */
    @PostMapping("/inspectRecord/errRate/post")
    TotalNumberRateDTO postRecordErrRate(@RequestBody InspectionCommonParam param);

    /**
     * 巡检记录完成率
     *
     * @param param 时间段
     *
     * @return 率
     */
    @GetMapping("/inspectRecord/completeRate")
    TotalNumberRateDTO getRecordCompleteRate(@SpringQueryMap InspectionCommonParam param);

    /**
     * 获取巡检记录项分组统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/inspect-record/items/statistics")
    List<InspectRecordItemStatisticsVO> getInspectRecordItemsStatistics(@SpringQueryMap InspectRecordItemQueryParam param);

    /**
     * 巡检次数指标
     */
    @GetMapping("/inspect-record/user/complete/num")
    InspectionRecordNumMetricDTO getRecordUserNumMetric(@RequestParam("period") Period period);

    /**
     * 是否存在巡检异常
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/inspect-record/error/flag")
    Boolean isExitAbnormal(@RequestParam("id") Long id, @RequestParam("businessType") String businessType);

}
