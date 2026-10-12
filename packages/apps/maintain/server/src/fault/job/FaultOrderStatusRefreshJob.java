package com.guhecloud.cube.srv.gen.maintain.server.fault.job;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dto.FaultOrderDTO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultOrder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/4 15:00
 * @Description:
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaultOrderStatusRefreshJob {
    private final ProcessInstanceClient processInstanceClient;
    private final FaultOrderService faultOrderService;

    @JobAnnotation(name = "faultOrderStatusRefreshJob", cron = "0 0 0 * * ?", desc = "故障报修-故障报修数据补全")
    public void execute(String param) {
        SessionContextHolder.requestTenant(Integer.parseInt(param));
        var processInstancePageQuery = new ProcessInstancePageQuery();
        processInstancePageQuery.setApplicationCode("FAULT");
        processInstancePageQuery.setBusinessType("FAULT");
        processInstancePageQuery.setPageSize(1000);
        processInstancePageQuery.setPageNumber(1);
        //获取流程数据
        var processInstanceDTOPagination = processInstanceClient.allPage(processInstancePageQuery);
        Map<String, ProcessInstanceDTO> updateMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(processInstanceDTOPagination.getRecords())) {
            processInstanceDTOPagination.getRecords().forEach(processInstanceDTO -> {
                updateMap.put(processInstanceDTO.getProcessInstanceId(), processInstanceDTO);
            });
        }
        var collect = new HashSet<>(updateMap.keySet());
        var list = faultOrderService.lambdaQuery()
                .in(FaultOrder::getProcessInsId, collect).list();
        for (var faultOrder : list) {
            var processInstanceDTO = updateMap.get(faultOrder.getProcessInsId());
            faultOrder.setState(processInstanceDTO.getStatus());
            faultOrder.setName(processInstanceDTO.getInstanceName());
            var message = processInstanceClient.getProcessInstanceFormInfo(faultOrder.getProcessInsId());
            final var faultOrderDTO = JSONObject.parseObject(message.getData(), FaultOrderDTO.class);
            if (ObjectUtils.isNotEmpty(faultOrderDTO)) {
                faultOrder.setFaultDescribe(faultOrderDTO.getFaultDescribe());
            }
        }
        var list1 = faultOrderService.lambdaQuery().notIn(FaultOrder::getProcessInsId, collect).list();
        if (CollectionUtils.isNotEmpty(list1)) {
            DatabaseContextHolder.softDelete(faultOrderService, list1);
        }
        faultOrderService.updateBatchById(list);
    }

}
