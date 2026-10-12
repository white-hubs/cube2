package com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.dto.PageOfDataBoardDTO;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.OrderHomePageVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.command.FaultBatchDeleteCommand;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.paran.FaultTypeStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultCompletedMetricVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultOrderVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultTypeStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultyEquipmentTopVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultOrder;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.FaultOrderPageQuery;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DeviceStatisticParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import instance.command.ProcessStartedMessageAttrCommand;
import instance.command.SubmitInitFormCommand;
import instance.event.NodePassMessage;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wql
 * @describe 故障单2.0
 * @date 2023/11/6 9:28
 */
public interface FaultOrderService extends IService<FaultOrder> {

    /**
     * @Author wql
     * @Description 故障单信息新增
     * @Date 9:44 2023/11/6
     **/
    FaultOrder addFaultOrder(ProcessStartedMessageAttrCommand message);

    FaultOrder addComplaintOrder(ProcessStartedMessageAttrCommand message);

    /**
     * @Author wql
     * @Description 个人故障报修工单
     * @Date 11:38 2023/11/6
     **/
    Pagination<FaultOrderVO> personalPage(ProcessInstancePageQuery processInstancePageQuery);

    /**
     * @Author wql
     * @Description 所有故障报修工单
     * @Date 14:22 2023/11/6
     **/
    Pagination<FaultOrderVO> allPage(FaultOrderPageQuery faultOrderPageQuery);

    /**
     * @Author wql
     * @Description app故障报修工单统计
     * @Date 14:37 2023/11/6
     **/
    FaultStatisticsVO faultStatistics(Period period);

    /**
     * @Author wql
     * @Description 故障单流程列表字段转换
     * @Date 11:26 2023/11/7
     **/
    List<FaultOrderVO> coverPage(Pagination<ProcessInstanceDTO> page);

    /**
     * @Author wql
     * @Description 更新故障单审批结果
     * @Date 14:43 2023/11/7
     **/
    void updateResult(String processInstanceId, String data, FlowResult flowResult);

    /**
     * @Author wql
     * @Description 分页查询故障单信息
     * @Date 15:06 2023/11/7
     **/
    Page<FaultOrder> pageQuery(Paging page, List<String> ids);

    /**
     * @Author wql
     * @Description 数据看板流程实例
     * @Date 9:59 2023/11/8
     **/
    Pagination<FaultOrderVO> backlog(PageOfDataBoardDTO pageOfDataBoardDTO);

    /**
     * @Author wql
     * @Description 故障单流程节点审批通过
     * @Date 20:22 2023/11/8
     **/
    void updateFaultOrder(NodePassMessage message);

    void updateComplaintOrder(String processInstanceId, String data, String state);

    /**
     * @Author wql
     * @Description 可视化故障单统计
     * @Date 11:08 2023/11/28
     **/
    List<FaultyEquipmentTopVO> getFaultyEquipmentTop(Period period);

    /**
     * 设备故障报修次数统计
     *
     * @param param 查询参数
     *
     * @return 报修次数
     */
    List<DeviceOperationStatisticsDTO> deviceFaultNumStatistics(DeviceStatisticParam param);

    Set<String> objFaultOrderStatistics(DeviceStatisticParam param);

    Map<Long, Long> countUnfinishedByAreaIds(List<Long> areaIds);

    Map<Long, Long> countByAreaIds(List<Long> areaIds);

    Long isFaultValid(String deviceId, Period period);

    OrderHomePageVO faultHomePageStatistics(BaseDateStatisticsParam param);

    Pagination<FaultTypeStatisticsVO> faultTypeStatistics(FaultTypeStatisticsParam param);

    void faultTypeExcelStatistics(FaultTypeStatisticsParam param);

    /**
     * 流程提交前置校验
     *
     * @param formFlowCommand
     */
    void checkFirstSubmit(SubmitInitFormCommand formFlowCommand);

    /**
     * 故障单批量删除
     *
     * @param deleteCommand
     */
    void batchDeleteFault(FaultBatchDeleteCommand deleteCommand);

    /**
     * 故障单完成率统计
     *
     * @param period
     * @param type
     *
     * @return
     */
    FaultCompletedMetricVO faultCompletedMetic(Period period, String type);

    void deleteFaultOrder(List<String> processInstanceIdList);
}
