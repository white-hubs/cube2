package com.guhecloud.cube.biz.door.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.door.server.dao.AccessEventMapper;
import com.guhecloud.cube.biz.door.server.dao.CardMapper;
import com.guhecloud.cube.biz.door.server.dao.EventRecordMapper;
import com.guhecloud.cube.biz.door.server.dao.OrgMapper;
import com.guhecloud.cube.biz.door.server.dao.VisitingRecordMapper;
import com.guhecloud.cube.biz.door.server.entity.AccessEvent;
import com.guhecloud.cube.biz.door.server.entity.Card;
import com.guhecloud.cube.biz.door.server.entity.EventRecord;
import com.guhecloud.cube.biz.door.server.entity.Org;
import com.guhecloud.cube.biz.door.server.entity.VisitingRecord;
import com.guhecloud.cube.biz.door.server.queryparam.AccessEventQueryParam;
import com.guhecloud.cube.biz.door.server.queryparam.CardQueryParam;
import com.guhecloud.cube.biz.door.server.queryparam.CommonStatQueryParam;
import com.guhecloud.cube.biz.door.server.service.DoorCardService;
import com.guhecloud.cube.biz.door.server.vo.AccessEventVO;
import com.guhecloud.cube.biz.door.server.vo.CardStatByStatusVO;
import com.guhecloud.cube.biz.door.server.vo.CommonStatVO;
import com.guhecloud.cube.biz.door.server.vo.DoorCardVO;
import com.guhecloud.cube.biz.door.server.vo.EventRecordVo;
import com.guhecloud.cube.biz.door.server.vo.VisitingRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoorCardServiceImpl extends ServiceImpl<CardMapper, Card> implements DoorCardService {
    private final AccessEventMapper accessEventMapper;
    private final VisitingRecordMapper visitingRecordMapper;
    private final EventRecordMapper eventRecordMapper;
    private final OrgMapper orgMapper;

    @Override
    public Pagination<DoorCardVO> getCardPage(CardQueryParam param) {
        Page<Card> page = this.lambdaQuery().and(StringUtils.isNotEmpty(param.getKeyword()),
                                                 q -> q.like(Card::getCardNo, param.getKeyword())
                                                         .or().like(Card::getPersonName, param.getKeyword()))
                .eq(StringUtils.isNotEmpty(param.getStatus()), Card::getUseStatus, param.getStatus())
                .orderByDesc(Card::getCreateTime)
                .page(PagingUtils.convert(param));
        Pagination<DoorCardVO> pagination = PagingUtils.convert(page, DoorCardVO.class);
        if (CollectionUtils.isNotEmpty(pagination.getRecords())) {
            pagination.getRecords().forEach(cd -> {
                if (StringUtils.isNotEmpty(cd.getOrgId())) {
                    Org org = orgMapper.selectById(cd.getOrgId());
                    cd.setOrgName(org != null ? org.getOrgName() : "");
                }
            });
        }
        return pagination;
    }

    @Override
    public Pagination<VisitingRecordVO> getVisitingRecordPage(AccessEventQueryParam param) {
        Period period = param.getPeriod();
        Period period2 = param.getPeriod2();
        Page<VisitingRecord> page = visitingRecordMapper.selectPage(PagingUtils.convert(param), Wrappers.<VisitingRecord>lambdaQuery()
                                                                            .like(StringUtils.isNotEmpty(param.getCardNo()), VisitingRecord::getCardNo, param.getCardNo())
                                                                            .and(StringUtils.isNotEmpty(param.getKeyword()), q ->
                                                                                    q.like(VisitingRecord::getReceptionistName, param.getKeyword())
                                                                                            .or().like(VisitingRecord::getVisitorName, param.getKeyword())
                                                                                            .or().like(VisitingRecord::getPhoneNo, param.getKeyword()))
                                                                            .eq(StringUtils.isNotEmpty(param.getStatus()), VisitingRecord::getVisitorStatus, param.getStatus())
                                                                            .between(period !=
                                                                                     null, VisitingRecord::getVisitStartTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                            .between(period2 !=
                                                                                     null, VisitingRecord::getVisitEndTime, ObjectUtils.ifNull(period2, Period::getBegin), ObjectUtils.ifNull(period2, Period::getEnd))
                                                                            .orderByDesc(VisitingRecord::getVisitStartTime)
                                                                   );
        return PagingUtils.convert(page, VisitingRecordVO.class);
    }

    @Override
    public Pagination<AccessEventVO> getAccessEventPage(AccessEventQueryParam param) {
        Period period = param.getPeriod();
        Page<AccessEvent> page = accessEventMapper.selectPage(PagingUtils.convert(param), Wrappers.<AccessEvent>lambdaQuery()
                                                                      .like(StringUtils.isNotEmpty(param.getCardNo()), AccessEvent::getCardNo, param.getCardNo())
                                                                      .like(StringUtils.isNotEmpty(param.getKeyword()), AccessEvent::getEventName, param.getKeyword())
                                                                      .eq(AccessEvent::getAccessType, param.getAccessType())
                                                                      .between(period !=
                                                                               null, AccessEvent::getEventTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                      .orderByDesc(AccessEvent::getEventTime)
                                                             );
        return PagingUtils.convert(page, AccessEventVO.class);
    }

    @Override
    public Pagination<EventRecordVo> getDoorEventPage(AccessEventQueryParam param) {
        Period period = param.getPeriod();
        Page<EventRecord> page = eventRecordMapper.selectPage(PagingUtils.convert(param), Wrappers.<EventRecord>lambdaQuery()
                                                                      .like(StringUtils.isNotEmpty(param.getCardNo()), EventRecord::getCardNo, param.getCardNo())
                                                                      .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q.like(EventRecord::getEventName, param.getKeyword())
                                                                              .or().like(EventRecord::getDoorName, param.getKeyword()))
                                                                      .between(period !=
                                                                               null, EventRecord::getEventTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                      .orderByDesc(EventRecord::getEventTime)
                                                             );
        return PagingUtils.convert(page, EventRecordVo.class);
    }

    @Override
    public CardStatByStatusVO getCardStatByStatus() {
        return this.baseMapper.getCardStatByStatus();
    }

    /**
     * 初始化统计参数
     *
     * @param param
     */
    private void initStatParam(CommonStatQueryParam param) {
        //根据统计类型，默认分组的时间格式
        if ("TODAY".equals(param.getType())) {
            param.setGroupFormat("%H");
        } else if ("THIS_WEEK".equals(param.getType()) || "THIS_MONTH".equals(param.getType())) {
            param.setGroupFormat("%Y-%m-%d");
        } else if ("THIS_YEAR".equals(param.getType())) {
            param.setGroupFormat("%Y-%m");
        } else {
            param.setGroupFormat("%Y-%m");
        }
        if (param.getPeriod() == null) {//默认本月
            LocalDateTime startDate = DateUtils.beginOfMonth(DateUtils.now());
            LocalDateTime endDate = DateUtils.now();
            param.setPeriod(Period.of(startDate, endDate));
        }
    }

    @Override
    public List<CommonStatVO> getVisitingRecordStatByTime(CommonStatQueryParam param) {
        //初始化查询参数
        this.initStatParam(param);
        List<CommonStatVO> dataList = this.visitingRecordMapper.getVisitingRecordStatByTime(param);
        return checkTrendData(dataList, param.getType(), param.getPeriod());
    }

    private List<CommonStatVO> checkTrendData(List<CommonStatVO> dataList, String statType, Period period) {
        LocalDateTime start = period.getBegin();
        LocalDateTime end = period.getEnd();
        if (statType.equals("THIS_WEEK") || statType.equals("THIS_MONTH")) {
            Stream.iterate(start, day -> day.plusDays(1))
                    .limit(ChronoUnit.DAYS.between(start, end) + 1)
                    .forEach(date -> {
                        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        CommonStatVO vo = dataList.stream().filter(d -> d.getName().equals(dateStr)).findFirst().orElse(null);
                        if (vo == null) {
                            dataList.add(new CommonStatVO(dateStr, "0"));
                        }
                    });
        } else if (statType.equals("TODAY")) {
            IntStream.range(0, 24).forEach(hour -> {
                CommonStatVO vo = dataList.stream().filter(d -> d.getName().equals(String.valueOf(hour))).findFirst().orElse(null);
                if (vo == null) {
                    dataList.add(new CommonStatVO(String.format("%02d", hour), "0"));
                }
            });
        } else if (statType.equals("THIS_YEAR")) {
            Stream.iterate(start, day -> day.plusMonths(1))
                    .limit(ChronoUnit.MONTHS.between(start, end) + 1)
                    .forEach(date -> {
                        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                        CommonStatVO vo = dataList.stream().filter(d -> d.getName().equals(dateStr)).findFirst().orElse(null);
                        if (vo == null) {
                            dataList.add(new CommonStatVO(dateStr, "0"));
                        }
                    });
        }
        dataList.sort(Comparator.comparing(CommonStatVO::getName));
        return dataList;
    }

    @Override
    public List<CommonStatVO> getAccessEventStatByName(AccessEventQueryParam param) {
        return this.accessEventMapper.getEventStatByName(param);
    }

    @Override
    public List<CommonStatVO> getDoorEventStatByName(AccessEventQueryParam param) {
        return this.eventRecordMapper.getEventStatByName(param);
    }

    @Override
    public void insertOrUpdate(Card card) {
        try {
            Card temp = this.baseMapper.selectOne(Wrappers.<Card>lambdaQuery()
                                                          .eq(Card::getCardNo, card.getCardNo()));
            if (temp != null) {
                temp.setUpdateTime(card.getUpdateTime());
                temp.setPersonId(card.getPersonId());
                temp.setPersonName(card.getPersonName());
                temp.setUseStatus(card.getUseStatus());
                temp.setStartDate(card.getStartDate());
                temp.setEndDate(card.getEndDate());
                this.baseMapper.updateById(temp);
            } else {
                this.baseMapper.insert(card);
            }
        } catch (Exception e) {
            log.error("门禁卡同步失败：", e);
        }

    }
}
