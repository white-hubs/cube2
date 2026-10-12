package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.querygateway.param.IllegalParkingBlacklistQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.querygateway.param.VehiclePenaltyRecordParam;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingBlacklistPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.VehiclePenaltyRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.IllegalParkingBlacklistRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.IllegalParkingRecordRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.VehiclePenaltyRecordRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.converters.IllegalParkingBlacklistConverts;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.converters.VehiclePenaltyRecordConverts;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.IllegalParkingBlacklistVO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.VehiclePenaltyRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 11:11
 * @Description:
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IllegalParkingBlacklistQueryService {

    private final IllegalParkingBlacklistRepository illegalParkingBlacklistRepository;

    private final VehiclePenaltyRecordRepository vehiclePenaltyRecordRepository;
    private final IllegalParkingRecordRepository illegalParkingRecordRepository;

    public Pagination<IllegalParkingBlacklistVO> getBlacklist(IllegalParkingBlacklistQueryParam queryParam) {
        Page<IllegalParkingBlacklistPO> illegalParkingBlacklistPOPage = illegalParkingBlacklistRepository.lambdaQuery()
                .and(StringUtils.isNotBlank(queryParam.getKeyword()),
                     t -> t.like(IllegalParkingBlacklistPO::getOwner, queryParam.getKeyword())
                             .or().like(IllegalParkingBlacklistPO::getPlateNumber, queryParam.getKeyword()))
                .and(Objects.nonNull(queryParam.getPeriod()), q -> q
                        .between(null != queryParam.getPeriod().getBegin() &&
                                 null != queryParam.getPeriod().getEnd(),
                                 IllegalParkingBlacklistPO::getCreateTime, queryParam.getPeriod().getBegin(), queryParam
                                         .getPeriod().getEnd())
                    )
                .orderByDesc(IllegalParkingBlacklistPO::getCreateTime)
                .orderByDesc(IllegalParkingBlacklistPO::getId)
                .page(PagingUtils.convert(queryParam));
        if (CollectionUtils.isEmpty(illegalParkingBlacklistPOPage.getRecords())) {
            return new Pagination<>();
        }
        List<String> plateNumberList = illegalParkingBlacklistPOPage.getRecords().stream().map(IllegalParkingBlacklistPO::getPlateNumber)
                .collect(Collectors.toList());
        Map<String, List<BigDecimal>> plateNumberScoreMap = Optional.ofNullable(vehiclePenaltyRecordRepository.lambdaQuery()
                                                                                        .in(VehiclePenaltyRecordPO::getPlateNumber, plateNumberList)
                                                                                        .list()).orElse(new ArrayList<>())
                .stream().filter(t -> Objects.nonNull(t.getViolationScore()))
                .collect(Collectors.groupingBy(VehiclePenaltyRecordPO::getPlateNumber, Collectors.mapping(VehiclePenaltyRecordPO::getViolationScore, Collectors.toList())));
        List<IllegalParkingBlacklistVO> illegalParkingBlacklistVOList = illegalParkingBlacklistPOPage.getRecords().stream()
                .map(t -> IllegalParkingBlacklistConverts.convertToVO(t, plateNumberScoreMap))
                .collect(Collectors.toList());
        return PagingUtils.convert(illegalParkingBlacklistPOPage, illegalParkingBlacklistVOList);
    }

    public Pagination<VehiclePenaltyRecordVO> getBlacklistDetail(VehiclePenaltyRecordParam param) {
        Page<VehiclePenaltyRecordPO> penaltyRecordPOPage = vehiclePenaltyRecordRepository.lambdaQuery()
                .eq(VehiclePenaltyRecordPO::getPlateNumber, param.getPlateNumber())
                .and(Objects.nonNull(param.getViolationTime()), q -> q
                        .between(null != param.getViolationTime().getBegin() &&
                                 null != param.getViolationTime().getEnd(),
                                 VehiclePenaltyRecordPO::getIllegalTime, param.getViolationTime().getBegin(), param
                                         .getViolationTime().getEnd())
                    )
                .orderByDesc(VehiclePenaltyRecordPO::getIllegalTime)
                .page(PagingUtils.convert(param));
        if (CollectionUtils.isEmpty(penaltyRecordPOPage.getRecords())) {
            return new Pagination<>();
        }
        Set<Long> illegalRecordId = penaltyRecordPOPage.getRecords().stream().map(VehiclePenaltyRecordPO::getIllegalRecordId)
                .collect(Collectors.toSet());
        Map<Long, IllegalParkingRecordPO> parkingRecordPOMap = Optional.ofNullable(illegalParkingRecordRepository.lambdaQuery()
                                                                                           .in(IllegalParkingRecordPO::getId, illegalRecordId)
                                                                                           .list()).orElse(new ArrayList<>())
                .stream().collect(Collectors.toMap(IllegalParkingRecordPO::getId, Function.identity()));
        List<VehiclePenaltyRecordVO> penaltyRecordVOS = penaltyRecordPOPage.getRecords()
                .stream().map(t -> VehiclePenaltyRecordConverts.convertToVO(t, parkingRecordPOMap))
                .collect(Collectors.toList());
        return PagingUtils.convert(penaltyRecordPOPage, penaltyRecordVOS);
    }
}
