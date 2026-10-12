package com.guhecloud.cube.srv.gen.visitor.management.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.srv.gen.visitor.management.server.enums.ReservationStatusEnum;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.BlacklistMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.PassRecordMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.ReservationRecordMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.BlacklistPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.PassRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.ReservationRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitAreaPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.service.ReservationPassService;
import com.guhecloud.cube.srv.gen.visitor.management.server.service.VisitAreaService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.LampReservationPassService;
import com.guhecloud.cube.srv.gen.visitor.management.server.utils.QRCodeUtil;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.PassRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.ReservationParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.ReservationRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.ContrastResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.PassRecordResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.QoqStatResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.ReservationRecordResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.ReservationResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 预约通行服务层
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReservationPassServiceImpl implements ReservationPassService {
    private final LampReservationPassService lampReservationPassService;
    private final ReservationRecordMapper reservationRecordMapper;
    private final VisitAreaService visitAreaService;
    private final BlacklistMapper blacklistMapper;
    private final RedissonClient redissonClient;
    private final PassRecordMapper passRecordMapper;
    private final FlowClient flowClient;

    /**
     * 通行记录
     *
     * @param param 入参
     *
     * @return 通行记录
     */
    @Override
    public Pagination<PassRecordResult> passRecordPage(PassRecordParam param) {

        Period period = param.getPeriod();
        boolean periodBool = period != null && period.getBegin() != null && period.getEnd() != null;
        Page<PassRecordPO> page = passRecordMapper.selectPage(PagingUtils.convert(param), Wrappers.<PassRecordPO>lambdaQuery()
                                                                      .between(periodBool, PassRecordPO::getSwingTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                                      .like(StringUtils.isNotEmpty(param.getName()), PassRecordPO::getVisitorName, param.getName())
                                                                      .orderByDesc(PassRecordPO::getSwingTime)
                                                             );
        if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
            return new Pagination<>();
        }
        List<PassRecordResult> passRecordResultList = new ArrayList<>();
        for (PassRecordPO record : page.getRecords()) {
            PassRecordResult passRecordResult = ObjectUtils.copying(record, PassRecordResult.class);
            passRecordResult.setPersonName(record.getVisitorName());
            passRecordResult.setPersonId(record.getVisitorId());
            passRecordResult.setEnterOrExit(record.getEnterOrExist());
            passRecordResultList.add(passRecordResult);
        }

        return PagingUtils.convert(page, passRecordResultList);
    }

    /**
     * 预约
     *
     * @param param 入参
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReservationResult reservation(ReservationParam param) {
        var checkTime = DateUtils.offsetDay(LocalDateTime.now(), BigDecimal.ROUND_UNNECESSARY);
        Assert.isOutOfRange(checkTime.isBefore(param.getPeriod().getBegin()), "最多只能预约7天后的数据！");
        // 验证黑名单
        var blacklistPOList = blacklistMapper.selectList(Wrappers.<BlacklistPO>lambdaQuery()
                                                                 .eq(BlacklistPO::getPhone, param.getPhone()));
        if (blacklistPOList != null && !blacklistPOList.isEmpty()) {
            throw new ServiceException("用户被禁用，请联系管理员！");
        }

        // 锁
        var fairLock = redissonClient.getFairLock("lock:visitorReservation:timeStr:" + param.getTimeStr());
        var lookBool = false;

        try {
            lookBool = fairLock.tryLock(10L, 10L, TimeUnit.SECONDS);
            if (!lookBool) {
                throw new ServiceException("网络繁忙，请稍后重试！");
            }
            return this.reservationResult(param);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            if (lookBool) {
                fairLock.unlock();
            }
        }

    }

    /**
     * 预约
     *
     * @param param 入参
     *
     * @return 结果
     */
    private ReservationResult reservationResult(ReservationParam param) {
        var ingStatusList = new ArrayList<>();
        ingStatusList.add(ReservationStatusEnum.DEPARTURE.getStatus());
        ingStatusList.add(ReservationStatusEnum.RESERVATION.getStatus());
        ingStatusList.add(ReservationStatusEnum.ON_THE_VISIT.getStatus());

        var result = lampReservationPassService.reservation(param);
        if (result == null || result.getId() == null) {
            throw new ServiceException("预约失败了!");
        }
        var visitAreaPO = new VisitAreaPO();
        if (CollectionUtils.isNotEmpty(param.getAreaId())) {
            var list = visitAreaService.lambdaQuery()
                    .in(VisitAreaPO::getAreaId, param.getAreaId())
                    .list();
            if (CollectionUtils.isNotEmpty(list)) {
                visitAreaPO = list.get(0);
            }
        }
        // 本系统保存
        var po = new ReservationRecordPO();
        po.setId(result.getId());
        po.setAddress(param.getAreaName());
        po.setAreaId(visitAreaPO.getId());
        po.setAreaName(visitAreaPO.getAreaName());
        po.setName(param.getName());
        po.setStartDateTime(param.getPeriod().getBegin());
        po.setEndDateTime(param.getPeriod().getEnd());
        po.setPhone(param.getPhone());
        po.setCertificateType(param.getCertificateType());
        po.setCertificateNumber(param.getCertificateNumber());
        po.setPersonSum(param.getPersonSum());
        po.setReason(param.getReason());
        po.setDw(param.getDw());
        po.setAreaCode(param.getAreaCode());
        po.setDayStr(param.getDayStr());
        if (StringUtils.isBlank(po.getDayStr())) {
            po.setDayStr(DateUtils.formatDate(param.getPeriod().getBegin()));
        }
        po.setTimeStr(param.getTimeStr());
        po.setNextDayBool(param.getNextDayBool());
        if (po.getNextDayBool() == null) {
            po.setNextDayBool(!DateUtils.isSameDay(param.getPeriod().getBegin(), param.getPeriod().getEnd()));
        }
        po.setVBarCode(result.getBarCode());
        po.setStatus(Boolean.TRUE.equals(param.getAuditBool()) ? ReservationStatusEnum.TO_BE_REVIEWED.getStatus()
                                                               : ReservationStatusEnum.RESERVATION.getStatus());
        reservationRecordMapper.insert(po);

        return result;
    }

    /**
     * 预约记录分页列表
     *
     * @param param 入参
     *
     * @return 预约记录
     */
    @Override
    public Pagination<ReservationRecordResult> reservationRecordPage(ReservationRecordParam param) {
        Period period = param.getPeriod();
        boolean periodBool = period != null && period.getBegin() != null && period.getEnd() != null;
        Page<ReservationRecordPO> page = reservationRecordMapper.selectPage(PagingUtils.convert(param), Wrappers
                                                                                    .<ReservationRecordPO>lambdaQuery()
                                                                                    .eq(StringUtils.isNotBlank(param.getPhone()), ReservationRecordPO::getPhone, param.getPhone())
                                                                                    .ge(periodBool, ReservationRecordPO::getEndDateTime, ObjectUtils.ifNull(period, Period::getBegin))
                                                                                    .le(periodBool, ReservationRecordPO::getStartDateTime, ObjectUtils.ifNull(period, Period::getEnd))
                                                                                    .like(StringUtils.isNotEmpty(param.getName()), ReservationRecordPO::getName, param.getName())
                                                                                    .in(param.getStatusList() != null &&
                                                                                        !param.getStatusList().isEmpty(), ReservationRecordPO::getStatus, param.getStatusList())
                                                                                    .orderByDesc(ReservationRecordPO::getCreateTime)
                                                                           );
        return PagingUtils.convert(page, ReservationRecordResult.class);
    }

    /**
     * 预约记录详情
     *
     * @param id 主键
     *
     * @return 详情
     */
    @Override
    public ReservationRecordResult reservationDetail(Long id) {
        ReservationRecordPO reservationRecordPO = reservationRecordMapper.selectById(id);
        if (Objects.isNull(reservationRecordPO)) {
            return new ReservationRecordResult();
        }
        ReservationRecordResult result = getReservationRecordResult(reservationRecordPO);
        return result;
    }

    private static ReservationRecordResult getReservationRecordResult(ReservationRecordPO reservationRecordPO) {
        ReservationRecordResult result = ObjectUtils.copying(reservationRecordPO, ReservationRecordResult.class);
        if (StringUtils.isNotBlank(reservationRecordPO.getVBarCode())) {
            String code = "data:image/jpeg;base64,";
            try {
                code = code +
                       Base64.getEncoder().encodeToString(QRCodeUtil.generateQRCode(reservationRecordPO.getVBarCode()).toByteArray());
            } catch (Exception e) {
                log.error("访客管理二维码转换失败！;{0}", e);
            }
            result.setAppointmentCode(code);
        }
        return result;
    }

    /**
     * 查看预约码
     *
     * @param id 记录id（访客id）
     *
     * @return 预约码
     */
    @Override
    public String getReservationCode(Long id) {
        return this.reservationDetail(id).getAppointmentCode();
    }

    /**
     * 取消预约
     *
     * @param id 预约id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelReservation(Long id) {
        ReservationRecordPO po = reservationRecordMapper.selectById(id);
        Assert.isNotFound(po, "预约不存在！");

        // 第三方修改状态
        lampReservationPassService.updateReservation(String.valueOf(id), 4);
        // 本地修改状态
        reservationRecordMapper.update(null, Wrappers.<ReservationRecordPO>lambdaUpdate().set(ReservationRecordPO::getStatus, 4)
                .eq(ReservationRecordPO::getId, id));
    }

    /**
     * 手机号是否预约过
     *
     * @param param 入参
     *
     * @return true表示预约过
     */
    @Override
    public Boolean phoneIsReservation(ReservationParam param) {
        List<Integer> ingStatusList = new ArrayList<>();
        ingStatusList.add(ReservationStatusEnum.DEPARTURE.getStatus());
        ingStatusList.add(ReservationStatusEnum.RESERVATION.getStatus());
        ingStatusList.add(ReservationStatusEnum.ON_THE_VISIT.getStatus());
        ReservationRecordPO po = reservationRecordMapper.selectOne(Wrappers.<ReservationRecordPO>lambdaQuery()
                                                                           .eq(ReservationRecordPO::getAreaId, param.getAreaId())
                                                                           .eq(ReservationRecordPO::getPhone, param.getPhone())
                                                                           .eq(ReservationRecordPO::getStartDateTime, param.getPeriod()
                                                                                   .getBegin())
                                                                           .eq(ReservationRecordPO::getEndDateTime, param.getPeriod()
                                                                                   .getEnd()).last(" limit 1 ")
                                                                           .in(ReservationRecordPO::getStatus, ingStatusList)
                                                                  );
        return po != null;
    }

    /**
     * 预约比率（开始时间在哪天就是哪天的数据）
     *
     * @param period 时间段
     * @param type   比率类型(DateField枚举对应的int值，天是DAY_OF_MONTH即5)
     *
     * @return 比率
     */
    @Override
    public ContrastResult getReservationStatRatio(Period period, ChronoUnit type) {
        var result = new ContrastResult();
        result.setCurrentValue(BigDecimal.ZERO.toString());
        result.setCompareValue(BigDecimal.ZERO.toString());
        result.setRatioValue(BigDecimal.ZERO.setScale(BigDecimal.ROUND_CEILING, RoundingMode.HALF_UP).toString());
        if (type == null || period == null || period.getBegin() == null || period.getEnd() == null) {
            return result;
        }
        var current = reservationRecordMapper.selectList(Wrappers.<ReservationRecordPO>lambdaQuery()
                                                                 .select(ReservationRecordPO::getId)
                                                                 .between(ReservationRecordPO::getStartDateTime, period.getBegin(), period.getEnd())
                                                        ).size();
        result.setCurrentValue(String.valueOf(current));
        var startTime = DateUtils.offset(period.getBegin(), type, -BigDecimal.ONE.intValue());
        var endTime = DateUtils.offset(period.getEnd(), type, -BigDecimal.ONE.intValue());
        var compare = reservationRecordMapper.selectList(Wrappers.<ReservationRecordPO>lambdaQuery()
                                                                 .select(ReservationRecordPO::getId)
                                                                 .between(ReservationRecordPO::getStartDateTime, startTime, endTime)
                                                        ).size();
        result.setCompareValue(String.valueOf(compare));

        if (compare <= BigDecimal.ZERO.intValue()) {
            result.setRatioValue(BigDecimal.ZERO.toString());
        } else {
            double d = ((double) current - compare) / compare;
            result.setRatioValue(BigDecimal.valueOf(d).setScale(BigDecimal.ROUND_CEILING, RoundingMode.HALF_UP).toString());
        }

        return result;
    }

    /**
     * 预约记录环比(日环比+周环比)
     * 游仙区小微产业驾驶舱定制接口
     *
     * @return 环比
     */
    @Override
    public QoqStatResult dayAndWeekRatio() {
        QoqStatResult result = new QoqStatResult();
        var nowTime = LocalDateTime.now();
        ContrastResult dayContrast = this.getReservationStatRatio(new Period(DateUtils.beginOfDay(nowTime), DateUtils.endOfDay(nowTime)), ChronoUnit.DAYS);
        ContrastResult weekContrast = this.getReservationStatRatio(new Period(DateUtils.beginOfWeek(nowTime), DateUtils.endOfWeek(nowTime)), ChronoUnit.WEEKS);
        result.setDayContrast(dayContrast);
        result.setWeekContrast(weekContrast);
        return result;
    }

    @Override
    public Long reservationRecordCount(ReservationRecordParam param) {
        Long selectCount = reservationRecordMapper.selectCount(Wrappers.<ReservationRecordPO>lambdaQuery()
                                                                       .select(ReservationRecordPO::getPhone)
                                                                       .in(ReservationRecordPO::getStatus, 2, 3)
                                                                       .like(StringUtils.isNotBlank(param.getName()), ReservationRecordPO::getName, param.getName())
                                                                       .groupBy(ReservationRecordPO::getPhone));
        return Objects.nonNull(selectCount) ? selectCount : 0;
    }

    @Override
    public Pagination<ReservationRecordResult> reservationPersonnelPage(ReservationRecordParam param) {

        Page<ReservationRecordPO> page = reservationRecordMapper.selectPage(PagingUtils.convert(param), Wrappers
                                                                                    .<ReservationRecordPO>lambdaQuery()
                                                                                    .select(ReservationRecordPO::getPhone, ReservationRecordPO::getName)
                                                                                    .like(StringUtils.isNotEmpty(param.getName()), ReservationRecordPO::getName, param.getName())
                                                                                    .in(ReservationRecordPO::getStatus, 2, 3)
                                                                                    .groupBy(ReservationRecordPO::getPhone)
                                                                                    .orderByDesc(ReservationRecordPO::getCreateTime)
                                                                           );
        return PagingUtils.convert(page, ReservationRecordResult.class);
    }

    @Override
    public ReservationRecordResult getReservationDetailByProcess(String processInsId) {
        ReservationRecordPO reservationRecordPO = reservationRecordMapper.selectOne(Wrappers.<ReservationRecordPO>lambdaQuery()
                                                                                            .eq(ReservationRecordPO::getProcessId, processInsId));
        if (Objects.isNull(reservationRecordPO)) {
            String sourceProcessId = flowClient.getSourceProcessIdByProcessId(processInsId);
            if (StringUtils.isNotBlank(sourceProcessId)) {
                reservationRecordPO = reservationRecordMapper.selectOne(Wrappers.<ReservationRecordPO>lambdaQuery()
                                                                                .eq(ReservationRecordPO::getProcessId, sourceProcessId));
            }
        }
        if (Objects.isNull(reservationRecordPO)) {
            new ReservationRecordResult();
        }
        return getReservationRecordResult(reservationRecordPO);
    }

}
