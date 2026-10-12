package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.querygateway;

import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.querygateway.param.IllegalParkingQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.IllegalParkingRecordRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.converters.IllegalParkingRecordConverts;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.IllegalParkingRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 17:30
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IllegalParkingQueryService {

    private final IllegalParkingRecordRepository illegalParkingRecordRepository;
    private final ProcessInstanceClient processInstanceClient;

    public Pagination<IllegalParkingRecordVO> recordList(IllegalParkingQueryParam parkingQueryParam) {
        if (StringUtils.isNotBlank(parkingQueryParam.getKeyword())) {
            List<IllegalParkingRecordPO> parkingRecordPOS = illegalParkingRecordRepository.lambdaQuery()
                    .select(IllegalParkingRecordPO::getProcessInstanceId)
                    .like(IllegalParkingRecordPO::getName, parkingQueryParam.getKeyword())
                    .list();
            if (CollectionUtils.isEmpty(parkingRecordPOS)) {
                return new Pagination<>();
            }
            parkingQueryParam.setProcessInstanceIdsList(parkingRecordPOS.stream().map(IllegalParkingRecordPO::getProcessInstanceId)
                                                                .collect(Collectors.toList()));
        }
        Pagination<ProcessInstanceDTO> page = processInstanceClient.page(parkingQueryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new Pagination<>();
        }
        Set<String> processInstanceIds = page.getRecords().stream().map(ProcessInstanceDTO::getProcessInstanceId)
                .collect(Collectors.toSet());
        Map<String, IllegalParkingRecordPO> illegalParkingRecordPOMap = Optional.ofNullable(illegalParkingRecordRepository.lambdaQuery()
                                                                                                    .in(IllegalParkingRecordPO::getProcessInstanceId, processInstanceIds)
                                                                                                    .list()).orElse(new ArrayList<>())
                .stream().collect(Collectors.toMap(IllegalParkingRecordPO::getProcessInstanceId, Function.identity()));
        return PagingUtils.convert(page, IllegalParkingRecordConverts.convertToRecordVO(page.getRecords(), illegalParkingRecordPOMap));
    }

}
