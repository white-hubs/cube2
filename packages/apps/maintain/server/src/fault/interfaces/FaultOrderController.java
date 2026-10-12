package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces;

import com.guhecloud.cube.base.flow.client.dto.PageOfDataBoardDTO;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.OrderHomePageVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.command.FaultBatchDeleteCommand;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.paran.FaultTypeStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultCompletedMetricVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultOrderVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultTypeStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultyEquipmentTopVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.FaultOrderPageQuery;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DeviceStatisticParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import instance.command.SubmitInitFormCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 故障单2.0
 *
 * @author wql
 * @describe 故障单2.0
 * @date 2023/11/6 9:38
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaultOrderController {
    private final FaultOrderService faultOrderService;

    /**
     * 个人故障报修工单
     *
     * @Author wql
     * @Description 个人故障报修工单
     * @Date 11:36 2023/11/6
     **/
    @GetMapping("/fault/apply/personal")
    public Pagination<FaultOrderVO> personalPage(@Valid ProcessInstancePageQuery processInstancePageQuery) {
        return faultOrderService.personalPage(processInstancePageQuery);
    }

    /**
     * 所有故障报修工单
     *
     * @Author wql
     * @Description 所有故障报修工单
     * @Date 11:36 2023/11/6
     **/
    @GetMapping("/fault/apply/all")
    public Pagination<FaultOrderVO> allPage(@Valid FaultOrderPageQuery faultOrderPageQuery) {
        return faultOrderService.allPage(faultOrderPageQuery);
    }

    /**
     * 数据看板流程实例
     *
     * @Author wql
     * @Description 数据看板流程实例
     * @Date 9:53 2023/11/8
     **/
    @GetMapping("/fault/apply/backlog")
    public Pagination<FaultOrderVO> backlog(@Valid PageOfDataBoardDTO pageOfDataBoardDTO) {
        return faultOrderService.backlog(pageOfDataBoardDTO);
    }

    /**
     * app故障报修工单统计
     *
     * @Author wql
     * @Description app故障报修工单统计
     * @Date 14:37 2023/11/6
     **/
    @GetMapping("/fault/apply/statistics")
    public FaultStatisticsVO faultStatistics(Period period) {
        return faultOrderService.faultStatistics(period);
    }

    /**
     * @Author wql
     * @Description 可视化故障单统计
     * @Date 11:07 2023/11/28
     **/
    @GetMapping("/getFaultyEquipmentTop")
    public List<FaultyEquipmentTopVO> getFaultyEquipmentTop(Period period) {
        return faultOrderService.getFaultyEquipmentTop(period);
    }

    /**
     * 设备健康度分析
     *
     * @param deviceId 设备id
     * @param period   查询时间段
     *
     * @return 故障单数量
     */
    @GetMapping("/isFaultValid")
    public Long isFaultValid(@RequestParam("deviceId") String deviceId, @RequestParam("period") Period period) {
        return faultOrderService.isFaultValid(deviceId, period);
    }

    /**
     * 设备故障报修次数统计
     *
     * @param param 查询参数
     *
     * @return 报修次数
     */
    @PostMapping("/fault/deviceFaultNum")
    public List<DeviceOperationStatisticsDTO> deviceFaultNumStatistics(@RequestBody DeviceStatisticParam param) {
        return faultOrderService.deviceFaultNumStatistics(param);
    }

    /**
     * 对象故障单
     *
     * @param param 统计范围
     *
     * @return 隐患单id列表
     */
    @GetMapping("/fault/order/statistic")
    public Set<String> objFaultOrderStatistics(DeviceStatisticParam param) {
        return faultOrderService.objFaultOrderStatistics(param);
    }

    /**
     * 按网格区域统计未完成故障工单数
     *
     * @param areaIds 网格区域id
     *
     * @return 未完成故障工单数
     */
    @PostMapping("/fault/order/unfinished/count/by/areaIds")
    public Map<Long, Long> countUnfinishedByAreaIds(@RequestBody List<Long> areaIds) {
        return faultOrderService.countUnfinishedByAreaIds(areaIds);
    }

    /**
     * 按网格区域统计故障工单数
     *
     * @param areaIds 网格区域id
     *
     * @return 故障工单数
     */
    @PostMapping("/fault/order/count/by/areaIds")
    public Map<Long, Long> countByAreaIds(@RequestBody List<Long> areaIds) {
        return faultOrderService.countByAreaIds(areaIds);
    }

    /**
     * 故障报销-报修单统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/fault/apply/home-page/statistics")
    @DataPermissionSwitch(dataModel = "faultWarranty:faultOrder")
    public OrderHomePageVO faultHomePageStatistics(BaseDateStatisticsParam param) {
        return faultOrderService.faultHomePageStatistics(param);
    }

    /**
     * 故障类型工单统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/fault/apply/statistics/type")
    public Pagination<FaultTypeStatisticsVO> faultTypeStatistics(FaultTypeStatisticsParam param) {
        return faultOrderService.faultTypeStatistics(param);
    }

    /**
     * 故障类型工单统计导出
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/fault/apply/statistics/typeExcel")
    public void faultTypeExcelStatistics(FaultTypeStatisticsParam param) {
        faultOrderService.faultTypeExcelStatistics(param);
    }

    /**
     * 故障保修流程发起前置校验
     *
     * @param formFlowCommand
     *
     * @return
     */
    @PostMapping("/fault/first/submit/check")
    public void checkFirstSubmit(@Valid @RequestBody SubmitInitFormCommand formFlowCommand) {
        faultOrderService.checkFirstSubmit(formFlowCommand);
    }

    /**
     * 故障报修批量删除
     *
     * @param deleteCommand
     */
    @PostMapping("/fault/batch-deleted")
    public void batchDeleteFault(@Valid @RequestBody FaultBatchDeleteCommand deleteCommand) {
        faultOrderService.batchDeleteFault(deleteCommand);
    }

    /**
     * 故障保修指标
     */
    @GetMapping("/fault/completed/metric")
    public FaultCompletedMetricVO faultCompletedMetic(@RequestParam("period") Period period,
                                                      @RequestParam(value = "type", required = false) String type
                                                     ) {
        return faultOrderService.faultCompletedMetic(period, type);
    }
}
