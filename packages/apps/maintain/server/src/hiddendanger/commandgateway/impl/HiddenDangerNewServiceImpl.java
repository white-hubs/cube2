package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.util.CustomCellWriteUtil;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.base.flow.client.dto.PageOfDataBoardDTO;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.ResourceEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.TicketStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.OrderHomePageVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.DateUnitUtils;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerNewService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao.HiddenDangerNewMapper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dto.HiddenDangerNewDTO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration.HiddenDangerResultEnum;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration.HiddenDangerStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.dto.HiddendangerWordOrderStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param.HiddenBatchDeleteCommand;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param.HiddenDangerProInsParam;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param.HiddenTaskStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.param.HiddenWorkOrderStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HeadquartersDataGatherVo;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerHandleVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerNewVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerNumStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerReportVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerTypeStatistics;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerTypeStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenDangerVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HiddenTaskStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerLevel;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerNew;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerType;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.HiddenDangerLevelQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.HiddenDangerTypeQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.query.HiddenDangerTypeCollectionQuery;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.rocketmq.message.HiddenDangerPatrolMessage;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.rocketmq.produce.HiddenDangerEventProduce;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DeviceStatisticParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import instance.command.ProcessStartedMessageAttrCommand;
import instance.event.NodePassMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 隐患排查2.0
 * @date 2023/11/8 20:40
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerNewServiceImpl extends ServiceImpl<HiddenDangerNewMapper, HiddenDangerNew> implements HiddenDangerNewService {

    private final ProcessInstanceClient processInstanceClient;
    private final HiddenDangerTypeQueryService hiddenDangerTypeQueryService;
    private final HiddenDangerLevelQueryService hiddenDangerLevelQueryService;
    private final HiddenDangerNewMapper hiddenDangerNewMapper;
    private final FlowClient flowClient;

    @Override
    public Pagination<HiddenDangerNewVO> personalPage(ProcessInstancePageQuery processInstancePageQuery) {
        Pagination<ProcessInstanceDTO> page = processInstanceClient.page(processInstancePageQuery);
        return PagingUtils.convert(page, coverPage(page));
    }

    private List<HiddenDangerNewVO> coverPage(Pagination<ProcessInstanceDTO> page) {
        List<HiddenDangerNewVO> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(record -> {
                HiddenDangerNewVO vo = new HiddenDangerNewVO();
                BeanUtils.copyProperties(record, vo);
                List<HiddenDangerNew> list = this.lambdaQuery().eq(HiddenDangerNew::getProcessInsId, record.getProcessInstanceId()).list();
                HiddenDangerNew hiddenDangerNew;
                if (ObjectUtils.isNotEmpty(list)) {
                    hiddenDangerNew = list.getFirst();
                } else {
                    //存在流引擎有而隐患排查这边没有的数据需要向隐患排查中补数据
                    ProcessStartedMessageAttrCommand message = processInstanceClient.getProcessInstanceFormInfo(record.getProcessInstanceId());
                    hiddenDangerNew = addHiddenDangerNew(message);
                }
                setListField(vo, hiddenDangerNew);
                vos.add(vo);
            });
        }
        return vos;
    }

    @Override
    public HiddenDangerNew addHiddenDangerNew(ProcessStartedMessageAttrCommand message) {
        HiddenDangerNew hiddenDangerNew = null;
        if (ObjectUtils.isNotEmpty(message) && ObjectUtils.isNotEmpty(message.getData())) {
            List<HiddenDangerNew> list = this.lambdaQuery().eq(HiddenDangerNew::getProcessInsId, message.getProcessInstanceId()).list();
            HiddenDangerNewDTO hiddenDangerNewDTO = null;
            try {
                hiddenDangerNewDTO = JSONObject.parseObject(message.getData(), HiddenDangerNewDTO.class);
            } catch (Exception e) {
                log.error("json parse error", e);
            }
            if (Objects.isNull(hiddenDangerNewDTO)) {
                return hiddenDangerNew;
            }
            HiddenDangerNewDTO.HiddenObj objInfoDTO =
                    Objects.nonNull(hiddenDangerNewDTO.getHiddenObj()) ? hiddenDangerNewDTO.getHiddenObj()
                                                                       : new HiddenDangerNewDTO.HiddenObj();
            if (ObjectUtils.isEmpty(list)) {
                //查询编号尾数
                var now = LocalDateTime.now();
                long count = this.lambdaQuery().between(HiddenDangerNew::getCreateTime, DateUtils.beginOfDay(now), DateUtils.endOfDay(now))
                                     .count() + 1;
                String countCode = count > 99 ? String.valueOf(count) : count > 9 ? "0" + count : "00" + count;
                final String resource = (String) message.getProcessVariables().get("resource");
                hiddenDangerNew = HiddenDangerNew.builder()
                        .code("YH" + DateUtils.format(now, "yyyyMMdd") + countCode)
                        .processInsId(message.getProcessInstanceId())
                        .name(message.getBusinessTypeName())
                        .submitStaff(message.getOutsiderName())
                        .submitTime(message.getStartTime())
                        .state(TicketStatusEnum.WAITING_CONFIRM.name())
                        .hiddenObjId(objInfoDTO.getId())
                        .remark(hiddenDangerNewDTO.getHiddenDescribe())
                        .hiddenObjName(
                                StringUtils.isBlank(objInfoDTO.getName()) ? hiddenDangerNewDTO.getCheckObj() : objInfoDTO.getName())
                        .hiddenObjType(hiddenDangerNewDTO.getObjType())
                        .hiddenTypeId(hiddenDangerNewDTO.getHiddenType())
                        .source(ObjectUtils.isEmpty(resource) ? ResourceEnum.MANUAL_SUBMISSION.getDesc()
                                                              : ResourceEnum.getDescription(resource))
                        .urgencyLevel(hiddenDangerNewDTO.getUrgencyLevel())
                        .result(StringUtils.isBlank(hiddenDangerNewDTO.getResult()) ? ""
                                                                                    : HiddenDangerResultEnum.getDescription(hiddenDangerNewDTO.getResult()))
                        .build();
                log.info("新增隐患排查单:{}", JSON.toJSONString(hiddenDangerNewDTO));
                this.save(hiddenDangerNew);
                sendHiddenEventMessage(message.getProcessInstanceId(), FlowResult.ACTIVE.getDesc());
            } else {
                hiddenDangerNew = list.get(0);
            }
        }
        return hiddenDangerNew;
    }

    @Override
    public List<HeadquartersDataGatherVo> headquartersDataGather(Period period) {
        List<HiddenDangerNew> list = this.baseMapper.selectHiddenDangerNewList(period, SessionContextHolder.getRequester().getTenant());
        List<HeadquartersDataGatherVo> vos = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(list)) {
            list.forEach(vo -> {
                List<HiddenDangerType> hiddenDangerTypes = hiddenDangerTypeQueryService.listHiddenDangerType(HiddenDangerTypeCollectionQuery
                                                                                                                     .builder()
                                                                                                                     .hiddenDangerTypeId(vo.getHiddenTypeId())
                                                                                                                     .build());
                List<HiddenDangerLevel> levels = hiddenDangerLevelQueryService.lambdaQuery()
                        .eq(HiddenDangerLevel::getId, vo.getUrgencyLevel()).list();
                vos.add(HeadquartersDataGatherVo.builder()
                                .id(vo.getId())
                                .name(vo.getName())
                                .hiddenDangerCategory(ObjectUtils.isEmpty(hiddenDangerTypes) ? "" : hiddenDangerTypes.get(0).getName())
                                .hiddenDangerStatus(vo.getState())
                                .levelName(ObjectUtils.isEmpty(levels) ? "" : levels.get(0).getName())
                                .remark(vo.getRemark())
                                .tenant(vo.getTenant())
                                .deleted(vo.getDeleted())
                                .createBy(vo.getCreateBy())
                                .createTime(vo.getSubmitTime())
                                .updateBy(vo.getUpdateBy())
                                .updateTime(vo.getUpdateTime())
                                .build());
            });
        }
        return vos;
    }

    @Override
    public HiddenTaskStatisticsVO HiddenTaskStatistics(HiddenTaskStatisticsParam statisticsParam) {
        List<HiddenDangerNew> list = this.lambdaQuery()
                .between(ObjectUtils.isNotEmpty(statisticsParam.getPeriod()), HiddenDangerNew::getSubmitTime,
                         ObjectUtils.ifNull(statisticsParam.getPeriod(), Period::getBegin),
                         ObjectUtils.ifNull(statisticsParam.getPeriod(), Period::getEnd))
                .isNotNull(HiddenDangerNew::getHiddenTypeId)
                .select(HiddenDangerNew::getUrgencyLevel, HiddenDangerNew::getHiddenTypeId).list();
        HiddenTaskStatisticsVO statisticsVO = new HiddenTaskStatisticsVO();
        if (ObjectUtils.isNotEmpty(list)) {
            Map<Long, Long> map = list.stream().collect(Collectors.groupingBy(HiddenDangerNew::getHiddenTypeId, Collectors.counting()));
            Map<Long, Long> hiddenTypeMap = map;
            List<HiddenTaskStatisticsVO.HiddenStatistic> hiddenLevel = new ArrayList<>();
            map.keySet().forEach(key -> {
                HiddenDangerType type = hiddenDangerTypeQueryService.lambdaQuery().eq(HiddenDangerType::getId, key).one();
                if (Objects.nonNull(type)) {
                    hiddenLevel.add(HiddenTaskStatisticsVO.HiddenStatistic.builder()
                                            .ratio(BigDecimal.valueOf(hiddenTypeMap.get(key)).divide(BigDecimal.valueOf(list.size()), 4,
                                                                                                     RoundingMode.HALF_UP)
                                                           .multiply(BigDecimal.valueOf(100L)).doubleValue())
                                            .name(type.getName())
                                            .num(hiddenTypeMap.get(key))
                                            .build());
                }
            });
            statisticsVO.setHiddenLevel(hiddenLevel);

            map = list.stream().filter(t -> Objects.nonNull(t.getUrgencyLevel()))
                    .collect(Collectors.groupingBy(HiddenDangerNew::getUrgencyLevel, Collectors.counting()));
            Map<Long, Long> urgencyLevelMap = map;
            List<HiddenTaskStatisticsVO.HiddenStatistic> hiddenClassify = new ArrayList<>();
            map.keySet().forEach(key -> {
                HiddenDangerLevel level = hiddenDangerLevelQueryService.lambdaQuery().eq(HiddenDangerLevel::getId, key).one();
                hiddenClassify.add(HiddenTaskStatisticsVO.HiddenStatistic.builder()
                                           .ratio(BigDecimal.valueOf(urgencyLevelMap.get(key)).divide(BigDecimal.valueOf(list.size()), 4,
                                                                                                      RoundingMode.HALF_UP)
                                                          .multiply(BigDecimal.valueOf(100L)).doubleValue())
                                           .name(level.getName())
                                           .num(urgencyLevelMap.get(key))
                                           .build());
            });
            statisticsVO.setHiddenClassify(hiddenClassify);
        }
        return statisticsVO;
    }

    @Override
    public List<Boolean> getCountHiddenRiskIndex(Period period) {
        if (ObjectUtils.isNull(period)) {
            var lastWeek = DateUtils.lastWeek();
            var beginOfWeek = DateUtils.beginOfWeek(lastWeek);
            var endOfWeek = DateUtils.endOfWeek(lastWeek);
            period = new Period(beginOfWeek, endOfWeek);
        }
        List<Boolean> booleans = new ArrayList<>();
        List<HiddenDangerNew> list = this.lambdaQuery().between(ObjectUtils.isNotEmpty(period), HiddenDangerNew::getSubmitTime,
                                                                ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                .select(HiddenDangerNew::getResponse).list();
        if (ObjectUtils.isNotEmpty(list)) {
            booleans = list.stream().map(HiddenDangerNew::getResponse).collect(Collectors.toList());
        }
        return booleans;
    }

    @Override
    public List<DeviceOperationStatisticsDTO> deviceHiddenNumStatistics(DeviceStatisticParam param) {
        if (ObjectUtils.isNotEmpty(param.getDeviceIds())) {
            return hiddenDangerNewMapper.deviceHiddenNumStatistics(param);
        }
        return new ArrayList<>();
    }

    @Override
    public HiddenDangerNumStatisticsVO hiddenDangerNumStatistics(Period period) {
        List<HiddenDangerNew> list = this.lambdaQuery().between(HiddenDangerNew::getSubmitTime, period.getBegin(), period.getEnd())
                .select(HiddenDangerNew::getId, HiddenDangerNew::getState).list();
        if (ObjectUtils.isEmpty(list)) {
            return HiddenDangerNumStatisticsVO.builder().total(0).finished(0).inHand(0).build();
        }
        int finished = (int) list.stream().filter(po -> HiddenDangerStatusEnum.FINISHED.name().equals(po.getState())).count();
        return HiddenDangerNumStatisticsVO.builder()
                .total(list.size())
                .finished(finished)
                .inHand(list.size() - finished)
                .build();
    }

    @Override
    public HiddenDangerTypeStatisticsVO hiddenDangerTypeStatistics(Period period) {
        long total = this.lambdaQuery().between(HiddenDangerNew::getSubmitTime, period.getBegin(), period.getEnd()).count();
        List<HiddenDangerTypeStatistics> vos = hiddenDangerNewMapper.hiddenDangerTypeStatistics(period);
        HiddenDangerTypeStatisticsVO statisticsVO = new HiddenDangerTypeStatisticsVO();
        statisticsVO.setTotalNum(total);
        if (ObjectUtils.isNotEmpty(vos)) {
            List<HiddenDangerTypeStatistics> statistics = new ArrayList<>();
            int countTotal = 0;
            for (int i = 0; i < 3; i++) {
                if (vos.size() > i) {
                    HiddenDangerTypeStatistics vo = vos.get(i);
                    vo.setProportion(MathUtils.calculateProportionPercentage(vo.getNum(), total).floatValue());
                    statistics.add(vo);
                    countTotal += vo.getNum();
                }
            }
            if (vos.size() > 3) {
                statistics.add(HiddenDangerTypeStatistics.builder()
                                       .num(total - countTotal)
                                       .name("其他")
                                       .proportion(MathUtils.calculateProportionPercentage(total - countTotal, total).floatValue())
                                       .build());
            }
            statisticsVO.setTypeStatisticsList(statistics);
        }
        return statisticsVO;
    }

    @Override
    public Float hiddenDangerHandleRatio(Period period) {
        List<HiddenDangerNew> list = this.lambdaQuery().between(HiddenDangerNew::getSubmitTime, period.getBegin(), period.getEnd())
                .select(HiddenDangerNew::getId, HiddenDangerNew::getState).list();
        if (ObjectUtils.isEmpty(list)) {
            return 100f;
        }
        long finished = list.stream().filter(po -> HiddenDangerStatusEnum.FINISHED.name().equals(po.getState())).count();
        return MathUtils.calculateProportionPercentage(finished, list.size()).floatValue();
    }

    @Override
    public Set<String> objHiddenOrderStatistics(DeviceStatisticParam param) {
        if (ObjectUtils.isNotEmpty(param.getDeviceIds())) {
            List<HiddenDangerNew> list = this.lambdaQuery()
                    .between(ObjectUtils.isNotEmpty(param.getPeriod()), HiddenDangerNew::getSubmitTime,
                             ObjectUtils.ifNull(param.getPeriod(), Period::getBegin), ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                    .in(ObjectUtils.isNotEmpty(param.getDeviceIds()), HiddenDangerNew::getHiddenObjId, param.getDeviceIds())
                    .select(HiddenDangerNew::getProcessInsId).list();
            if (ObjectUtils.isNotEmpty(list)) {
                return list.stream().map(HiddenDangerNew::getProcessInsId).collect(Collectors.toSet());
            }
        }
        return new HashSet<>();
    }

    @Override
    public HiddenDangerHandleVO hiddenDangerForSafetyReport(Period period) {
        List<HiddenDangerReportVO> hiddenDangerReportVOS = hiddenDangerNewMapper.selectHiddenDangerForSafetyReport(period);
        Long nowNum = 0L;
        if (CollectionUtils.isNotEmpty(hiddenDangerReportVOS)) {
            nowNum = hiddenDangerReportVOS.stream()
                    .map(HiddenDangerReportVO::getNum).reduce(Long::sum).get();
        }
        //环比
        Period month = DateUnitUtils.getPreviousPeriod("THIS_MONTH", period);
        Long ringNum = hiddenDangerNewMapper.selectHiddenDangerTotal(month);
        //同比
        Period samePeriod = DateUnitUtils.getSamePeriod(period);
        Long sameNum = hiddenDangerNewMapper.selectHiddenDangerTotal(samePeriod);

        return HiddenDangerHandleVO.builder().num(nowNum)
                .ringNum(ringNum)
                .sameNum(sameNum)
                .hiddenDangerReportVOLis(hiddenDangerReportVOS)
                .build();
    }

    @Override
    public OrderHomePageVO objHiddenOrderHomePageStatistics(BaseDateStatisticsParam param) {
        Assert.parameterIsNull(param.getPeriod(), "请选则时间范围");
        //总数
        Long nowNum = this.lambdaQuery()
                .between(HiddenDangerNew::getSubmitTime, param.getPeriod().getBegin(),
                         param.getPeriod().getEnd()).count();
        //环比
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod());
        Long ringNum = this.lambdaQuery()
                .between(HiddenDangerNew::getSubmitTime, previousPeriod.getBegin(),
                         previousPeriod.getEnd())
                .count();
        //完成数
        Long finishedNum = this.lambdaQuery()
                .between(HiddenDangerNew::getSubmitTime, param.getPeriod().getBegin(),
                         param.getPeriod().getEnd())
                .eq(HiddenDangerNew::getState, HiddenDangerStatusEnum.FINISHED.name())
                .count();
        //处理中
        Long processCount = this.lambdaQuery()
                .between(HiddenDangerNew::getSubmitTime, param.getPeriod().getBegin(),
                         param.getPeriod().getEnd())
                .ne(HiddenDangerNew::getState, HiddenDangerStatusEnum.FINISHED.name())
                .count();
        return OrderHomePageVO.builder()
                .orderCount(nowNum)
                .orderRatio(MathUtils.calculateRingRatioPercentage(nowNum, ringNum).floatValue())
                .processOrder(processCount)
                .finishOrder(finishedNum)
                .build();
    }

    @Override
    public Pagination<HiddendangerWordOrderStatisticsDTO> hiddendangerWorkOrderStatistics(HiddenWorkOrderStatisticsParam param) {
        List<HiddendangerWordOrderStatisticsDTO> records = this.getFaultTypeStatisticsExtend(param);
        if (ObjectUtils.isNull(records)) {
            return PagingUtils.convert(new Page<>(param.getPageNumber(), param.getPageSize()));
        }
        Page<HiddendangerWordOrderStatisticsDTO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page.setTotal(records.size());
        Integer maxSize = param.getPageNumber() * param.getPageSize();
        Integer minSize = param.getPageSize() * (param.getPageNumber() - 1);
        if (records.size() > minSize) {
            page.setRecords(records.subList(minSize, records.size() < maxSize ? records.size() : maxSize));
        }
        return PagingUtils.convert(page);
    }

    @Override
    public void hiddendangerWorkOrderStatisticsExcel(HiddenWorkOrderStatisticsParam param) {
        List<HiddendangerWordOrderStatisticsDTO> records = this.getFaultTypeStatisticsExtend(param);
        Assert.parameterIsNull(records, "暂无数据，无法导出");
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String excelName = "";
        try {
            excelName = URLEncoder.encode("隐患排查工单统计", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
            EasyExcel.write(response.getOutputStream(), HiddendangerWordOrderStatisticsDTO.class)
                    .registerWriteHandler(new CustomCellWriteUtil())
                    .sheet("隐患排查工单统计")
                    .doWrite(records);
        } catch (Exception e) {
            log.debug("maintain:隐患排查工单统计导出异常：", e);
            throw new ServiceException("下载异常");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteHidden(HiddenBatchDeleteCommand deleteCommand) {
        if (CollectionUtils.isEmpty(deleteCommand.getProcessInstanceIds())) {
            return;
        }
        List<HiddenDangerNew> list = this.lambdaQuery().in(HiddenDangerNew::getProcessInsId, deleteCommand.getProcessInstanceIds()).list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        DatabaseContextHolder.softDelete(HiddenDangerNew.class, list);
        flowClient.deleteFormFlow(deleteCommand.getProcessInstanceIds());
    }

    @Override
    public List<HiddendangerWordOrderStatisticsDTO> hiddendangerWorkOrderStatisticsAll(HiddenWorkOrderStatisticsParam param) {
        return this.getFaultTypeStatisticsExtend(param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHiddenDanger(List<String> processInstanceIdList) {
        long timeMillis = System.currentTimeMillis();
        //删除流程实例
        this.baseMapper.update(null, Wrappers.<HiddenDangerNew>lambdaUpdate()
                                       .set(HiddenDangerNew::getDeleted, timeMillis)
                                       .set(HiddenDangerNew::getUpdateTime, LocalDateTime.now())
                                       .in(HiddenDangerNew::getProcessInsId, processInstanceIdList)
                              );
    }

    private void sendHiddenEventMessage(String processInstanceId, String name) {
        HiddenDangerNew hiddenDangerNew = this.lambdaQuery().eq(HiddenDangerNew::getProcessInsId, processInstanceId).last("limit 1").one();
        HiddenDangerVO eventVO = ObjectUtils.copying(hiddenDangerNew, HiddenDangerVO.class);
        eventVO.setHiddenDangerStatus(name);
        if (eventVO.getHiddenObjId() != null) {
            HiddenDangerEventProduce.sendHiddenDangerEvent(eventVO);
        }
    }

    private void setListField(HiddenDangerNewVO vo, HiddenDangerNew hiddenDangerNew) {
        if (ObjectUtils.isNotEmpty(hiddenDangerNew)) {
            vo.setCode(hiddenDangerNew.getCode());
            vo.setName(hiddenDangerNew.getName());
            vo.setHiddenObjName(hiddenDangerNew.getHiddenObjName());
            if (Objects.nonNull(hiddenDangerNew.getHiddenTypeId())) {
                final HiddenDangerTypeCollectionQuery query = new HiddenDangerTypeCollectionQuery();
                query.setHiddenDangerTypeId(hiddenDangerNew.getHiddenTypeId());
                final List<HiddenDangerType> hiddenDangerTypes = hiddenDangerTypeQueryService.listHiddenDangerType(query);
                if (ObjectUtils.isNotEmpty(hiddenDangerTypes)) {
                    vo.setHiddenType(hiddenDangerTypes.get(0).getName());
                }
            }
            vo.setSource(hiddenDangerNew.getSource());
            vo.setResult(hiddenDangerNew.getResult());
            final HiddenDangerLevel byId = hiddenDangerLevelQueryService.getById(hiddenDangerNew.getUrgencyLevel());
            vo.setUrgencyLevel(Objects.nonNull(byId) ? byId.getName() : "");
            vo.setActiveNodeName(StringUtils.isBlank(vo.getActiveNodeName()) ? "完成" : vo.getActiveNodeName());
        }
    }

    @Override
    public Pagination<HiddenDangerNewVO> allPage(HiddenDangerProInsParam processInstancePageQuery) {
        if (Objects.nonNull(processInstancePageQuery.getHiddenObjId())) {
            List<String> processInsIdList = Optional.ofNullable(this.lambdaQuery()
                                                                        .select(HiddenDangerNew::getProcessInsId)
                                                                        .eq(HiddenDangerNew::getHiddenObjId, processInstancePageQuery.getHiddenObjId())
                                                                        .list()).orElse(new ArrayList<>())
                    .stream().filter(t -> StringUtils.isNotBlank(t.getProcessInsId()))
                    .map(HiddenDangerNew::getProcessInsId)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(processInsIdList)) {
                return new Pagination<>();
            }
            processInstancePageQuery.setProcessInstanceIds(String.join(",", processInsIdList));
        }
        Pagination<ProcessInstanceDTO> page = processInstanceClient.allPage(processInstancePageQuery);
        return PagingUtils.convert(page, coverPage(page));
    }

    @Override
    public Pagination<HiddenDangerNewVO> backlog(PageOfDataBoardDTO pageOfDataBoardDTO) {
        final Pagination<ProcessInstanceDTO> page = processInstanceClient.pageOfDataBoard(pageOfDataBoardDTO);
        return PagingUtils.convert(page, coverPage(page));
    }

    @Override
    public void updateHiddenDanger(NodePassMessage message) {
        final HiddenDangerNewDTO hiddenDangerNewDTO = JSONObject.parseObject(message.getData(), HiddenDangerNewDTO.class);
        log.info("更新隐患排查,确认结果:{},解决结果:{}", hiddenDangerNewDTO.getConfirm(), hiddenDangerNewDTO.getResult());
        String state = StringUtils.isNotBlank(hiddenDangerNewDTO.getConfirm()) ? TicketStatusEnum.WAITING_FOR_HANDLE.name() : "";
        state = StringUtils.isNotBlank(hiddenDangerNewDTO.getResult()) ? TicketStatusEnum.TO_BE_CLOSED.name() : state;
        setIsResponse(message.getProcessInstanceId());
        List<HiddenDangerNew> list = this.lambdaQuery().eq(HiddenDangerNew::getProcessInsId, message.getProcessInstanceId())
                .select(HiddenDangerNew::getState).list();
        if (StringUtils.isNotBlank(state) && ObjectUtils.isNotEmpty(list) &&
            !TicketStatusEnum.FINISHED.name().equals(list.get(0).getState())) {
            HiddenDangerNewDTO.HiddenObj objInfoDTO =
                    Objects.nonNull(hiddenDangerNewDTO.getHiddenObj()) ? hiddenDangerNewDTO.getHiddenObj()
                                                                       : new HiddenDangerNewDTO.HiddenObj();
            this.lambdaUpdate().set(HiddenDangerNew::getState, state)
                    .set(Objects.nonNull(objInfoDTO.getId()), HiddenDangerNew::getHiddenObjId, objInfoDTO.getId())
                    .set(StringUtils.isNotBlank(objInfoDTO.getName()), HiddenDangerNew::getHiddenObjName, objInfoDTO.getName())
                    .set(Objects.nonNull(hiddenDangerNewDTO.getHiddenType()), HiddenDangerNew::getHiddenTypeId, hiddenDangerNewDTO.getHiddenType())
                    .set(Objects.nonNull(hiddenDangerNewDTO.getUrgencyLevel()), HiddenDangerNew::getUrgencyLevel, hiddenDangerNewDTO.getUrgencyLevel())
                    .set(StringUtils.isNotBlank(hiddenDangerNewDTO.getResult()), HiddenDangerNew::getResult, HiddenDangerResultEnum.getDescription(hiddenDangerNewDTO.getResult()))
                    .eq(HiddenDangerNew::getProcessInsId, message.getProcessInstanceId()).update();
        }
    }

    /**
     * @Author wql
     * @Description 更新隐患响应状态
     * @Date 17:23 2023/11/16
     **/
    private void setIsResponse(String processInsId) {
        HiddenDangerNew dangerNew = this.lambdaQuery().eq(HiddenDangerNew::getProcessInsId, processInsId).one();
        if (ObjectUtils.isNotEmpty(dangerNew) && !dangerNew.getResponse()) {
            dangerNew.setResponse(true);
            this.updateById(dangerNew);
        }
    }

    @Override
    public void updateResult(String processInstanceId, String data, FlowResult result) {
        final HiddenDangerNewDTO hiddenDangerNewDTO = JSONObject.parseObject(data, HiddenDangerNewDTO.class);
        final boolean update = this.lambdaUpdate().set(HiddenDangerNew::getState, TicketStatusEnum.FINISHED.name())
                .set(HiddenDangerNew::getResult, HiddenDangerResultEnum.getDescription(hiddenDangerNewDTO.getResult()))
                .eq(HiddenDangerNew::getProcessInsId, processInstanceId).update();
        log.info("隐患排查流程审批结果更新结果:{},processInstanceId:{},result:{}", update, processInstanceId, result.getDesc());
        setIsResponse(processInstanceId);
        sendHiddenEventMessage(processInstanceId, result.getDesc());
        sendHiddenCompleteMessage(processInstanceId);
    }

    /**
     * @Author wql
     * @Description 发送隐患处理结束消息
     * @Date 11:48 2023/11/14
     **/
    private void sendHiddenCompleteMessage(String processInstanceId) {
        List<String> objects = new ArrayList<>();
        objects.add(processInstanceId);
        HiddenDangerPatrolMessage hiddenDangerPatrolMessage = new HiddenDangerPatrolMessage();
        hiddenDangerPatrolMessage.setHiddenDangerIds(objects);
        RocketMQContextHolder.send(HiddenDangerPatrolMessage.TOPIC, hiddenDangerPatrolMessage);
    }

    /**
     * 工单数据扩展
     */
    private List<HiddendangerWordOrderStatisticsDTO> getFaultTypeStatisticsExtend(HiddenWorkOrderStatisticsParam param) {
        var begin = ObjectUtils.isNotEmpty(param.getPeriod()) && ObjectUtils.isNotEmpty(param.getPeriod().getBegin()) ? param.getPeriod()
                .getBegin() : null;
        var end =
                ObjectUtils.isNotEmpty(param.getPeriod()) && ObjectUtils.isNotEmpty(param.getPeriod().getEnd()) ? param.getPeriod().getEnd()
                                                                                                                : null;
        List<HiddenDangerNew> hiddenDangerNewList = this.hiddenDangerNewMapper.selectList(new LambdaQueryWrapper<HiddenDangerNew>()
                                                                                                  .isNotNull(HiddenDangerNew::getHiddenTypeId)
                                                                                                  .in(ObjectUtils.isNotEmpty(param.getHiddenDangerTypeId()), HiddenDangerNew::getHiddenTypeId, param.getHiddenDangerTypeId())
                                                                                                  .between(ObjectUtils.isNotEmpty(begin) &&
                                                                                                           ObjectUtils.isNotEmpty(end), HiddenDangerNew::getSubmitTime, begin, end)
                                                                                                  .orderByAsc(HiddenDangerNew::getHiddenTypeId));
        if (ObjectUtils.isEmpty(hiddenDangerNewList)) {
            return null;
        }
        Integer total;
        List<ProcessInstanceDTO> data = new ArrayList<>();
        ProcessInstancePageQuery flowParam = new ProcessInstancePageQuery();
        flowParam.setPageSize(1000);
        flowParam.setPageNumber(0);
        flowParam.setApplicationCode(param.getApplicationCode());
        flowParam.setSubmitTime(param.getPeriod());
        Pagination<ProcessInstanceDTO> flowData = null;
        do {
            flowParam.setPageNumber(flowParam.getPageNumber() + 1);
            total = flowParam.getPageNumber() * flowParam.getPageSize();
            flowData = processInstanceClient.allPage(flowParam);
            data.addAll(flowData.getRecords());
        } while (flowData.getTotal() > total);
        List<HiddendangerWordOrderStatisticsDTO> records = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(data)) {
            HiddenDangerTypeCollectionQuery hiddenDangerTypeQuery = new HiddenDangerTypeCollectionQuery(null, param.getHiddenDangerTypeId());
            List<HiddenDangerType> hiddenDangerTypeList = hiddenDangerTypeQueryService.listHiddenDangerType(hiddenDangerTypeQuery);
            Map<Long, HiddenDangerType> hiddenTypeMap = hiddenDangerTypeList.stream()
                    .collect(Collectors.toMap(HiddenDangerType::getId, hiddenDangerType -> hiddenDangerType));
            Map<Long, List<HiddenDangerNew>> faultOrderMap = hiddenDangerNewList.stream()
                    .collect(Collectors.groupingBy(HiddenDangerNew::getHiddenTypeId));
            Map<String, ProcessInstanceDTO> processInstanceMap = data.stream()
                    .collect(Collectors.toMap(ProcessInstanceDTO::getProcessInstanceId, ProcessInstanceDTO -> ProcessInstanceDTO));
            for (Map.Entry<Long, List<HiddenDangerNew>> entry : faultOrderMap.entrySet()) {
                HiddendangerWordOrderStatisticsDTO dto = HiddendangerWordOrderStatisticsDTO.getHiddendangerWordOrderStatisticsDTO(hiddenTypeMap, entry
                        .getValue().getFirst().getHiddenTypeId());
                if (ObjectUtils.isEmpty(dto)) {
                    continue;
                }
                AtomicReference<Boolean> bool = new AtomicReference<>(false);
                entry.getValue().forEach(ex -> {
                    ProcessInstanceDTO processInstanceDTO = processInstanceMap.get(ex.getProcessInsId());
                    boolean flowResultBool =
                            !ObjectUtils.isNotEmpty(processInstanceDTO) || FlowResult.UNDO.name().equals(processInstanceDTO.getStatus()) ||
                            FlowResult.REJECT.name().equals(processInstanceDTO.getStatus());
                    if (!flowResultBool) {
                        bool.set(true);
                        dto.setTotalNumber(dto.getTotalNumber() + 1);
                        if (processInstanceDTO.getStatus().equals("FINISHED")) {
                            dto.setDealNumber(dto.getDealNumber() + 1);
                        } else {
                            dto.setWaitNumber(dto.getWaitNumber() + 1);
                        }
                        Long minute = DateUtils.between(processInstanceDTO.getCreateTime(), processInstanceDTO.getUpdateTime(), ChronoUnit.MINUTES);
                        dto.setTotalWaitTime(ObjectUtils.isNotEmpty(dto.getTotalWaitTime()) ? dto.getTotalWaitTime() + minute : minute);
                    }
                });
                if (ObjectUtils.isNotEmpty(bool) && bool.get()) {
                    dto.getExtend();
                    records.add(dto);
                }
            }
        }
        return records;
    }
}
