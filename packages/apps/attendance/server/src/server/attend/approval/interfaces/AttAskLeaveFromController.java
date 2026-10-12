package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces;

import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway.AttAskLeaveFromQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param.AttAskLeaveFromPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result.AttCancelAskFromResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 请假单控制层
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAskLeaveFromController {
    private final AttAskLeaveFromQueryService attAskLeaveFromQueryService;
    private final ProcessInstanceClient processInstanceClient;

    /**
     * 获取可销假的请假单
     *
     * @param param 条件入参
     */
    @GetMapping("/att/askLeaveFrom/cancelAskFrom/page")
    public Pagination<AttCancelAskFromResult> pageCancelAskFrom(AttAskLeaveFromPageParam param) {
        param.setNotCancel(true);
        param.setStatus(FlowResult.FINISHED.name());
        param.setStaffId(SecurityContextHolder.getPrincipal().getId());
        Pagination<AttAskLeaveFromPO> leaveFromPagination = attAskLeaveFromQueryService.askLeaveFromPage(param);

        if (leaveFromPagination.getTotal() <= AttConstants.zeroInt) {
            return new Pagination<>();
        }

        List<String> processInstanceIdList = new ArrayList<>();
        for (AttAskLeaveFromPO leaveFromPO : leaveFromPagination.getRecords()) {
            processInstanceIdList.add(leaveFromPO.getProcessInstanceId());
        }

        ProcessInstancePageQuery instancePageQuery = new ProcessInstancePageQuery();
        instancePageQuery.setPageSize(param.getPageSize());
        instancePageQuery.setPageNumber(param.getPageNumber());
        instancePageQuery.setApplicationCode("ATTENDANCE_MANAGEMENT");
        instancePageQuery.setBusinessType("LEAVE");
        instancePageQuery.setProcessInstanceIds(StringUtils.join(",", processInstanceIdList));
        Pagination<ProcessInstanceDTO> pagination = processInstanceClient.page(instancePageQuery);

        Map<String, ProcessInstanceDTO> flowMap = new HashMap<>();
        if (pagination.getRecords() != null) {
            for (ProcessInstanceDTO flow : pagination.getRecords()) {
                flowMap.put(flow.getProcessInstanceId(), flow);
            }
        }

        return new Pagination<>(leaveFromPagination.getTotal(), leaveFromPagination.getPages(), leaveFromPagination.getRecords().stream()
                .map(temp -> {
                    ProcessInstanceDTO flowDTO = flowMap.get(temp.getProcessInstanceId());
                    AttCancelAskFromResult result = AttCancelAskFromResult.builder().id(temp.getId()).holidayRuleId(temp.getHolidayId())
                            .leaveStartTime(temp.getLeaveStartTime()).leaveEndTime(temp.getLeaveEndTime()).build();
                    if (flowDTO != null) {
                        result.setFlowFromNo(flowDTO.getFormNo().toString());
                        result.setFlowInstanceName(flowDTO.getInstanceName());
                        result.setFlowStatus(flowDTO.getStatus());
                        result.setFlowSubmitTime(flowDTO.getSubmitedTime());
                    }
                    return result;
                }).collect(Collectors.toList()));
    }

}
