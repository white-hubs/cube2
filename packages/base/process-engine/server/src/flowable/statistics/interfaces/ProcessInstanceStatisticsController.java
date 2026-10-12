package com.guhecloud.cube.base.flow.flowable.statistics.interfaces;

import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.dto.ProcessInstanceNumberOfGroupByResultDTO;
import com.guhecloud.cube.base.flow.flowable.process.application.querygateway.query.ProcessInstancePageQuery;
import com.guhecloud.cube.base.flow.flowable.process.infrastructure.mapper.ExtProcessInstanceMapper;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.BacklogKey;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.NumberOfApplicationDTO;
import com.guhecloud.cube.base.flow.flowable.process.interfaces.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.flowable.statistics.vo.ProcessInstanceNumberOfAllVO;
import com.guhecloud.cube.base.flow.flowable.task.application.querygateway.TaskInstanceQueryService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProcessInstanceStatisticsController {

    private final ExtProcessInstanceMapper extProcessInstanceMapper;
    private final TaskInstanceQueryService taskInstanceQueryService;

    /**
     * 已完成数 未完成数
     */
    @GetMapping("/statistics/number")
    public List<ProcessInstanceNumberOfGroupByResultDTO> getNumberOfApplication(String appCodes) {
        List<String> applicationCodeList = new ArrayList<>();
        if (StringUtils.isNotBlank(appCodes)) {
            applicationCodeList = Arrays.asList(appCodes.split(","));
        }
        List<ProcessInstanceNumberOfGroupByResultDTO> processInstanceNumberOfGroupByResultDTOS = extProcessInstanceMapper.selectNumberGroupByResult(applicationCodeList);
        List<ProcessInstanceNumberOfGroupByResultDTO> result = new ArrayList<>(2);
        // 统计已完成和未完成数量
        ProcessInstanceNumberOfGroupByResultDTO finished = new ProcessInstanceNumberOfGroupByResultDTO("FINISHED", 0);
        ProcessInstanceNumberOfGroupByResultDTO unfinished = new ProcessInstanceNumberOfGroupByResultDTO("UNFINISHED", 0);

        processInstanceNumberOfGroupByResultDTOS.forEach(processInstanceNumberOfGroupByResultDTO -> {
            if (Objects.nonNull(processInstanceNumberOfGroupByResultDTO.getResult())
                && (processInstanceNumberOfGroupByResultDTO.getResult().equals("PASS")
                    || processInstanceNumberOfGroupByResultDTO.getResult().equals("UNDO")
                    || processInstanceNumberOfGroupByResultDTO.getResult().equals("REJECT"))
            ) {
                finished.setNumber(finished.getNumber() + processInstanceNumberOfGroupByResultDTO.getNumber());
            } else {
                unfinished.setNumber(unfinished.getNumber() + processInstanceNumberOfGroupByResultDTO.getNumber());
            }
        });

        result.add(finished);
        result.add(unfinished);
        return result;
    }

    /**
     * 按应用维度统计
     */
    @GetMapping("/statistics/application/number")
    public List<NumberOfApplicationDTO> getNumberOfApplication(@RequestParam Period period) {
        return extProcessInstanceMapper.selectNumberOfApplication(period);
    }

    /**
     * @Author wql
     * @Description 统计流程实例数量
     * @Date 16:40 2023/5/26
     **/
    @GetMapping("/statistics/user/flow/number")
    public ProcessInstanceNumberOfAllVO userHandle(
            @RequestParam(value = "businessType", required = false) String businessType,
            @RequestParam("applicationCode") String applicationCode,
            @RequestParam("period") Period period
                                                  ) {
        //统计我发起的
        ProcessInstancePageQuery query = new ProcessInstancePageQuery();
        query.setPageSize(1);
        query.setPageNumber(1);
        query.setBusinessType(businessType);
        query.setApplicationCode(applicationCode);
        query.setSubmitTime(period);
        query.setType(BacklogKey.SUBMITTED_OF_ME.getValue());
        Pagination<ProcessInstanceDTO> submitPage = taskInstanceQueryService.page(query);
        long startNum = ObjectUtils.isEmpty(submitPage) ? 0 : submitPage.getTotal();
        //统计我待办的
        query.setStartUserId(null);
        query.setType(BacklogKey.BACKLOG_OF_ME.getValue());
        Pagination<ProcessInstanceDTO> backlogPage = taskInstanceQueryService.page(query);
        long waitingNum = ObjectUtils.isEmpty(backlogPage) ? 0 : backlogPage.getTotal();
        //统计已处理的
        query.setType(BacklogKey.PROCESSED_OF_ME.getValue());
        Pagination<ProcessInstanceDTO> processPage = taskInstanceQueryService.page(query);
        long processNum = ObjectUtils.isEmpty(processPage) ? 0 : processPage.getTotal();
        return new ProcessInstanceNumberOfAllVO(startNum, waitingNum, processNum);
    }
}
