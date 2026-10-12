package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.dto.PageOfDataBoardDTO;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.OrderHomePageVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.dto.HiddendangerWordOrderStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param.HiddenBatchDeleteCommand;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param.HiddenDangerProInsParam;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param.HiddenTaskStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param.HiddenWorkOrderStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HeadquartersDataGatherVo;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerHandleVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerNewVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerNumStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerTypeStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenTaskStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerNew;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DeviceStatisticParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import instance.command.ProcessStartedMessageAttrCommand;
import instance.event.NodePassMessage;

import java.util.List;
import java.util.Set;

/**
 * @author wql
 * @describe 隐患排查2.0
 * @date 2023/11/8 20:39
 */
public interface HiddenDangerNewService extends IService<HiddenDangerNew> {
    /**
     * @Author wql
     * @Description 个人故障报修工单
     * @Date 20:45 2023/11/8
     **/
    Pagination<HiddenDangerNewVO> personalPage(ProcessInstancePageQuery processInstancePageQuery);

    /**
     * @Author wql
     * @Description 所有故障报修工单
     * @Date 20:45 2023/11/8
     **/
    Pagination<HiddenDangerNewVO> allPage(HiddenDangerProInsParam processInstancePageQuery);

    /**
     * @Author wql
     * @Description 数据看板流程实例
     * @Date 20:45 2023/11/8
     **/
    Pagination<HiddenDangerNewVO> backlog(PageOfDataBoardDTO pageOfDataBoardDTO);

    /**
     * @Author wql
     * @Description 更新隐患排查单
     * @Date 9:55 2023/11/9
     **/
    void updateHiddenDanger(NodePassMessage message);

    /**
     * @Author wql
     * @Description 隐患排查审批结束
     * @Date 9:55 2023/11/9
     **/
    void updateResult(String processInstanceId, String data, FlowResult result);

    /**
     * @Author wql
     * @Description 隐患排查发起成功
     * @Date 9:55 2023/11/9
     **/
    HiddenDangerNew addHiddenDangerNew(ProcessStartedMessageAttrCommand message);

    /**
     * @Author wql
     * @Description 隐患排查2.0数据采集
     * @Date 9:27 2023/11/10
     **/
    List<HeadquartersDataGatherVo> headquartersDataGather(Period period);

    /**
     * @Author wql
     * @Description 隐患统计
     * @Date 13:43 2023/11/12
     **/
    HiddenTaskStatisticsVO HiddenTaskStatistics(HiddenTaskStatisticsParam statisticsParam);

    /**
     * @Author wql
     * @Description 获取子市场计算隐患风险指数数据
     * @Date 17:49 2023/11/16
     **/
    List<Boolean> getCountHiddenRiskIndex(Period period);

    /**
     * 设备隐患报修统计
     *
     * @param param 统计参数
     *
     * @return 隐患报修数量
     */
    List<DeviceOperationStatisticsDTO> deviceHiddenNumStatistics(DeviceStatisticParam param);

    HiddenDangerNumStatisticsVO hiddenDangerNumStatistics(Period period);

    HiddenDangerTypeStatisticsVO hiddenDangerTypeStatistics(Period period);

    Float hiddenDangerHandleRatio(Period period);

    Set<String> objHiddenOrderStatistics(DeviceStatisticParam param);

    HiddenDangerHandleVO hiddenDangerForSafetyReport(Period period);

    /**
     * 隐患整改-隐患单统计
     *
     * @param param
     *
     * @return
     */
    OrderHomePageVO objHiddenOrderHomePageStatistics(BaseDateStatisticsParam param);

    Pagination<HiddendangerWordOrderStatisticsDTO> hiddendangerWorkOrderStatistics(HiddenWorkOrderStatisticsParam param);

    void hiddendangerWorkOrderStatisticsExcel(HiddenWorkOrderStatisticsParam param);

    /**
     * 隐患单批量删除
     *
     * @param deleteCommand
     */
    void batchDeleteHidden(HiddenBatchDeleteCommand deleteCommand);

    List<HiddendangerWordOrderStatisticsDTO> hiddendangerWorkOrderStatisticsAll(HiddenWorkOrderStatisticsParam param);

    void deleteHiddenDanger(List<String> processInstanceIdList);
}
