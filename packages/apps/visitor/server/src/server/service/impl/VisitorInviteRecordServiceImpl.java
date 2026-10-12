package com.guhecloud.cube.srv.gen.visitor.management.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.srv.gen.visitor.management.server.enums.VisitorInviteStausEnum;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.po.VisitorInvitePO;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.ReservationRecordMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.VisitorInviteRecordMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.ReservationRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitAreaPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitorInviteRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.service.VisitAreaService;
import com.guhecloud.cube.srv.gen.visitor.management.server.service.VisitorInviteRecordService;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.VisitorInviteRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.VisitorInviteRecordResult;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/24 16:56
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitorInviteRecordServiceImpl extends ServiceImpl<VisitorInviteRecordMapper, VisitorInviteRecordPO> implements VisitorInviteRecordService {
    private final VisitAreaService visitAreaService;
    private final FlowClient flowClient;
    private final ReservationRecordMapper reservationRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pagination<VisitorInviteRecordResult> visitorInviteRecordPage(VisitorInviteRecordParam param) {

        Page<VisitorInviteRecordPO> page = baseMapper.selectPage(PagingUtils.convert(param), Wrappers
                .<VisitorInviteRecordPO>lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getPhone()), VisitorInviteRecordPO::getVisitorPhone, param.getPhone())
                .orderByDesc(VisitorInviteRecordPO::getCreateTime));
        if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
            return new Pagination<>();
        }
        List<Long> areaIdList = page.getRecords().stream().filter(t -> StringUtils.isNotBlank(t.getAreaId())).map(t -> {
                    return Arrays.stream(t.getAreaId().split(",")).collect(Collectors.toList());
                }).flatMap(Collection::stream)
                .map(Long::parseLong)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> areaMap;
        if (!CollectionUtils.isEmpty(areaIdList)) {
            areaMap = visitAreaService.lambdaQuery()
                    .in(VisitAreaPO::getId, areaIdList)
                    .list()
                    .stream().collect(Collectors.toMap(VisitAreaPO::getId, VisitAreaPO::getAreaName));
        } else {
            areaMap = new HashMap<>();
        }
        List<String> processInsIdList = page.getRecords().stream().filter(t -> StringUtils.isNotBlank(t.getProcessInsId()))
                .map(VisitorInviteRecordPO::getProcessInsId)
                .collect(Collectors.toList());
        Map<String, String> processIdBySourceId;
        Map<String, ReservationRecordPO> reservationRecordPOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(processInsIdList)) {
            processIdBySourceId = flowClient.getProcessIdBySourceId(processInsIdList);
            List<ReservationRecordPO> reservationRecordPOS = reservationRecordMapper.selectList(Wrappers.<ReservationRecordPO>lambdaQuery()
                                                                                                        .in(ReservationRecordPO::getProcessId, processInsIdList));
            if (!CollectionUtils.isEmpty(reservationRecordPOS)) {
                reservationRecordPOMap = reservationRecordPOS.stream()
                        .collect(Collectors.toMap(ReservationRecordPO::getProcessId, t -> t, (k1, k2) -> k1));
            }

        } else {
            processIdBySourceId = new HashMap<>();
        }
        for (VisitorInviteRecordPO record : page.getRecords()) {
            //存在预约记录进行更新
            if (Objects.nonNull(reservationRecordPOMap.get(record.getProcessInsId()))) {
                ReservationRecordPO reservationRecordPO = reservationRecordPOMap.get(record.getProcessInsId());
                record.setReservationRecordId(reservationRecordPO.getId());
                record.setBarCode(reservationRecordPO.getVBarCode());
                record.setStatus(VisitorInviteStausEnum.COMPLETE.name());
            }
        }
        this.updateBatchById(page.getRecords());
        Pagination<VisitorInviteRecordResult> convert = PagingUtils.convert(page, VisitorInviteRecordResult.class);
        convert.getRecords().forEach(t -> {
            if (StringUtils.isNotBlank(t.getAreaId())) {
                String areaStr = Arrays.stream(t.getAreaId().split(",")).filter(area -> areaMap.containsKey(Long.parseLong(area)))
                        .map(area -> {
                            return areaMap.get(Long.parseLong(area));
                        }).collect(Collectors.joining(","));
                t.setAreaName(areaStr);
            }
            t.setProcessInsId(processIdBySourceId.getOrDefault(t.getProcessInsId(), t.getProcessInsId()));
        });
        return convert;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateVisitorInviteRecord(VisitorInvitePO visitorInvitePO, String processInsId, String status, String barCode) {
        VisitorInviteRecordPO visitorInviteRecordPO = this.lambdaQuery()
                .eq(VisitorInviteRecordPO::getProcessInsId, processInsId)
                .orderByDesc(VisitorInviteRecordPO::getCreateTime)
                .last("limit 1")
                .one();
        if (Objects.isNull(visitorInviteRecordPO)) {
            visitorInviteRecordPO = new VisitorInviteRecordPO();
            visitorInviteRecordPO.setId(IdWorker.getId());
        }
        visitorInviteRecordPO.setVisitorName(visitorInvitePO.getVisitorName());
        visitorInviteRecordPO.setVisitorIdCard(visitorInvitePO.getVisitorIdCard());
        visitorInviteRecordPO.setVisitorPhone(visitorInvitePO.getVisitorPhone());
        visitorInviteRecordPO.setAreaId(!CollectionUtils.isEmpty(visitorInvitePO.getAreaId())
                                        ? visitorInvitePO.getAreaId().stream().map(Objects::toString).collect(Collectors.joining(","))
                                        : null);
        visitorInviteRecordPO.setProcessInsId(processInsId);
        visitorInviteRecordPO.setAccessDate(visitorInvitePO.getAccessDate());
        if (!CollectionUtils.isEmpty(visitorInvitePO.getTimeStr())) {
            visitorInviteRecordPO.setTimeStr(String.join("-", visitorInvitePO.getTimeStr()));
        }
        visitorInviteRecordPO.setReason(visitorInvitePO.getReason());
        visitorInviteRecordPO.setPlateNumber(visitorInvitePO.getPlateNumber());
        if (!CollectionUtils.isEmpty(visitorInvitePO.getFollowVisitors())) {
            visitorInviteRecordPO.setFollowVisitors(JsonUtils.serialize(visitorInvitePO.getFollowVisitors()));
        }
        visitorInviteRecordPO.setStatus(status);
        visitorInviteRecordPO.setProcessInsId(processInsId);
        visitorInviteRecordPO.setBarCode(barCode);
        this.saveOrUpdate(visitorInviteRecordPO);
    }
}
