package com.guhecloud.cube.biz.door.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceBimSceneStdClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.door.server.dao.EventRecordMapper;
import com.guhecloud.cube.biz.door.server.entity.EventRecord;
import com.guhecloud.cube.biz.door.server.param.PassedNumParam;
import com.guhecloud.cube.biz.door.server.param.StatisticsParam;
import com.guhecloud.cube.biz.door.server.queryparam.EventRecordQueryParam;
import com.guhecloud.cube.biz.door.server.service.EventRecordService;
import com.guhecloud.cube.biz.door.server.vo.DoorEventsVo;
import com.guhecloud.cube.biz.door.server.vo.DoorStatisticsVo;
import com.guhecloud.cube.biz.door.server.vo.EventRecordVo;
import com.guhecloud.cube.biz.door.server.vo.RankingVo;
import com.guhecloud.cube.biz.door.server.vo.TimeTrendVo;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventRecordServiceImpl extends ServiceImpl<EventRecordMapper, EventRecord> implements EventRecordService {
    private final EventRecordMapper eventRecordMapper;
    private final DeviceBimSceneStdClient deviceBimSceneClient;
    private final SecurityClientHandlerServiceImpl securityClientHandlerService;
    private final FileClient fileClient;

    @Override
    public long passedTodayNum() {
        LocalDateTime date = DateUtils.now();
        return this.count(Wrappers.<EventRecord>lambdaQuery()
                                  .between(EventRecord::getEventTime, DateUtils.beginOfDay(date), DateUtils.endOfDay(date)));
    }

    @Override
    public long passedNumByDay(PassedNumParam param) {
        return this.count(Wrappers.<EventRecord>lambdaQuery()
                                  .between(ObjectUtils.isNotNull(param.getPeriod()), EventRecord::getEventTime,
                                           ObjectUtils.ifNull(param.getPeriod(), Period::getBegin),
                                           ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                                  .like(StringUtils.isNotEmpty(param.getKeyWord()), EventRecord::getDoorName, param.getKeyWord())
                                  .eq(StringUtils.isNotEmpty(param.getEventResult()), EventRecord::getEventResult, param.getEventResult()));
    }

    @Override
    public List<TimeTrendVo> getDoorTend(String timeFlag) {
        ArrayList<TimeTrendVo> timeTrendVos = new ArrayList<>();
        LocalDateTime now = DateUtils.now();
        if (timeFlag.equals("timeDay")) {
            for (int i = 1; i <= 24; i++) {
                LocalDateTime offset = DateUtils.offset(now, ChronoUnit.HOURS, -(24 - i));
                String begin = DateUtils.format(offset, "yyyy-MM-dd HH:00:00");
                String end = DateUtils.format(offset, "yyyy-MM-dd HH:59:59");
                try {
                    LocalDateTime beginLocalDateTime = DateUtils.parse(begin);
                    LocalDateTime endLocalDateTime = DateUtils.parse(end);

                    Integer eventRecordNum = eventRecordMapper.getEventRecordNum(beginLocalDateTime, endLocalDateTime);
                    TimeTrendVo timeTrendVo = new TimeTrendVo();
                    timeTrendVo.setTimeValue(String.valueOf(offset.getHour()));
                    timeTrendVo.setNum(eventRecordNum);
                    timeTrendVos.add(timeTrendVo);
                } catch (DateTimeParseException e) {
                    log.error("解析时间异常");
                }
            }
        } else if (timeFlag.equals("timeMonth")) {
            for (int i = 1; i <= 30; i++) {
                LocalDateTime offset = DateUtils.offset(now, ChronoUnit.DAYS, -(30 - i));
                String begin = DateUtils.format(offset, "yyyy-MM-dd 00:00:00");
                String end = DateUtils.format(offset, "yyyy-MM-dd 23:59:59");
                try {
                    LocalDateTime beginLocalDateTime = DateUtils.parse(begin);
                    LocalDateTime endLocalDateTime = DateUtils.parse(end);

                    Integer eventRecordNum = eventRecordMapper.getEventRecordNum(beginLocalDateTime, endLocalDateTime);
                    TimeTrendVo timeTrendVo = new TimeTrendVo();
                    String format = DateUtils.format(offset, "yyyy-MM-dd");
                    timeTrendVo.setTimeValue(format);
                    timeTrendVo.setNum(eventRecordNum);
                    timeTrendVos.add(timeTrendVo);
                } catch (DateTimeParseException e) {
                    log.error("解析时间异常");
                }
            }
        } else {
            for (int i = 1; i <= 12; i++) {
                LocalDateTime offset = DateUtils.offset(now, ChronoUnit.MONTHS, -(12 - i));
                String begin = DateUtils.format(DateUtils.beginOfMonth(offset), "yyyy-MM-dd 00:00:00");
                String end = DateUtils.format(DateUtils.endOfMonth(offset), "yyyy-MM-dd 23:59:59");
                try {
                    LocalDateTime beginLocalDateTime = DateUtils.parse(begin);
                    LocalDateTime endLocalDateTime = DateUtils.parse(end);

                    Integer eventRecordNum = eventRecordMapper.getEventRecordNum(beginLocalDateTime, endLocalDateTime);
                    TimeTrendVo timeTrendVo = new TimeTrendVo();
                    String format = DateUtils.format(offset, "yyyy-MM");
                    timeTrendVo.setTimeValue(format);
                    timeTrendVo.setNum(eventRecordNum);
                    timeTrendVos.add(timeTrendVo);
                } catch (DateTimeParseException e) {
                    log.error("解析时间异常");
                }
            }
        }
        return timeTrendVos;
    }

    @Override
    public List<RankingVo> getDoorRanking() {
        LocalDateTime date = DateUtils.now();
        LocalDateTime offset = DateUtils.offset(date, ChronoUnit.DAYS, -30);
        return eventRecordMapper.getDoorRanking(offset, date);
    }

    @Override
    public List<DoorEventsVo> getDoorEvents() {
        return eventRecordMapper.getDoorEvents();
    }

    @Override
    public Page<EventRecordVo> getDoorEventRecord(EventRecordQueryParam eventRecordQueryParam) {
        if (StringUtils.isNotEmpty(eventRecordQueryParam.getPeriod())) {
            String[] split = eventRecordQueryParam.getPeriod().split(",");
            eventRecordQueryParam.setBeginTime(split[0]);
            eventRecordQueryParam.setEndTime(split[1]);
        }
        List<String> thirdPartyIds = securityClientHandlerService.getSecurityDoorDeviceThirdIds(null);
        eventRecordQueryParam.setThirdPartyIds(thirdPartyIds);
        Page<EventRecordVo> page = eventRecordMapper.getDoorEventRecord(
                PagingUtils.convert(eventRecordQueryParam), eventRecordQueryParam);
        fillPictureFiles(page.getRecords());
        return page;
    }

    private void fillPictureFiles(List<EventRecordVo> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        List<Long> fileIds = records.stream()
                .map(EventRecordVo::getPictureFileId)
                .filter(id -> id != null)
                .distinct()
                .toList();
        if (fileIds.isEmpty()) {
            return;
        }
        List<FileVO> files = fileClient.getByIds(fileIds);
        if (files == null || files.isEmpty()) {
            return;
        }
        Map<Long, FileVO> fileMap = files.stream()
                .filter(file -> file != null && file.getId() != null)
                .collect(Collectors.toMap(FileVO::getId, file -> file, (oldValue, newValue) -> oldValue));
        records.forEach(record -> record.setPictureFile(fileMap.get(record.getPictureFileId())));
    }

    @Override
    public Page<EventRecordVo> getvisualizationDoorEventRecord(EventRecordQueryParam eventRecordQueryParam) {
        Set<String> codes;
        try {
            codes = this.deviceBimSceneClient.getBimDeviceCodes(eventRecordQueryParam.getSceneType(), eventRecordQueryParam.getAreaId());
        } catch (Exception e) {
            log.error("远程调用device-center获取设备code失败！，错误原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("codes:{}", codes.size());
        if (ObjectUtils.isEmpty(codes)) {
            codes.add("-1");
        }
        for (String ex : codes) {
            log.info("code:{}", ex);
        }
        eventRecordQueryParam.setThirdPartyIds(new ArrayList<>(codes));
        if (StringUtils.isNotEmpty(eventRecordQueryParam.getPeriod())) {
            String[] split = eventRecordQueryParam.getPeriod().split(",");
            eventRecordQueryParam.setBeginTime(split[0]);
            eventRecordQueryParam.setEndTime(split[1]);
        }
        return eventRecordMapper.getVisualizationDoorEventRecord(PagingUtils.convert(eventRecordQueryParam), eventRecordQueryParam);
    }

    @Override
    public List<TimeTrendVo> getToDayEventTrend() {
        ArrayList<TimeTrendVo> timeTrendVos = new ArrayList<>();
        LocalDateTime now = DateUtils.now();
        int hour = now.getHour();
        for (int i = 0; i <= hour; i++) {
            LocalDateTime offset = DateUtils.offset(now, ChronoUnit.HOURS, -(hour - i));
            String begin = DateUtils.format(offset, "yyyy-MM-dd HH:00:00");
            String end = DateUtils.format(offset, "yyyy-MM-dd HH:59:59");
            try {
                LocalDateTime beginLocalDateTime = DateUtils.parse(begin);
                LocalDateTime endLocalDateTime = DateUtils.parse(end);

                Integer eventRecordNum = eventRecordMapper.getEventRecordNum(beginLocalDateTime, endLocalDateTime);
                TimeTrendVo timeTrendVo = new TimeTrendVo();
                timeTrendVo.setTimeValue(String.valueOf(offset.getHour()));
                timeTrendVo.setNum(eventRecordNum);
                timeTrendVos.add(timeTrendVo);
            } catch (DateTimeParseException e) {
                log.error("解析时间异常");
            }
        }
        return timeTrendVos;
    }

    @Override
    public Page<EventRecordVo> getAbnormalEvent(EventRecordQueryParam eventRecordQueryParam) {
        if (!eventRecordQueryParam.getPeriod().isEmpty()) {
            String[] split = eventRecordQueryParam.getPeriod().split(",");
            eventRecordQueryParam.setBeginTime(split[0]);
            eventRecordQueryParam.setEndTime(split[1]);
        }
        return eventRecordMapper.getAbnormalEvent(PagingUtils.convert(eventRecordQueryParam), eventRecordQueryParam);
    }

    @Override
    public List<String> getEventIds(List<String> ids) {

        return null;
    }

    @Override
    public DoorStatisticsVo getEventInOrOut(StatisticsParam param) {

        return eventRecordMapper.getEventInOrOut(param);
    }

    @Override
    public Map<String, Object> getInAndOutTrend(StatisticsParam param) {
        Map<String, Object> remap = new HashMap<>();
        List<String> xTitle = new ArrayList<>();
        String timeFlag = "";
        switch (param.getType()) {
            case "TODAY":
                timeFlag = "'%H'";
                for (int i = 0; i < 24; i++) {
                    LocalDateTime offset = DateUtils.offset(param.getPeriod().getBegin(), ChronoUnit.HOURS, i);
                    int hour = offset.getHour();
                    if (hour < 10) {
                        xTitle.add("0" + hour);
                    } else {
                        xTitle.add(String.valueOf(hour));
                    }
                }
                break;
            case "THIS_WEEK": {
                timeFlag = "'%Y-%m-%d'";
                int between = (int) DateUtils.between(param.getPeriod().getBegin(), param.getPeriod().getEnd(), ChronoUnit.DAYS);
                for (int i = 0; i < between; i++) {
                    LocalDateTime offset = DateUtils.offset(param.getPeriod().getBegin(), ChronoUnit.DAYS, i);
                    xTitle.add(DateUtils.format(offset, "yyyy-MM-dd"));
                }
                break;
            }
            case "THIS_MONTH": {
                timeFlag = "'%Y-%m-%d'";
                int between = (int) DateUtils.between(param.getPeriod().getBegin(), param.getPeriod().getEnd(), ChronoUnit.DAYS);
                for (int i = 0; i < between; i++) {
                    LocalDateTime offset = DateUtils.offset(param.getPeriod().getBegin(), ChronoUnit.DAYS, i);
                    xTitle.add(DateUtils.format(offset, "yyyy-MM-dd"));
                }
                break;
            }
            case "THIS_YEAR":
                timeFlag = "'%Y-%m'";
                for (int i = 1; i < 12; i++) {
                    LocalDateTime offset = DateUtils.offset(param.getPeriod().getBegin(), ChronoUnit.MONTHS, i);
                    xTitle.add(DateUtils.format(offset, "yyyy-MM"));
                }
                break;
        }
        param.setInAndOut("1");
        List<TimeTrendVo> inTrend = eventRecordMapper.getInAndOutTrend(param, timeFlag);
        Integer inTotal = eventRecordMapper.getInAndOutTotal(param);
        param.setInAndOut("0");
        List<TimeTrendVo> outTrend = eventRecordMapper.getInAndOutTrend(param, timeFlag);
        Integer outTotal = eventRecordMapper.getInAndOutTotal(param);
        remap.put("inTrend", inTrend);
        remap.put("inTotal", inTotal);
        remap.put("outTrend", outTrend);
        remap.put("outTotal", outTotal);
        remap.put("xtitle", xTitle);
        return remap;
    }

    @Override
    public void insertOrUpdate(EventRecord event) {
        try {
            EventRecord eventRecord = eventRecordMapper.selectOne(Wrappers.<EventRecord>lambdaQuery()
                                                                          .eq(EventRecord::getDoorIndexCode, event.getDoorIndexCode())
                                                                          .eq(EventRecord::getEventTime, event.getEventTime())
                                                                          .eq(EventRecord::getTenant, event.getTenant())
                                                                          .eq(StringUtils.isNotEmpty(event.getPersonId()), EventRecord::getPersonId, event.getPersonId()));
            if (eventRecord != null) {
                eventRecord.setEventTime(event.getEventTime());
                eventRecord.setUpdateTime(event.getUpdateTime());
                if (event.getPictureFileId() != null) {
                    eventRecord.setPictureFileId(event.getPictureFileId());
                }
                eventRecordMapper.updateById(eventRecord);
            } else {
                eventRecordMapper.insert(event);
            }
        } catch (Exception e) {
            log.error("门禁事件同步异常：", e);
        }
    }

    /**
     * 查询巡更记录
     *
     * @param param 查询参数
     *
     * @return 巡更记录
     */
    @Override
    public List<EventRecord> getPatrolRecord(StatisticsParam param) {
        //eventType:198918(巡查卡比对通过事件)
        return this.lambdaQuery().eq(EventRecord::getEventType, "巡查卡比对通过")
                .between(ObjectUtils.isNotNull(param.getPeriod()), EventRecord::getEventTime,
                         ObjectUtils.ifNull(param.getPeriod(), Period::getBegin),
                         ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                .orderByDesc(EventRecord::getEventTime).list();
    }

}
