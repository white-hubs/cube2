package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.impl;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.constants.LineLossConstants;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dto.LineLossReadRecordDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossReadQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossClickHouseService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossReadService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.QueryCondition;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossReadRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineLossReadServiceImpl implements LineLossReadService {
    private final LineLossClickHouseService lineLossClickHouseService;
    private final DeviceCenterStdClient deviceCenterStdClient;

    @Override
    public Pagination<LineLossReadRecordVO> pageLossRead(LineLossReadQueryParam param) {
        // 构建查询条件
        QueryCondition queryCondition = buildQueryCondition(param);

        // 从 ClickHouse 查询分页数据
        Pagination<LineLossReadRecordDTO> pagination = lineLossClickHouseService.pageClickHouse(
                LineLossConstants.LINE_LOSS_READ_TABLE_NAME,
                LineLossReadRecordDTO.class,
                queryCondition,
                null, // 默认按创建时间倒序
                param.getPageNumber(),
                param.getPageSize()
                                                                                               );

        if (CollectionUtils.isEmpty(pagination.getRecords())) {
            return Pagination.empty();
        }

        var deviceIdSet = pagination.getRecords().stream().map(LineLossReadRecordDTO::getDeviceId).collect(Collectors.toSet());
        // 获取设备信息
        var deviceParam = new DevicePostByIdSetParam();
        deviceParam.setDeviceIdSet(deviceIdSet);
        var deviceInfoList = deviceCenterStdClient.postListByDeviceId(deviceParam);
        var deviceInfoMap = Optional.ofNullable(deviceInfoList).orElse(CollectionUtils.emptyList()).stream()
                .collect(Collectors.toMap(DeviceBaseArchivesInfoDTO::getId, item -> item, (o, n) -> o));
        // 转换为 VO 对象
        List<LineLossReadRecordVO> voList = convertToVOList(pagination.getRecords(), deviceInfoMap);

        return Pagination.<LineLossReadRecordVO>builder()
                .total(pagination.getTotal())
                .pages(pagination.getPages())
                .records(voList)
                .build();
    }

    /**
     * 构建查询条件
     *
     * @param param 查询参数
     *
     * @return 查询条件对象
     */
    private QueryCondition buildQueryCondition(LineLossReadQueryParam param) {
        String startTime = param.getStartTime();
        String endTime = param.getEndTime();
        Set<Long> deviceIdSet = param.getDeviceIdSet();

        // 如果开始时间和结束时间都为空，默认当前月
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime monthStart = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            startTime = monthStart.format(formatter);
            endTime = now.format(formatter);
        }

        // 使用 Builder 模式构建查询条件
        QueryCondition.Builder builder = QueryCondition.builder();

        // 添加时间范围条件
        builder.addTimeRange(startTime, endTime, "collect_time");

        // 如果设备 ID 集合不为空，添加设备 ID 过滤条件
        if (deviceIdSet != null && !deviceIdSet.isEmpty()) {
            builder.addInCondition("device_id", (Set<?>) deviceIdSet);
        }

        // 如果有属性码条件，添加属性码过滤
        if (param.getAttributeCode() != null && !param.getAttributeCode().trim().isEmpty()) {
            builder.addEqCondition("attribute_code", param.getAttributeCode());
        }

        return builder.build();
    }

    /**
     * 将 DTO 列表转换为 VO 列表
     *
     * @param dtoList DTO 列表
     *
     * @return VO 列表
     */
    private List<LineLossReadRecordVO> convertToVOList(List<LineLossReadRecordDTO> dtoList,
                                                       Map<Long, DeviceBaseArchivesInfoDTO> deviceInfoMap
                                                      ) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>();
        }

        List<LineLossReadRecordVO> voList = new ArrayList<>();
        for (LineLossReadRecordDTO dto : dtoList) {
            var deviceInfo = deviceInfoMap.getOrDefault(dto.getDeviceId(), new DeviceBaseArchivesInfoDTO());
            LineLossReadRecordVO vo = new LineLossReadRecordVO();
            // deviceId 和 attributeCode 需要根据实际情况转换为 deviceCode 和 deviceName
            // 这里暂时留空，需要通过设备档案服务查询
            vo.setDeviceCode(deviceInfo.getCode());
            vo.setDeviceName(deviceInfo.getName());
            vo.setLastCollectTime(dto.getLastCollectTime());
            vo.setLastReadValue(dto.getLastReadValue());
            vo.setCurrentCollectTime(dto.getCollectTime());
            vo.setReadValue(dto.getReadValue());
            vo.setUseValue(dto.getUseValue());
            voList.add(vo);
        }

        return voList;
    }
}
