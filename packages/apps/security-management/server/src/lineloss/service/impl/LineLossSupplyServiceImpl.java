package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.ElectricitySupplyService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.ElectricityConnectionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.ElectricityNodeVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.ElectricitySupplyDetailVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.constants.LineLossConstants;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dao.LineLossAlarmRuleLineMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dao.LineLossAlarmRuleMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.dto.LineLossRecordDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossAlarmRule;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossConnection;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossNode;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossEditParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossTrendParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossClickHouseService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossConnectionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossNodeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossSupplyService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.QueryCondition;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.utils.StringSplitIdUtil;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.ElectricitySupplyChangeVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossAlarmRuleVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossSupplyDetailVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossTrendVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 线损关系图服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineLossSupplyServiceImpl implements LineLossSupplyService {
    private final ElectricitySupplyService electricitySupplyService;
    private final LineLossNodeService lineLossNodeService;
    private final LineLossConnectionService lineLossConnectionService;
    private final LineLossAlarmRuleLineMapper lineLossAlarmRuleLineMapper;
    private final LineLossAlarmRuleMapper lineLossAlarmRuleMapper;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final LineLossClickHouseService lineLossClickHouseService;

    /**
     * 获取供电关系变更内容
     *
     * @return 变更数据
     */
    @Override
    public ElectricitySupplyChangeVO getElectricitySupplyChange() {
        ElectricitySupplyChangeVO changeVO = new ElectricitySupplyChangeVO();
        // 获取供电关系数据
        ElectricitySupplyDetailVO detailVO = electricitySupplyService.detail();
        if (detailVO == null || CollectionUtils.isEmpty(detailVO.getNodeList()) || CollectionUtils.isEmpty(detailVO.getConnectionList())) {
            return changeVO;
        }
        List<ElectricityNodeVO> nodeList = detailVO.getNodeList();
        var nodeDeviceIdMap = new HashMap<Long, Long>();
        nodeList.forEach(nodeVO -> {
            // 如果有节点有且只有一个设备，则保存该设备
            if (StringUtils.isNotBlank(nodeVO.getDeviceIds()) && nodeVO.getDeviceIds().split(",").length == 1) {
                nodeDeviceIdMap.put(nodeVO.getId(), Long.valueOf(nodeVO.getDeviceIds().split(",")[0]));
            }
        });
        List<ElectricityConnectionVO> connectionList = detailVO.getConnectionList();

        this.handlerChangeNode(nodeList, changeVO);
        this.handlerChangeConnection(connectionList, changeVO, nodeDeviceIdMap);
        return changeVO;
    }

    /**
     * 同步供电关系到线损关系
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean syncFromElectricitySupply() {
        var changeVo = this.getElectricitySupplyChange();
        // 移除旧数据
        lineLossNodeService.remove(Wrappers.emptyWrapper());
        if (CollectionUtils.isNotEmpty(changeVo.getNodeInsertList())) {
            lineLossNodeService.saveBatch(changeVo.getNodeInsertList());
        }
        if (CollectionUtils.isNotEmpty(changeVo.getConnectionDeleteIdList())) {
            DatabaseContextHolder.softDelete(LineLossConnection.class, new HashSet<>(changeVo.getConnectionDeleteIdList()));
        }
        if (CollectionUtils.isNotEmpty(changeVo.getConnectionInsertList())) {
            lineLossConnectionService.saveBatch(changeVo.getConnectionInsertList());
        }
        if (CollectionUtils.isNotEmpty(changeVo.getConnectionUpdateList())) {
            lineLossConnectionService.saveOrUpdateBatch(changeVo.getConnectionUpdateList());
        }
        return true;
    }

    /**
     * 获取详情
     *
     * @return 详情
     */
    @Override
    public LineLossSupplyDetailVO detail() {
        var lineLossSupplyDetailVO = new LineLossSupplyDetailVO();
        // 查询节点
        var lineLossNodeList = lineLossNodeService.list();
        // 查询连接关系
        var lineLossConnectionList = lineLossConnectionService.list();
        // 获取线路和报警配置关系
        var lineAlarmRuleMap = this.getAllLineRuleMap();

        // 获取所有设备
        var deviceMap = this.getSupplyAllDeviceMap(lineLossNodeList, lineLossConnectionList);
        // 转换并填充节点数据
        lineLossSupplyDetailVO.setCovertNodeList(lineLossNodeList, deviceMap);
        // 填充连接关系数据
        lineLossSupplyDetailVO.setCovertConnectionList(lineLossConnectionList, lineAlarmRuleMap, deviceMap);
        // 返回
        return lineLossSupplyDetailVO;
    }

    /**
     * 修改线损关系中的设备
     *
     * @param param 参数
     *
     * @return 是否成功
     */
    @Override
    public Boolean editLossLineDevice(LineLossEditParam param) {
        var oldConnection = lineLossConnectionService.getById(param.getId());
        Assert.isNotFound(oldConnection, "未查询到相关信息");
        oldConnection.setFromDeviceId(param.getFromDeviceId());
        oldConnection.setToDeviceId(param.getToDeviceId());
        oldConnection.setUpdateTime(LocalDateTime.now());
        oldConnection.setUpdateBy(SecurityContextHolder.getPrincipal().getAccount());
        lineLossConnectionService.updateById(oldConnection);
        return true;
    }

    /**
     * 根据线路 ID 查询线损趋势
     *
     * @param param 查询参数
     *
     * @return 线损趋势数据列表
     */
    @Override
    public List<LineLossTrendVO> getLineLossTrend(LineLossTrendParam param) {
        Assert.isParameterError(param.getLineId() == null, "线路 ID 不能为空");
        Assert.isParameterError(StringUtils.isBlank(param.getTimeRange()), "时间类型不能为空");

        // 获取线路信息
        var connection = lineLossConnectionService.getById(param.getLineId());
        Assert.isNotFound(connection, "未查询到相关信息");

        // 根据时间范围计算开始和结束时间
        String[] timeRange = getTimeRange(param.getTimeRange());
        String startTime = timeRange[0];
        String endTime = timeRange[1];

        // 从 ClickHouse 查询数据 - 使用通用查询条件，直接使用 lineId
        var queryCondition = QueryCondition.builder()
                .addTimeRange(startTime, endTime, "collect_time")
                .addEqCondition("line_id", param.getLineId())
                .build();

        var recordList = lineLossClickHouseService.listClickHouse(
                LineLossConstants.LINE_LOSS_STAT_TABLE_NAME,
                LineLossRecordDTO.class,
                queryCondition,
                "collect_time"
                                                                 );

        // 将同一天的数据合并
        List<LineLossRecordDTO> aggregatedList = aggregateDailyData(recordList);

        // 转换为 VO 并返回
        return aggregatedList.stream()
                .map(this::convertToTrendVO)
                .sorted(Comparator.comparing(LineLossTrendVO::getCollectTime, Comparator.nullsLast(LocalDateTime::compareTo)).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 转换 DTO 为趋势 VO
     */
    private LineLossTrendVO convertToTrendVO(LineLossRecordDTO dto) {
        var trendVO = new LineLossTrendVO();
        trendVO.setLineId(dto.getLineId());
        trendVO.setLineName(dto.getLineName());
        trendVO.setLossRate(dto.getLossRate());
        trendVO.setInputEnergy(dto.getFromUseValue());
        trendVO.setOutputEnergy(dto.getToUseValue());
        trendVO.setCollectTime(dto.getCollectTime());
        return trendVO;
    }

    /**
     * 将同一天的线损数据合并为一条，累加输入能量、输出能量和线损率
     *
     * @param recordList 原始记录列表
     *
     * @return 聚合后的记录列表（每天一条记录）
     */
    private List<LineLossRecordDTO> aggregateDailyData(List<LineLossRecordDTO> recordList) {
        if (CollectionUtils.isEmpty(recordList)) {
            return List.of();
        }

        // 按日期分组聚合数据
        Map<String, LineLossRecordDTO> dailyMap = new HashMap<>();
        for (LineLossRecordDTO record : recordList) {
            // 提取日期部分（yyyy-MM-dd）
            String dateKey = DateUtils.formatDate(record.getCollectTime());

            if (!dailyMap.containsKey(dateKey)) {
                // 创建新的聚合记录
                LineLossRecordDTO aggregated = new LineLossRecordDTO();
                aggregated.setLineId(record.getLineId());
                aggregated.setLineName(record.getLineName());
                aggregated.setCollectTime(DateUtils.parse(dateKey + " 00:00:00")); // 使用当天的 00:00:00 作为时间
                aggregated.setFromUseValue(record.getFromUseValue() != null ? record.getFromUseValue() : BigDecimal.ZERO);
                aggregated.setToUseValue(record.getToUseValue() != null ? record.getToUseValue() : BigDecimal.ZERO);
                aggregated.setLossRate(record.getLossRate() != null ? record.getLossRate() : BigDecimal.ZERO);
                dailyMap.put(dateKey, aggregated);
            } else {
                // 累加已有记录的字段值
                LineLossRecordDTO existing = dailyMap.get(dateKey);
                if (record.getFromUseValue() != null) {
                    existing.setFromUseValue(existing.getFromUseValue().add(record.getFromUseValue()));
                }
                if (record.getToUseValue() != null) {
                    existing.setToUseValue(existing.getToUseValue().add(record.getToUseValue()));
                }
                if (record.getLossRate() != null) {
                    existing.setLossRate(existing.getLossRate().add(record.getLossRate()));
                }
            }
        }

        return new ArrayList<>(dailyMap.values());
    }

    /**
     * 根据时间范围类型获取开始和结束时间
     *
     * <p>周：从今天 0 点起往前 7 个完整自然日（不含当天）；月 / 年：上周期对应日 0 点至今天 0 点（月用 minusMonths(1)，年用 minusYears(1)）。</p>
     *
     * @param timeRange 时间范围类型：WEEK / MONTH / YEAR
     *
     * @return [开始时间，结束时间]
     */
    private String[] getTimeRange(String timeRange) {
        LocalDate today = LocalDate.now();
        LocalDateTime startTime;
        LocalDateTime endTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if ("WEEK".equalsIgnoreCase(timeRange)) {
            // 从今天 0 点起往前 7 个完整自然日（不含当天）：含 D-7 日整天 ~ D-1 日整天
            LocalDate startDate = today.minusDays(7);
            LocalDate endDate = today.minusDays(1);
            startTime = startDate.atStartOfDay();
            endTime = endDate.atTime(23, 59, 59);
        } else if ("YEAR".equalsIgnoreCase(timeRange)) {
            // YEAR：去年「今天」0 点 ~ 今天 0 点（闰年等由 minusYears 处理）
            startTime = today.minusYears(1).atStartOfDay();
            endTime = today.atStartOfDay();
        } else {
            // MONTH：上月「今天」0 点 ~ 今天 0 点（如 3/12 则 2/12 00:00:00 ~ 3/12 00:00:00；3/31 对应 2 月最后一天由 minusMonths 处理）
            startTime = today.minusMonths(1).atStartOfDay();
            endTime = today.atStartOfDay();
        }

        return new String[]{ startTime.format(formatter), endTime.format(formatter) };
    }

    /**
     * 获取每条线路对应的线损报警规则配置。
     *
     * <p>
     * 会从规则主表与“规则-线路”映射表中读取数据，最终整理为：{@code lineId -> LineLossAlarmRuleVO}。
     * </p>
     *
     * @return 线路ID到报警规则VO的映射；无配置时返回空 Map
     */
    private Map<Long, LineLossAlarmRuleVO> getAllLineRuleMap() {
        var lineRuleMap = new HashMap<Long, LineLossAlarmRuleVO>();
        // 先查“规则-线路”映射；如果为空，说明没有任何线路配置直接返回空 Map
        var lineLossAlarmRuleLineList = lineLossAlarmRuleLineMapper.selectList(Wrappers.emptyWrapper());
        if (CollectionUtils.isEmpty(lineLossAlarmRuleLineList)) {
            return lineRuleMap;
        }
        var ruleMap = new HashMap<Long, LineLossAlarmRuleVO>();
        // 再查报警规则主表，并按 alarmRuleId 构建 VO
        var lineLossAlarmRuleList = lineLossAlarmRuleMapper.selectList(Wrappers.<LineLossAlarmRule>lambdaQuery()
                                                                               .eq(LineLossAlarmRule::getEnabled, true));
        lineLossAlarmRuleList.forEach(item -> {
            var ruleVo = new LineLossAlarmRuleVO();
            ruleVo.covertLineLossBasicVO(item);
            ruleMap.put(item.getId(), ruleVo);
        });

        // 最后把规则 VO 按映射关系挂到对应 lineId 上
        lineLossAlarmRuleLineList.forEach(item -> {
            if (ruleMap.containsKey(item.getAlarmRuleId())) {
                lineRuleMap.put(item.getLineId(), ruleMap.get(item.getAlarmRuleId()));
            }
        });
        return lineRuleMap;
    }

    /**
     * 获取所有设备
     *
     * @param lineLossNodeList       节点集合
     * @param lineLossConnectionList 线路关系集合
     *
     * @return 设备map
     */
    private Map<Long, DeviceBaseArchivesInfoDTO> getSupplyAllDeviceMap(List<LineLossNode> lineLossNodeList,
                                                                       List<LineLossConnection> lineLossConnectionList
                                                                      ) {
        var deviceIdSet = new HashSet<Long>();
        Optional.ofNullable(lineLossNodeList).orElse(CollectionUtils.emptyList())
                .forEach(node -> StringSplitIdUtil.handlerIdsAddSet(node.getDeviceIds(), deviceIdSet));
        Optional.ofNullable(lineLossConnectionList).orElse(CollectionUtils.emptyList()).forEach(connection -> {
            if (connection.getFromDeviceId() != null) {
                deviceIdSet.add(connection.getFromDeviceId());
            }
            if (connection.getToDeviceId() != null) {
                deviceIdSet.add(connection.getToDeviceId());
            }
        });
        var devicePostByIdSetParam = new DevicePostByIdSetParam();
        devicePostByIdSetParam.setDeviceIdSet(deviceIdSet);
        var deviceList = deviceCenterStdClient.postListByDeviceId(devicePostByIdSetParam);
        return deviceList.stream().collect(Collectors.toMap(DeviceBaseArchivesInfoDTO::getId, device -> device, (o, n) -> n));
    }

    /**
     * 处理节点关系变更
     *
     * @param nodeList 线路关系
     * @param changeVO 变更数据
     */
    private void handlerChangeNode(List<ElectricityNodeVO> nodeList, ElectricitySupplyChangeVO changeVO) {
        // 转换并保存节点（注意需要分开新增的，删除的和修改的）
        var oldNodeIdSet = lineLossNodeService.list().stream().map(LineLossNode::getId).collect(Collectors.toSet());
        var newNodeIdSet = new HashSet<Long>();
        var lineLossNodeList = nodeList.stream()
                .map(n -> {
                    newNodeIdSet.add(n.getId());
                    return this.convertToLineLossNode(n);
                })
                .collect(Collectors.toList());
        // A 有但 B 无（A - B）
        var delIdSet = new HashSet<>(oldNodeIdSet);
        delIdSet.removeAll(newNodeIdSet);
        // B 有但 A 无（B - A）
        var insIdSet = new HashSet<>(oldNodeIdSet);
        insIdSet.removeAll(newNodeIdSet);

        changeVO.setConnectionInserIdList(new ArrayList<>(insIdSet));
        changeVO.setConnectionDeleteIdList(new ArrayList<>(delIdSet));
        changeVO.setNodeInsertList(lineLossNodeList);
    }

    /**
     * 处理连接关系变更
     *
     * @param connectionList 线路关系
     * @param changeVO       变更数据
     */
    private void handlerChangeConnection(List<ElectricityConnectionVO> connectionList, ElectricitySupplyChangeVO changeVO,
                                         Map<Long, Long> nodeDeviceIdMap
                                        ) {
        // 转换并保存连接关系（注意需要分开新增的，删除的和修改的）
        var oldConnectionMap = new HashMap<Long, LineLossConnection>();
        var oldConnectionIdSet = lineLossConnectionService.list().stream().map(c -> {
            oldConnectionMap.put(c.getId(), c);
            return c.getId();
        }).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(connectionList)) {
            return;
        }
        var newConnectionIdSet = new HashSet<Long>();
        var lineLossConnectionMap = connectionList.stream()
                .map(c -> {
                    newConnectionIdSet.add(c.getId());
                    return this.convertToLineLossConnection(c, nodeDeviceIdMap);
                })
                .collect(Collectors.toMap(LineLossConnection::getId, c -> c, (c1, c2) -> c1));
        // 1. 相同数据（交集）
        var updIdSet = new HashSet<>(oldConnectionIdSet);
        updIdSet.retainAll(newConnectionIdSet);  // 保留 A ∩ B
        // 2. A 有但 B 无（A - B）
        var delIdSet = new HashSet<>(oldConnectionIdSet);
        delIdSet.removeAll(newConnectionIdSet);        // 移除 B 中存在的
        // 3. B 有但 A 无（B - A）
        var insIdSet = new HashSet<>(newConnectionIdSet);
        insIdSet.removeAll(oldConnectionIdSet);        // 移除 A 中存在的

        changeVO.setConnectionInserIdList(new ArrayList<>(insIdSet));
        changeVO.setConnectionDeleteIdList(new ArrayList<>(delIdSet));

        if (CollectionUtils.isNotEmpty(insIdSet)) {
            var insLit = insIdSet.stream()
                    .map(lineLossConnectionMap::get)
                    .collect(Collectors.toList());
            changeVO.setConnectionInsertList(insLit);
        }

        if (CollectionUtils.isNotEmpty(updIdSet)) {
            var updList = new ArrayList<LineLossConnection>();
            updIdSet.forEach(id -> {
                var old = oldConnectionMap.get(id);
                var newConnection = lineLossConnectionMap.get(id);
                old.setLineName(newConnection.getLineName());
                old.setFromNodeId(newConnection.getFromNodeId());
                old.setToNodeId(newConnection.getToNodeId());
                old.setWebStyle(newConnection.getWebStyle());
                if (nodeDeviceIdMap.containsKey(newConnection.getFromNodeId()) &&
                    nodeDeviceIdMap.containsKey(newConnection.getToNodeId())) {
                    old.setFromDeviceId(nodeDeviceIdMap.get(newConnection.getFromNodeId()));
                    old.setToDeviceId(nodeDeviceIdMap.get(newConnection.getToNodeId()));
                } else {
                    old.setFromDeviceId(null);
                    old.setToDeviceId(null);
                }
                old.setUpdateBy(SecurityContextHolder.getPrincipal().getAccount());
                old.setUpdateTime(LocalDateTime.now());
                updList.add(old);
            });
            changeVO.setConnectionUpdateList(updList);
        }
    }

    /**
     * 转换供电节点为线损节点
     */
    private LineLossNode convertToLineLossNode(ElectricityNodeVO nodeVO) {
        LineLossNode lineLossNode = new LineLossNode();
        lineLossNode.setId(nodeVO.getId());
        lineLossNode.setCode(nodeVO.getCode());
        lineLossNode.setName(nodeVO.getName());
        lineLossNode.setType(nodeVO.getType());
        lineLossNode.setDeviceIds(nodeVO.getDeviceIds());
        lineLossNode.setChildrenIds(nodeVO.getChildrenIds());
        lineLossNode.setLocation(nodeVO.getLocation());
        lineLossNode.setDescription(nodeVO.getDescription());
        lineLossNode.setWebStyle(nodeVO.getWebStyle());
        lineLossNode.setCreateTime(LocalDateTime.now());
        lineLossNode.setUpdateTime(LocalDateTime.now());
        return lineLossNode;
    }

    /**
     * 转换供电连接为线损连接
     */
    private LineLossConnection convertToLineLossConnection(ElectricityConnectionVO connectionVO, Map<Long, Long> nodeDeviceIdMap) {
        LineLossConnection lineLossConnection = new LineLossConnection();
        lineLossConnection.setId(connectionVO.getId());
        lineLossConnection.setLineName(connectionVO.getLineName());
        lineLossConnection.setFromNodeId(connectionVO.getFromNodeId());
        lineLossConnection.setToNodeId(connectionVO.getToNodeId());
        if (nodeDeviceIdMap.containsKey(connectionVO.getFromNodeId()) && nodeDeviceIdMap.containsKey(connectionVO.getToNodeId())) {
            lineLossConnection.setFromDeviceId(nodeDeviceIdMap.get(connectionVO.getFromNodeId()));
            lineLossConnection.setToDeviceId(nodeDeviceIdMap.get(connectionVO.getToNodeId()));
        } else {
            lineLossConnection.setFromDeviceId(null);
            lineLossConnection.setToDeviceId(null);
        }
        lineLossConnection.setWebStyle(connectionVO.getWebStyle());
        lineLossConnection.setCreateTime(LocalDateTime.now());
        lineLossConnection.setUpdateTime(LocalDateTime.now());
        return lineLossConnection;
    }
}
