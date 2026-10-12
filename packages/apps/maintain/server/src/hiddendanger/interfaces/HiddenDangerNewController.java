package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces;

import com.guhecloud.cube.base.flow.client.dto.PageOfDataBoardDTO;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.OrderHomePageVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerNewService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.dto.HiddendangerWordOrderStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param.HiddenBatchDeleteCommand;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param.HiddenDangerProInsParam;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param.HiddenTaskStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param.HiddenWorkOrderStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerNewVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenTaskStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DeviceStatisticParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

/**
 * 隐患排查2.0
 *
 * @author wql
 * @describe 隐患排查2.0
 * @date 2023/11/8 20:39
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerNewController {
    private final HiddenDangerNewService hiddenDangerNewService;

    /**
     * 个人隐患排查工单
     *
     * @Author wql
     * @Description 个人隐患排查工单
     * @Date 11:36 2023/11/6
     **/
    @GetMapping("/hidden/apply/personal")
    public Pagination<HiddenDangerNewVO> personalPage(@Valid ProcessInstancePageQuery processInstancePageQuery) {
        return hiddenDangerNewService.personalPage(processInstancePageQuery);
    }

    /**
     * 所有隐患排查工单
     *
     * @Author wql
     * @Description 所有隐患排查工单
     * @Date 11:36 2023/11/6
     **/
    @GetMapping("/hidden/apply/all")
    public Pagination<HiddenDangerNewVO> allPage(@Valid HiddenDangerProInsParam processInstancePageQuery) {
        return hiddenDangerNewService.allPage(processInstancePageQuery);
    }

    /**
     * 数据看板流程实例
     *
     * @Author wql
     * @Description 数据看板流程实例
     * @Date 9:53 2023/11/8
     **/
    @GetMapping("/hidden/apply/backlog")
    public Pagination<HiddenDangerNewVO> backlog(@Valid PageOfDataBoardDTO pageOfDataBoardDTO) {
        return hiddenDangerNewService.backlog(pageOfDataBoardDTO);
    }

    /**
     * 隐患统计
     *
     * @Author wql
     * @Description 隐患统计
     * @Date 13:38 2023/11/12
     **/
    @GetMapping("/hidden/statistics")
    public HiddenTaskStatisticsVO HiddenTaskStatistics(HiddenTaskStatisticsParam statisticsParam) {
        return hiddenDangerNewService.HiddenTaskStatistics(statisticsParam);
    }

    /**
     * 设备隐患报修统计
     *
     * @param param 统计参数
     *
     * @return 隐患报修数量
     */
    @PostMapping("/hidden/deviceHiddenNum")
    List<DeviceOperationStatisticsDTO> deviceHiddenNumStatistics(@RequestBody DeviceStatisticParam param) {
        return hiddenDangerNewService.deviceHiddenNumStatistics(param);
    }

    /**
     * 对象隐患单统计
     *
     * @param param 统计范围
     *
     * @return 隐患单id列表
     */
    @GetMapping("/hidden/order/statistic")
    public Set<String> objHiddenOrderStatistics(DeviceStatisticParam param) {
        return hiddenDangerNewService.objHiddenOrderStatistics(param);
    }

    /**
     * 隐患整改-隐患单统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/hidden/order/home-page")
    @DataPermissionSwitch(dataModel = "hiddenDanger:hiddenDangerNew")
    public OrderHomePageVO objHiddenOrderHomePageStatistics(BaseDateStatisticsParam param) {
        return hiddenDangerNewService.objHiddenOrderHomePageStatistics(param);
    }

    /**
     * 隐患单工单统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/hidden/work/order/statistics")
    public Pagination<HiddendangerWordOrderStatisticsDTO> hiddendangerWorkOrderStatistics(HiddenWorkOrderStatisticsParam param) {
        return hiddenDangerNewService.hiddendangerWorkOrderStatistics(param);
    }

    /**
     * 隐患单工单统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/hidden/work/order/statistics/all")
    public List<HiddendangerWordOrderStatisticsDTO> hiddendangerWorkOrderStatisticsAll(HiddenWorkOrderStatisticsParam param) {
        return hiddenDangerNewService.hiddendangerWorkOrderStatisticsAll(param);
    }

    /**
     * 隐患单工单统计导出
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/hidden/work/order/statisticsExcel")
    public void hiddendangerWorkOrderStatisticsExcel(HiddenWorkOrderStatisticsParam param) {
        hiddenDangerNewService.hiddendangerWorkOrderStatisticsExcel(param);
    }

    /**
     * 隐患工单批量删除
     *
     * @param deleteCommand
     */
    @PostMapping("/hidden/batch-deleted")
    public void batchDeleteHidden(@Valid @RequestBody HiddenBatchDeleteCommand deleteCommand) {
        hiddenDangerNewService.batchDeleteHidden(deleteCommand);
    }
}
