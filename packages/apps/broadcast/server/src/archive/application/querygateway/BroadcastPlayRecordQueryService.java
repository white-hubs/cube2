package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.BroadcastPlayRecordDeviceService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.BroadcastPlayRecordService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.querygateway.query.BroadcastPlayRecordQuery;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastPlayRecordDevicePO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastPlayRecordPO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.interfaces.vo.BroadcastPlayRecordPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastPlayRecordQueryService {

    private final BroadcastPlayRecordService broadcastPlayRecordService;

    private final BroadcastPlayRecordDeviceService broadcastPlayRecordDeviceService;

    public Pagination<BroadcastPlayRecordPageVO> getPage(BroadcastPlayRecordQuery query) {
        Page<BroadcastPlayRecordPO> broadcastPlayRecordPOPage = this.broadcastPlayRecordService.page(PagingUtils.convert(query), Wrappers
                .<BroadcastPlayRecordPO>lambdaQuery()
                .like(StringUtils.isNotEmpty(query.getKeyword()), BroadcastPlayRecordPO::getBroadcastTemplateName, query.getKeyword())
                .eq(Objects.nonNull(query.getTemplateId()), BroadcastPlayRecordPO::getBroadcastTemplateId, query.getTemplateId())
                .between(Objects.nonNull(query.getPeriod()), BroadcastPlayRecordPO::getCreateTime,
                         ObjectUtils.ifNull(query.getPeriod(), Period::getBegin),
                         ObjectUtils.ifNull(query.getPeriod(), Period::getEnd))
                .orderByDesc(BroadcastPlayRecordPO::getBroadcastTime));
        if (CollectionUtils.isEmpty(broadcastPlayRecordPOPage.getRecords())) {
            return Pagination.empty();
        }
        Set<Long> recordIds = broadcastPlayRecordPOPage.getRecords().stream().map(BroadcastPlayRecordPO::getId).collect(Collectors.toSet());
        Map<Long, List<BroadcastPlayRecordDevicePO>> recordDeviceMap = this.broadcastPlayRecordDeviceService.getByRecordIds(recordIds)
                .stream()
                .collect(Collectors.groupingBy(BroadcastPlayRecordDevicePO::getBroadcastPlayRecordId));
        broadcastPlayRecordPOPage.getRecords().forEach(record -> {
            if (recordDeviceMap.containsKey(record.getId())) {
                List<BroadcastPlayRecordDevicePO> playRecordDevicePOS = recordDeviceMap.get(record.getId());
                record.setBroadcastPlayDevices(playRecordDevicePOS.stream().map(BroadcastPlayRecordDevicePO::getDeviceName)
                                                       .collect(Collectors.joining(",")));
            }
        });
        return PagingUtils.convert(broadcastPlayRecordPOPage, BroadcastPlayRecordPageVO.class);
    }
}
