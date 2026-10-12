package com.guhecloud.cube.srv.gen.visitor.management.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.visitor.management.server.enums.ReservationStatusEnum;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.ReservationRecordMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.VisitAreaDetailMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.VisitAreaMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.ReservationRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitAreaDetailPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitAreaPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.service.VisitAreaService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.LampAreaService;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.KeyWordPageParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.VisitAreaDetailParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.VisitAreaDetailTimeParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.VisitAreaParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.AreaSelectResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.VisitAreaDetailResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.VisitAreaDetailTimeResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.VisitAreaResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 区域
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitAreaServiceImpl extends ServiceImpl<VisitAreaMapper, VisitAreaPO> implements VisitAreaService {
    private final LampAreaService lampAreaService;
    private final VisitAreaMapper visitAreaMapper;
    private final VisitAreaDetailMapper visitAreaDetailMapper;
    private final SqlSessionFactory sqlSessionFactory;
    private final ReservationRecordMapper reservationRecordMapper;

    /**
     * 区域下拉列表
     *
     * @param param 入参
     *
     * @return 列表
     */
    @Override
    public List<AreaSelectResult> getAreaList(KeyWordPageParam param) {
        List<VisitAreaPO> list = this.lambdaQuery().list();
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().map(t -> {
            AreaSelectResult result = new AreaSelectResult();
            result.setId(t.getId());
            result.setName(t.getAreaName());
            return result;
        }).collect(Collectors.toList());

    }

    /**
     * 访问时段列表
     *
     * @param param 入参
     *
     * @return 访客访问时段列表
     */
    @Override
    public Pagination<VisitAreaResult> periodPage(KeyWordPageParam param) {
        Page<VisitAreaPO> page = visitAreaMapper.selectPage(PagingUtils.convert(param), Wrappers.<VisitAreaPO>lambdaQuery()
                                                                    .eq(VisitAreaPO::getNormal, true)
                                                                    .like(StringUtils.isNotBlank(param.getKeyword()), VisitAreaPO::getAreaName, param.getKeyword())
                                                           );
        return PagingUtils.convert(page, VisitAreaResult.class);
    }

    /**
     * 访问时段详情
     *
     * @param param 条件
     *
     * @return 详情
     */
    @Override
    @SuppressWarnings("unchecked")
    public VisitAreaResult periodDetail(VisitAreaDetailParam param) {
        Assert.isParameterError(param.getAreaId() == null, "区域id不能为空");
        VisitAreaPO po = visitAreaMapper.selectOne(Wrappers.<VisitAreaPO>lambdaQuery()
                                                           .eq(param.getParentId() != null, VisitAreaPO::getId, param.getParentId())
                                                           .eq(VisitAreaPO::getAreaId, param.getAreaId())
                                                           .last(" limit 1 ")
                                                  );
        if (po == null) {
            po = new VisitAreaPO();
        }
        VisitAreaResult result = new VisitAreaResult();
        BeanUtils.copyProperties(po, result);
        // 获取详情
        Period period = param.getPeriod();
        boolean periodBool = period != null && period.getBegin() != null && period.getEnd() != null;
        List<VisitAreaDetailPO> detailList = visitAreaDetailMapper.selectList(Wrappers.<VisitAreaDetailPO>lambdaQuery()
                                                                                      .eq(VisitAreaDetailPO::getAreaId, param.getAreaId())
                                                                                      .eq(VisitAreaDetailPO::getNormal, true)
                                                                                      .ge(periodBool, VisitAreaDetailPO::getEndDate, ObjectUtils.ifNull(period, Period::getBegin))
                                                                                      .le(periodBool, VisitAreaDetailPO::getStartDate, ObjectUtils.ifNull(period, Period::getEnd))
                                                                                      .orderByAsc(VisitAreaDetailPO::getStartDate, VisitAreaDetailPO::getStartTimeStr, VisitAreaDetailPO::getNextDayBool, VisitAreaDetailPO::getEndTimeStr)
                                                                             );
        if (detailList != null) {
            List<VisitAreaDetailResult> detailResultList = new ArrayList<>();

            // 获取区域某天的(时间段)预约数量
            Map<String, List<ReservationRecordPO>> reservationMap = new HashMap<>();
            if (periodBool && DateUtils.isSameDay(period.getBegin(), period.getEnd())) {
                reservationMap = this.getDayTimeReservation(param.getAreaId(), period.getBegin());
            }

            Map<String, List<VisitAreaDetailPO>> dateMap = detailList.stream().filter(p -> StringUtils.isNotBlank(p.getDatePeriodStr()))
                    .collect(Collectors.groupingBy(VisitAreaDetailPO::getDatePeriodStr));
            VisitAreaPO finalPo = po;
            Map<String, List<ReservationRecordPO>> finalReservationMap = reservationMap;
            dateMap.forEach((dateStr, dateList) -> {
                VisitAreaDetailResult detailResult = new VisitAreaDetailResult();
                VisitAreaDetailPO o = dateList.get(0);
                BeanUtils.copyProperties(o, detailResult);
                detailResult.setParentId(finalPo.getId());
                List<VisitAreaDetailTimeResult> timeList = new ArrayList<>();
                for (VisitAreaDetailPO poTime : dateList) {
                    VisitAreaDetailTimeResult timeResult = new VisitAreaDetailTimeResult();
                    timeResult.setId(poTime.getId());
                    timeResult.setTimeStr(poTime.getTimeStr());
                    timeResult.setStartTimeStr(poTime.getStartTimeStr());
                    timeResult.setEndTimeStr(poTime.getEndTimeStr());
                    timeResult.setNextDayBool(poTime.getNextDayBool());
                    timeResult.setTotalNumber(poTime.getTotalNumber());
                    List<ReservationRecordPO> list = finalReservationMap.get(poTime.getTimeStr());
                    timeResult.setRemainNumber(list == null ? poTime.getTotalNumber() : Math.max(poTime.getTotalNumber() - list.size(), 0));
                    timeList.add(timeResult);
                }
                detailResult.setTimeList(timeList);
                detailResultList.add(detailResult);
            });
            result.setDetailList(detailResultList);
        }

        return result;
    }

    /**
     * 添加区域时段
     *
     * @param param 入参
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addVisitPeriod(VisitAreaParam param) {
        VisitAreaResult thirdAreaResult = lampAreaService.areaDetail(param.getAreaId());
        Assert.isNotFound(thirdAreaResult == null || thirdAreaResult.getId() == null, "区域不存在！");
        param.setDeviceNumber(thirdAreaResult.getDeviceNumber());
        VisitAreaResult visitAreaResult = visitAreaMapper.selectByAreaId(param.getAreaId());
        Assert.isConflict(visitAreaResult != null, "设置已存在！");
        VisitAreaPO po = new VisitAreaPO();
        BeanUtils.copyProperties(param, po);
        po.setId(IdWorker.getId());
        po.setNormal(true);
        po.setAreaName(param.getAreaName());
        po.setCreateUserName(SecurityContextHolder.getPrincipal().getName());
        visitAreaMapper.insert(po);
        this.areaDetailSave(param);
        return po.getId();
    }

    /**
     * 编辑访问时段
     *
     * @param param 入参
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPeriods(VisitAreaParam param) {
        VisitAreaPO po = visitAreaMapper.selectById(param.getId());
        if (po == null) {
            throw new ServiceException("数据不存在！");
        }
        po.setVisitPeriods(param.getVisitPeriods());
        if (param.getDetailList() == null || param.getDetailList().isEmpty()) {
            throw new ServiceException("请设置访问日期段！");
        }
        // 保存时段详情
        this.areaDetailSave(param);

        visitAreaMapper.updateById(po);
    }

    /**
     * 移除访问区域
     *
     * @param areaId 区域
     */
    @Override
    public void removePeriod(Long areaId) {
        List<VisitAreaPO> areaPOList = visitAreaMapper.selectList(Wrappers.<VisitAreaPO>lambdaQuery().eq(VisitAreaPO::getAreaId, areaId));
        if (!CollectionUtils.isEmpty(areaPOList)) {
            DatabaseContextHolder.softDelete(VisitAreaPO.class, areaPOList.stream().map(VisitAreaPO::getId).collect(Collectors.toSet()));
        }
        List<VisitAreaDetailPO> detailPOList = visitAreaDetailMapper.selectList(Wrappers.<VisitAreaDetailPO>lambdaQuery()
                                                                                        .eq(VisitAreaDetailPO::getAreaId, areaId));
        if (!CollectionUtils.isEmpty(detailPOList)) {
            DatabaseContextHolder.softDelete(VisitAreaDetailPO.class, detailPOList.stream().map(VisitAreaDetailPO::getId)
                    .collect(Collectors.toSet()));
        }
    }

    /**
     * 保存访问时段详情
     * 注意这里会先删除原来的区域时段，后再保存
     *
     * @param param 入参
     *
     * @return id
     */
    private Long areaDetailSave(VisitAreaParam param) {
        long result = 0L;
        if (param.getDetailList() != null && !param.getDetailList().isEmpty()) {
            // 日期段交叉校验的集合
            List<Period> datePeriodList = new ArrayList<>();
            // 组装数据
            List<VisitAreaDetailPO> deatilPOList = new ArrayList<>();
            for (VisitAreaDetailParam detailParam : param.getDetailList()) {
                Period period = detailParam.getPeriod();
                String datePeriodStr = DateUtils.formatDate(period.getBegin()) + "," + DateUtils.formatDate(period.getEnd());
                datePeriodList.add(period);
                for (VisitAreaDetailTimeParam timeParam : detailParam.getTimeList()) {
                    VisitAreaDetailPO detailPO = new VisitAreaDetailPO();
                    BeanUtils.copyProperties(detailParam, detailPO);
                    detailPO.setStartDate(detailParam.getPeriod().getBegin());
                    // 这里mysql日期字段(yyyy-MM-dd) 23:59:59.999会被存成第二天0点
                    detailPO.setEndDate(detailParam.getPeriod().getEnd().minusNanos(500));
                    detailPO.setStartDateStr(DateUtils.formatDate(detailPO.getStartDate()));
                    detailPO.setEndDateStr(DateUtils.formatDate(detailPO.getEndDate()));
                    detailPO.setDatePeriodStr(datePeriodStr);
                    detailPO.setTotalNumber(timeParam.getTotalNumber());
                    detailPO.setAreaId(param.getAreaId());
                    detailPO.setAreaName(param.getAreaName());
                    detailPO.setNextDayBool(timeParam.getNextDayBool());
                    detailPO.setTimeStr(timeParam.getTimeStr());
                    detailPO.setStartTimeStr(timeParam.getStartTimeStr());
                    detailPO.setEndTimeStr(timeParam.getEndTimeStr());
                    deatilPOList.add(detailPO);
                }
            }
            Assert.isConflict(this.checkPeriodCross(datePeriodList), "日期段有交叉！");
            result = param.getDetailList().size();
            // 删除原来的
            visitAreaDetailMapper.delete(Wrappers.<VisitAreaDetailPO>lambdaQuery().eq(VisitAreaDetailPO::getAreaId, param.getAreaId()));

            // 关闭session的自动提交
            SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            try {
                VisitAreaDetailMapper detailMapper = sqlSession.getMapper(VisitAreaDetailMapper.class);
                deatilPOList.forEach(detailMapper::insert);
                // 提交数据
                sqlSession.commit();
            } catch (Exception e) {
                sqlSession.rollback();
                throw new ServiceException("操作失败！");
            } finally {
                sqlSession.close();
            }
        }

        return result;
    }

    /**
     * 获取区域某日的预约(按时段分组)
     *
     * @param date   日期
     * @param areaId 区域
     *
     * @return 每个时段的预约
     */
    private Map<String, List<ReservationRecordPO>> getDayTimeReservation(Long areaId, LocalDateTime date) {
        Map<String, List<ReservationRecordPO>> map = new HashMap<>();
        if (date == null || areaId == null) {
            return map;
        }
        // 处理用于查询已预约数量的日期段
        List<Integer> ingStatusList = new ArrayList<>();
        ingStatusList.add(ReservationStatusEnum.DEPARTURE.getStatus());
        ingStatusList.add(ReservationStatusEnum.RESERVATION.getStatus());
        ingStatusList.add(ReservationStatusEnum.ON_THE_VISIT.getStatus());
        List<ReservationRecordPO> recordPOList = reservationRecordMapper.selectList(Wrappers.<ReservationRecordPO>lambdaQuery()
                                                                                            .eq(ReservationRecordPO::getAreaId, areaId)
                                                                                            .ge(ReservationRecordPO::getStartDateTime, DateUtils.beginOfDay(date))
                                                                                            .le(ReservationRecordPO::getEndDateTime, DateUtils.endOfDay(DateUtils.offsetDay(date, 1)))
                                                                                            .in(ReservationRecordPO::getStatus, ingStatusList)
                                                                                   );
        if (recordPOList != null) {
            map = recordPOList.stream().collect(Collectors.groupingBy(ReservationRecordPO::getTimeStr));
        }

        return map;
    }

    /**
     * 时间段交叉校验（true表示交叉）
     *
     * @param sourceList 源数据
     *
     * @return 是否交叉
     */
    private boolean checkPeriodCross(List<Period> sourceList) {
        if (sourceList != null && !sourceList.isEmpty()) {
            int sourceListSize = sourceList.size();
            for (int i = 0; i < sourceListSize; i++) {
                for (int j = i + 1; j < sourceListSize; j++) {
                    Period periodI = sourceList.get(i);
                    Period periodJ = sourceList.get(j);
                    boolean crossBool = DateUtils.toTimestamp(periodI.getBegin()) <= DateUtils.toTimestamp(periodJ.getEnd()) &&
                                        DateUtils.toTimestamp(periodI.getEnd()) >= DateUtils.toTimestamp(periodJ.getBegin());
                    if (crossBool) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
