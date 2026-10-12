package com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.ObjInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.ObjTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.TicketStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.service.ObjInfoGainService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dao.FaultReportMapper;
import com.guhecloud.cube.srv.gen.maintain.server.fault.enumeration.ReportEnum;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.DevcieCountVo;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultAreaAnalyseVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultReportObjAnalyseVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultReportOverviewVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultReportVo;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.ObjAreaVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultOrder;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultReport;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.FaultReportQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.ReportQuery;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaultReportQueryServiceImpl extends ServiceImpl<FaultReportMapper, FaultReport> implements FaultReportQueryService {
    private final FaultOrderService faultOrderService;
    private final ObjInfoGainService objInfoGainService;

    @Override
    public Pagination<FaultReportVo> getReport(ReportQuery reportQuery) {
        Page<FaultReport> page = this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(reportQuery.getType()), FaultReport::getType, reportQuery.getType())
                .like(StringUtils.isNotEmpty(reportQuery.getKeyword()), FaultReport::getName, reportQuery.getKeyword())
                .orderByDesc(BasicEntity::getCreateTime)
                .page(PagingUtils.convert(reportQuery));
        List<FaultReport> records = page.getRecords();
        List<FaultReportVo> copying = ObjectUtils.copying(records, FaultReportVo.class);
        for (FaultReportVo faultReportVo : copying) {
            faultReportVo.setTypeName("分析" + ReportEnum.getNameByCode(faultReportVo.getType()));
        }
        // 构建信息
        return PagingUtils.convert(page, copying);
    }

    @Override
    public Map<String, Object> getReportOverview(Long id) {
        Map<String, Object> map = new HashMap<>();
        FaultReport report = this.getById(id);
        String type = report.getType();
        String nameByCode = ReportEnum.getNameByCode(type);
        String title = "分析" + nameByCode;
        String titleTime;
        var start = report.getStartTime();
        if (ReportEnum.REPORT_DAY.name().equals(type)) {
            titleTime = DateUtils.formatDate(start);
        } else {
            var end = report.getEndTime();
            titleTime = DateUtils.formatDate(start) + "~" + DateUtils.formatDate(end);
        }
        map.put("title", title);
        map.put("titleTime", titleTime);

        //故障信息总览
        getOverview(map, report);
        //故障设备分析
        getAnalysis(map, report);
        //区域分析
        getAreaAnalysis(map, report);
        return map;
    }

    private void getAreaAnalysis(Map<String, Object> map, FaultReport report) {
        List<FaultOrder> list = faultOrderService.lambdaQuery()
                .between(FaultOrder::getSubmitTime, report.getStartTime(), report.getEndTime())
                .isNotNull(FaultOrder::getObjType).isNotNull(FaultOrder::getObjId)
                .select(FaultOrder::getObjId, FaultOrder::getObjType, FaultOrder::getState).list();
        List<FaultAreaAnalyseVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(list)) {
            List<ObjAreaVO> areaVOS = new ArrayList<>();
            list.forEach(faultOrder -> {
                ObjInfoDTO objInfo = objInfoGainService.getObjInfo(faultOrder.getObjId(), faultOrder.getObjType());
                if (StringUtils.isNotBlank(objInfo.getAreaId())) {
                    areaVOS.add(ObjAreaVO.builder()
                                        .areaId(objInfo.getAreaId())
                                        .areaName(objInfo.getAreaName())
                                        .state(faultOrder.getState())
                                        .build());
                }
            });
            Map<String, List<ObjAreaVO>> collect = areaVOS.stream().collect(Collectors.groupingBy(ObjAreaVO::getAreaId));
            collect.keySet().forEach(key -> {
                List<ObjAreaVO> objAreaVOS = collect.get(key);
                Map<String, Long> count = objAreaVOS.stream().collect(Collectors.groupingBy(ObjAreaVO::getState, Collectors.counting()));
                vos.add(FaultAreaAnalyseVO.builder()
                                .areaName(objAreaVOS.get(0).getAreaName())
                                .objAreaTotal(objAreaVOS.size())
                                .objAreaConfirm(getMapValue(count, TicketStatusEnum.WAITING_CONFIRM.name()))
                                .objAreaHandle(getMapValue(count, TicketStatusEnum.WAITING_FOR_HANDLE.name()))
                                .objAreaClosed(getMapValue(count, TicketStatusEnum.TO_BE_CLOSED.name()))
                                .objAreaFinished(getMapValue(count, TicketStatusEnum.FINISHED.name()))
                                .build());
            });
        }
        map.put("analysisAreaTotal", vos.size());
        map.put("analysisAreaFirstName", ObjectUtils.isEmpty(vos) ? "" : vos.get(0).getAreaName());
        map.put("analysisAreaList", vos);
    }

    private void getAnalysis(Map<String, Object> map, FaultReport report) {
        List<FaultOrder> list = faultOrderService.lambdaQuery()
                .between(FaultOrder::getSubmitTime, report.getStartTime(), report.getEndTime())
                .isNotNull(FaultOrder::getObjType).select(FaultOrder::getObjType, FaultOrder::getState).list();
        Map<String, List<FaultOrder>> collect = list.stream().collect(Collectors.groupingBy(FaultOrder::getObjType));
        List<FaultReportObjAnalyseVO> analyseVOS = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(collect)) {
            List<FaultReportObjAnalyseVO> finalAnalyseVOS = new ArrayList<>();
            collect.keySet().forEach(key -> {
                List<FaultOrder> faultOrders = collect.get(key);
                Map<String, Long> count = faultOrders.stream().collect(Collectors.groupingBy(FaultOrder::getState, Collectors.counting()));
                finalAnalyseVOS.add(FaultReportObjAnalyseVO.builder()
                                            .objTypeName(ObjTypeEnum.getNameByCode(key))
                                            .objTypeTotal(faultOrders.size())
                                            .objTypeConfirm(getMapValue(count, TicketStatusEnum.WAITING_CONFIRM.name()))
                                            .objTypeHandle(getMapValue(count, TicketStatusEnum.WAITING_FOR_HANDLE.name()))
                                            .objTypeClosed(getMapValue(count, TicketStatusEnum.TO_BE_CLOSED.name()))
                                            .objTypeFinished(getMapValue(count, TicketStatusEnum.FINISHED.name()))
                                            .build());
            });
            analyseVOS = finalAnalyseVOS.stream().sorted(Comparator.comparing(FaultReportObjAnalyseVO::getObjTypeTotal).reversed())
                    .collect(Collectors.toList());
        }
        map.put("analysisTotal", collect.size());
        map.put("analysisFirstName", ObjectUtils.isEmpty(analyseVOS) ? "" : analyseVOS.get(0).getObjTypeName());
        map.put("analysisList", analyseVOS);
    }

    /**
     * @Author wql
     * @Description 故障信息总览
     * @Date 11:25 2023/11/9
     **/
    private void getOverview(Map<String, Object> map, FaultReport report) {
        List<FaultOrder> list = faultOrderService.lambdaQuery()
                .between(FaultOrder::getSubmitTime, report.getStartTime(), report.getEndTime())
                .select(FaultOrder::getSubmitTime, FaultOrder::getName, FaultOrder::getObjName, FaultOrder::getSource,
                        FaultOrder::getSubmitStaff, FaultOrder::getState, FaultOrder::getResult).list();
        Map<String, Long> collect = new HashMap<>();
        List<FaultReportOverviewVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(list)) {
            list.forEach(t -> {
                if (TicketStatusEnum.getNotActiveStatus().contains(t.getState())) {
                    t.setState(TicketStatusEnum.FINISHED.name());
                }
                if (FlowResult.ACTIVE.name().equals(t.getState())) {
                    t.setState(TicketStatusEnum.WAITING_FOR_HANDLE.name());
                }
            });
            collect = list.stream().collect(Collectors.groupingBy(FaultOrder::getState, Collectors.counting()));
            list.forEach(faultOrder -> {
                vos.add(FaultReportOverviewVO.builder()
                                .submitTime(faultOrder.getSubmitTime())
                                .name(faultOrder.getName())
                                .objName(faultOrder.getObjName())
                                .resource(faultOrder.getSource())
                                .submitter(faultOrder.getSubmitStaff())
                                .faultStatus(TicketStatusEnum.getDescription(faultOrder.getState()))
                                .faultResult(faultOrder.getResult())
                                .build());
            });
        }
        map.put("waitingForConfirm", getMapValue(collect, TicketStatusEnum.WAITING_CONFIRM.name()));
        map.put("waitingForHandle", getMapValue(collect, TicketStatusEnum.WAITING_FOR_HANDLE.name()));
        map.put("toBeClosed", getMapValue(collect, TicketStatusEnum.TO_BE_CLOSED.name()));
        map.put("finished", getMapValue(collect, TicketStatusEnum.FINISHED.name()));
        map.put("total", vos.size());
        map.put("reportOverviewList", vos);
    }

    private Long getMapValue(Map<String, Long> map, String key) {
        Long value = map.get(key);
        return ObjectUtils.isEmpty(value) ? 0L : value;
    }

    @Override
    public List<DevcieCountVo> getDeviceCount(List<Long> ids) {
        List<DevcieCountVo> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(ids)) {
            List<FaultOrder> list = faultOrderService.lambdaQuery().in(FaultOrder::getObjId, ids)
                    .select(FaultOrder::getObjId, FaultOrder::getCost).list();
            if (ObjectUtils.isNotEmpty(list)) {
                Map<Long, List<FaultOrder>> collect = list.stream().collect(Collectors.groupingBy(FaultOrder::getObjId));
                collect.keySet().forEach(key -> {
                    double sum = collect.get(key).stream().mapToDouble(FaultOrder::getCost).sum();
                    vos.add(DevcieCountVo.builder()
                                    .deviceId(key)
                                    .num(BigDecimal.valueOf(sum))
                                    .build());
                });
            }
        }
        return vos;
    }

}
