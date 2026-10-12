package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.KeyValueWithTsDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.WaterDataDayMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterDataDay;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterDataMonth;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterDataYear;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.ReportDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.UpdateDataParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.ColdWholesalerService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.WaterDataDayService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.WaterDataMonthService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.WaterDataYearService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DataDeviceVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DataTotalVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.ReportDetailDayVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WaterDataDayServiceImpl extends ServiceImpl<WaterDataDayMapper, WaterDataDay> implements WaterDataDayService {
    private final WaterDataDayMapper waterDataDayMapper;

    private final WaterDataMonthService waterDataMonthService;

    private final WaterDataYearService waterDataYearService;

    private final ColdWholesalerService wholesalerService;

    @Override
    public Map<String, Object> reportDetailDay(ReportDetailParam reportDetailParam) {
        Map<String, Object> remap = new HashMap<>();
        Page<ReportDetailDayVo> reportDetailDayVoPage = waterDataDayMapper.reportDetailDay(PagingUtils.convert(reportDetailParam), reportDetailParam);
        DataTotalVo dataTotalVo = waterDataDayMapper.reportDetailDayTotal(reportDetailParam);
        remap.put("total", dataTotalVo);
        remap.put("pageList", reportDetailDayVoPage);
        return remap;
    }

    @Override
    public Map<String, Object> reportDetailElectricity(ReportDetailParam reportDetailParam) {
        Map<String, Object> remap = new HashMap<>();
        DataTotalVo dataTotalVo = waterDataDayMapper.reportDetailDayTotal(reportDetailParam);
        Page<ReportDetailDayVo> reportDetailDayVoPage = waterDataDayMapper.reportDetailDayElectricCharge(PagingUtils.convert(reportDetailParam), reportDetailParam);
        if (CollectionUtils.isNotEmpty(reportDetailDayVoPage.getRecords())) {
            getElectricRatio(reportDetailParam, reportDetailDayVoPage.getRecords());
        }
        remap.put("total", dataTotalVo);
        remap.put("pageList", reportDetailDayVoPage);
        return remap;
    }

    @Override
    public List<ReportDetailDayVo> reportDetailDayExcel(ReportDetailParam reportDetailParam) {
        List<ReportDetailDayVo> reportDetailDayVos = waterDataDayMapper.reportDetailDayExcel(reportDetailParam);
        Integer num = 0;
        for (ReportDetailDayVo reportDetailDayVo : reportDetailDayVos) {
            num += 1;
            reportDetailDayVo.setNum(num);
        }
        return reportDetailDayVos;
    }

    @Override
    public List<ReportDetailDayVo> reportDetailDayExcelElectricCharge(ReportDetailParam reportDetailParam) {
        List<ReportDetailDayVo> reportDetailDayVos = waterDataDayMapper.reportDetailDayExcelElectricCharge(reportDetailParam);
        getElectricRatio(reportDetailParam, reportDetailDayVos);
        Integer num = 0;
        for (ReportDetailDayVo reportDetailDayVo : reportDetailDayVos) {
            num += 1;
            reportDetailDayVo.setNum(num);
        }
        return reportDetailDayVos;
    }

    @Override
    public void addData(UpdateDataParam updateDataParam) {
        List<DataDeviceVo> wholesalerDevices = wholesalerService.getWholesalerDevicesByUpdate(updateDataParam.getDeviceCode());
        var date = DateUtils.parse(updateDataParam.getDate());
        var beginOfDay = DateUtils.beginOfDay(date);
        var endOfDay = DateUtils.endOfDay(date);
        RedissonRedLock redissonRedLock = RedisUtils.getDistributedLock()
                .tryLock("waterData", Expiration.from(180000, TimeUnit.MILLISECONDS));
        try {
            if (Objects.nonNull(redissonRedLock)) {
                handleData(beginOfDay, endOfDay, wholesalerDevices, SessionContextHolder.getRequester()
                        .getTenant(), updateDataParam.getMinute());
            }
        } catch (Exception e) {
            log.error("补录错误信息===={}", e);
        } finally {
            if (Objects.nonNull(redissonRedLock)) {
                redissonRedLock.unlock();
            }
        }

    }

    @Override
    public void updateData(UpdateDataParam updateDataParam) {
        var wholesalerDevices = wholesalerService.getWholesalerDevicesByUpdate(updateDataParam.getDeviceCode());
        var date = DateUtils.parse(updateDataParam.getDate());
        var beginOfDay = DateUtils.beginOfDay(date);

        RedissonRedLock redissonRedLock = RedisUtils.getDistributedLock()
                .tryLock("waterData", Expiration.from(180000, TimeUnit.MILLISECONDS));
        try {
            if (Objects.nonNull(redissonRedLock)) {
                handleUpdateData(beginOfDay, updateDataParam.getOldMinute(), wholesalerDevices, SessionContextHolder.getRequester()
                        .getTenant(), updateDataParam.getMinute());
            }
        } catch (Exception e) {
            log.error("修改错误信息===={}", e);
        } finally {
            if (Objects.nonNull(redissonRedLock)) {
                redissonRedLock.unlock();
            }
        }
    }

    private void handleData(LocalDateTime beginTime, LocalDateTime endTime, List<DataDeviceVo> dataDeviceVos, Integer tenant,
                            BigDecimal minuteValue
                           ) {
        System.out.println("冷冻水数据修改================================");
        SessionContextHolder.requestTenant(tenant);
        String format = DateUtils.format(beginTime, "yyyy-MM");
        String formatYear = DateUtils.format(beginTime, "yyyy");
        List<String> ids = dataDeviceVos.stream().map(DataDeviceVo::getDeviceCode).collect(Collectors.toList());
        List<WaterDataMonth> months = waterDataMonthService.list(Wrappers.<WaterDataMonth>lambdaQuery()
                                                                         .in(WaterDataMonth::getDeviceCode, ids)
                                                                         .eq(WaterDataMonth::getDataTime, format));
        List<WaterDataYear> years = waterDataYearService.list(Wrappers.<WaterDataYear>lambdaQuery()
                                                                      .in(WaterDataYear::getDeviceCode, ids)
                                                                      .eq(WaterDataYear::getDataTime, formatYear));
        Map<String, WaterDataMonth> monthMap = months.stream().collect(Collectors.toMap(WaterDataMonth::getDeviceCode, s -> s));
        Map<String, WaterDataYear> yearMap = years.stream().collect(Collectors.toMap(WaterDataYear::getDeviceCode, s -> s));
        for (DataDeviceVo dataDeviceVo : dataDeviceVos) {
            try {
                KeyValueWithTsDTO historicalAttributeChangeDTO = new KeyValueWithTsDTO();
                historicalAttributeChangeDTO.setValue(minuteValue.toString());
                if (Objects.nonNull(historicalAttributeChangeDTO) && Objects.nonNull(historicalAttributeChangeDTO.getValue())) {
                    WaterDataDay waterDataDayExist = this.getOne(Wrappers.<WaterDataDay>lambdaQuery()
                                                                         .eq(WaterDataDay::getDataTime, beginTime)
                                                                         .eq(WaterDataDay::getDeviceCode, dataDeviceVo.getDeviceCode()));
                    if (Objects.isNull(waterDataDayExist)) {
                        BigDecimal readeValue = new BigDecimal(historicalAttributeChangeDTO.getValue()
                                                                       .toString()).setScale(2, RoundingMode.UP);
                        BigDecimal hour = readeValue.divide(new BigDecimal("60"), 2, RoundingMode.UP);
                        BigDecimal flow = hour.multiply(new BigDecimal(dataDeviceVo.getUnitTraffic())).setScale(2, RoundingMode.UP);
                        BigDecimal dayTotal = flow.multiply(dataDeviceVo.getUnitPrice()).setScale(2, RoundingMode.UP);
                        //天统计
                        WaterDataDay waterDataDay = new WaterDataDay();
                        waterDataDay.setCode(dataDeviceVo.getCode());
                        waterDataDay.setDepartmentId(dataDeviceVo.getDepartmentId());
                        waterDataDay.setGearPositionAreaId(dataDeviceVo.getGearPositionAreaId());
                        waterDataDay.setGearPositionCode(dataDeviceVo.getGearPositionCode());
                        waterDataDay.setDeviceCode(dataDeviceVo.getDeviceCode());
                        waterDataDay.setShiftMaster(dataDeviceVo.getShiftMaster());
                        waterDataDay.setUnitPrice(dataDeviceVo.getUnitPrice());
                        waterDataDay.setDepartment(dataDeviceVo.getDepartment());
                        waterDataDay.setGearPositionArea(dataDeviceVo.getGearPositionArea());
                        waterDataDay.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                        waterDataDay.setFlow(flow);
                        waterDataDay.setDuration(hour);
                        waterDataDay.setTotal(dayTotal);
                        waterDataDay.setDataTime(DateUtils.format(beginTime, "yyyy-MM-dd"));
                        Integer month = beginTime.getMonthValue();
                        Integer day = beginTime.getDayOfMonth();
                        waterDataDay.setStatisticalFlag(month + "月" + day + "日");
                        this.save(waterDataDay);
                        //月统计
                        WaterDataMonth waterDataMonth = monthMap.get(dataDeviceVo.getDeviceCode());
                        int monthFlag = beginTime.getMonthValue();
                        if (Objects.nonNull(waterDataMonth)) {
                            waterDataMonth.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                            waterDataMonth.setFlow(waterDataMonth.getFlow().add(flow));
                            waterDataMonth.setTotal(waterDataMonth.getTotal().add(dayTotal));
                            waterDataMonth.setUnitPrice(dataDeviceVo.getUnitPrice());
                            waterDataMonth.setDuration(waterDataMonth.getDuration().add(hour));
                            waterDataMonthService.updateById(waterDataMonth);
                        } else {
                            WaterDataMonth waterDataMonth1 = new WaterDataMonth();
                            waterDataMonth1.setDataTime(format);
                            waterDataMonth1.setFlow(flow);
                            waterDataMonth1.setUnitPrice(dataDeviceVo.getUnitPrice());
                            waterDataMonth1.setCode(dataDeviceVo.getCode());
                            waterDataMonth1.setDuration(hour);
                            waterDataMonth1.setDepartmentId(dataDeviceVo.getDepartmentId());
                            waterDataMonth1.setGearPositionAreaId(dataDeviceVo.getGearPositionAreaId());
                            waterDataMonth1.setTotal(dayTotal);
                            waterDataMonth1.setDepartment(dataDeviceVo.getDepartment());
                            waterDataMonth1.setGearPositionArea(dataDeviceVo.getGearPositionArea());
                            waterDataMonth1.setDeviceCode(dataDeviceVo.getDeviceCode());
                            waterDataMonth1.setShiftMaster(dataDeviceVo.getShiftMaster());
                            waterDataMonth1.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                            waterDataMonth1.setShiftMasterPhone(dataDeviceVo.getShiftMasterPhone());
                            var time = DateUtils.beginOfMonth(beginTime);
                            int month1 = time.getDayOfMonth();
                            int month2 = beginTime.getDayOfMonth();
                            String format1 = DateUtils.format(dataDeviceVo.getBillableTime(), "yyyy-MM");
                            String format2 = DateUtils.format(beginTime, "yyyy-MM");
                            if (format1.equals(format2)) {
                                month1 = dataDeviceVo.getBillableTime().getDayOfMonth();
                            }
                            waterDataMonth1.setStatisticalCycle(month1 + "-" + month2);
                            waterDataMonth1.setStatisticalFlag(monthFlag + "月");
                            waterDataMonth1.setGearPositionCode(dataDeviceVo.getGearPositionCode());
                            waterDataMonthService.save(waterDataMonth1);
                        }
                        //年统计
                        WaterDataYear waterDataYear = yearMap.get(dataDeviceVo.getDeviceCode());
                        if (Objects.nonNull(waterDataYear)) {
                            waterDataYear.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                            waterDataYear.setFlow(waterDataYear.getFlow().add(flow));
                            waterDataYear.setDuration(waterDataYear.getDuration().add(hour));
                            if (monthFlag == 1) {
                                waterDataYear.setOne(waterDataYear.getOne().add(dayTotal));
                            } else if (monthFlag == 2) {
                                waterDataYear.setTwo(waterDataYear.getTwo().add(dayTotal));
                            } else if (monthFlag == 3) {
                                waterDataYear.setThree(waterDataYear.getThree().add(dayTotal));
                            } else if (monthFlag == 4) {
                                waterDataYear.setFour(waterDataYear.getFour().add(dayTotal));
                            } else if (monthFlag == 5) {
                                waterDataYear.setFive(waterDataYear.getFive().add(dayTotal));
                            } else if (monthFlag == 6) {
                                waterDataYear.setSix(waterDataYear.getSix().add(dayTotal));
                            } else if (monthFlag == 7) {
                                waterDataYear.setSeven(waterDataYear.getSeven().add(dayTotal));
                            } else if (monthFlag == 8) {
                                waterDataYear.setEight(waterDataYear.getEight().add(dayTotal));
                            } else if (monthFlag == 9) {
                                waterDataYear.setNine(waterDataYear.getNine().add(dayTotal));
                            } else if (monthFlag == 10) {
                                waterDataYear.setTen(waterDataYear.getTen().add(dayTotal));
                            } else if (monthFlag == 11) {
                                waterDataYear.setEleven(waterDataYear.getEleven().add(dayTotal));
                            } else if (monthFlag == 12) {
                                waterDataYear.setTwelve(waterDataYear.getTwelve().add(dayTotal));
                            }
                            BigDecimal yearTotal = BigDecimal.ZERO;
                            BigDecimal bigDecimal = Objects.isNull(waterDataYear.getOne()) ? BigDecimal.ZERO : waterDataYear.getOne();
                            BigDecimal bigDecimal1 = Objects.isNull(waterDataYear.getTwo()) ? BigDecimal.ZERO : waterDataYear.getTwo();
                            BigDecimal bigDecimal2 = Objects.isNull(waterDataYear.getThree()) ? BigDecimal.ZERO : waterDataYear.getThree();
                            BigDecimal bigDecimal3 = Objects.isNull(waterDataYear.getFour()) ? BigDecimal.ZERO : waterDataYear.getFour();
                            BigDecimal bigDecimal4 = Objects.isNull(waterDataYear.getFive()) ? BigDecimal.ZERO : waterDataYear.getFive();
                            BigDecimal bigDecimal5 = Objects.isNull(waterDataYear.getSix()) ? BigDecimal.ZERO : waterDataYear.getSix();
                            BigDecimal bigDecimal6 = Objects.isNull(waterDataYear.getSeven()) ? BigDecimal.ZERO : waterDataYear.getSeven();
                            BigDecimal bigDecimal7 = Objects.isNull(waterDataYear.getEight()) ? BigDecimal.ZERO : waterDataYear.getEight();
                            BigDecimal bigDecimal8 = Objects.isNull(waterDataYear.getNine()) ? BigDecimal.ZERO : waterDataYear.getNine();
                            BigDecimal bigDecimal9 = Objects.isNull(waterDataYear.getTen()) ? BigDecimal.ZERO : waterDataYear.getTen();
                            BigDecimal bigDecimal10 =
                                    Objects.isNull(waterDataYear.getEleven()) ? BigDecimal.ZERO : waterDataYear.getEleven();
                            BigDecimal bigDecimal11 =
                                    Objects.isNull(waterDataYear.getTwelve()) ? BigDecimal.ZERO : waterDataYear.getTwelve();
                            yearTotal = yearTotal.add(bigDecimal).add(bigDecimal1).add(bigDecimal2).add(bigDecimal3).add(bigDecimal4)
                                    .add(bigDecimal5).add(bigDecimal6).add(bigDecimal7).add(bigDecimal8).add(bigDecimal9).add(bigDecimal10)
                                    .add(bigDecimal11);
                            waterDataYear.setTotal(yearTotal);
                            waterDataYearService.updateById(waterDataYear);
                        } else {
                            WaterDataYear waterDataYear1 = new WaterDataYear();
                            waterDataYear1.setCode(dataDeviceVo.getCode());
                            waterDataYear1.setDataTime(formatYear);
                            waterDataYear1.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                            waterDataYear1.setGearPositionCode(dataDeviceVo.getGearPositionCode());
                            waterDataYear1.setDepartmentId(dataDeviceVo.getDepartmentId());
                            waterDataYear1.setGearPositionAreaId(dataDeviceVo.getGearPositionAreaId());
                            waterDataYear1.setDeviceCode(dataDeviceVo.getDeviceCode());
                            waterDataYear1.setDepartment(dataDeviceVo.getDepartment());
                            waterDataYear1.setGearPositionArea(dataDeviceVo.getGearPositionArea());
                            waterDataYear1.setShiftMaster(dataDeviceVo.getShiftMaster());
                            waterDataYear1.setFlow(flow);
                            waterDataYear1.setDuration(hour);
                            if (monthFlag == 1) {
                                waterDataYear1.setOne(dayTotal);
                            } else if (monthFlag == 2) {
                                waterDataYear1.setTwo(dayTotal);
                            } else if (monthFlag == 3) {
                                waterDataYear1.setThree(dayTotal);
                            } else if (monthFlag == 4) {
                                waterDataYear1.setFour(dayTotal);
                            } else if (monthFlag == 5) {
                                waterDataYear1.setFive(dayTotal);
                            } else if (monthFlag == 6) {
                                waterDataYear1.setSix(dayTotal);
                            } else if (monthFlag == 7) {
                                waterDataYear1.setSeven(dayTotal);
                            } else if (monthFlag == 8) {
                                waterDataYear1.setEight(dayTotal);
                            } else if (monthFlag == 9) {
                                waterDataYear1.setNine(dayTotal);
                            } else if (monthFlag == 10) {
                                waterDataYear1.setTen(dayTotal);
                            } else if (monthFlag == 11) {
                                waterDataYear1.setEleven(dayTotal);
                            } else if (monthFlag == 12) {
                                waterDataYear1.setTwelve(dayTotal);
                            }
                            waterDataYear1.setTotal(dayTotal);
                            waterDataYearService.save(waterDataYear1);
                        }
                    }

                }
                System.out.println("冷冻水数据修改================================");
            } catch (Exception e) {
                log.error("冷冻水数据修改==={}", e);
            }
        }

    }

    private void handleUpdateData(LocalDateTime beginTime, BigDecimal minuteOldValue, List<DataDeviceVo> dataDeviceVos, Integer tenant,
                                  BigDecimal minuteValue
                                 ) {
        System.out.println("冷冻水数据修改================================");
        SessionContextHolder.requestTenant(tenant);
        String format = DateUtils.format(beginTime, "yyyy-MM");
        String formatYear = DateUtils.format(beginTime, "yyyy");
        List<String> ids = dataDeviceVos.stream().map(DataDeviceVo::getDeviceCode).collect(Collectors.toList());
        List<WaterDataMonth> months = waterDataMonthService.list(Wrappers.<WaterDataMonth>lambdaQuery()
                                                                         .in(WaterDataMonth::getDeviceCode, ids)
                                                                         .eq(WaterDataMonth::getDataTime, format));
        List<WaterDataYear> years = waterDataYearService.list(Wrappers.<WaterDataYear>lambdaQuery()
                                                                      .in(WaterDataYear::getDeviceCode, ids)
                                                                      .eq(WaterDataYear::getDataTime, formatYear));
        Map<String, WaterDataMonth> monthMap = months.stream().collect(Collectors.toMap(WaterDataMonth::getDeviceCode, s -> s));
        Map<String, WaterDataYear> yearMap = years.stream().collect(Collectors.toMap(WaterDataYear::getDeviceCode, s -> s));
        for (DataDeviceVo dataDeviceVo : dataDeviceVos) {
            try {
                KeyValueWithTsDTO historicalAttributeChangeDTO = new KeyValueWithTsDTO();
                historicalAttributeChangeDTO.setValue(minuteValue.toString());
                if (Objects.nonNull(historicalAttributeChangeDTO) && Objects.nonNull(historicalAttributeChangeDTO.getValue())) {
                    //错值计算
                    BigDecimal hourOld = minuteOldValue.divide(new BigDecimal("60"), 2, RoundingMode.UP);
                    BigDecimal flowOld = hourOld.multiply(new BigDecimal(dataDeviceVo.getUnitTraffic())).setScale(2, RoundingMode.UP);
                    BigDecimal dayTotalOld = flowOld.multiply(dataDeviceVo.getUnitPrice()).setScale(2, RoundingMode.UP);

                    BigDecimal readeValue = new BigDecimal(historicalAttributeChangeDTO.getValue().toString()).setScale(2, RoundingMode.UP);
                    BigDecimal hour = readeValue.divide(new BigDecimal("60"), 2, RoundingMode.UP);
                    BigDecimal flow = hour.multiply(new BigDecimal(dataDeviceVo.getUnitTraffic())).setScale(2, RoundingMode.UP);
                    BigDecimal dayTotal = flow.multiply(dataDeviceVo.getUnitPrice()).setScale(2, RoundingMode.UP);
                    //天统计
                    WaterDataDay waterDataDay = this.getOne(Wrappers.<WaterDataDay>lambdaQuery()
                                                                    .eq(WaterDataDay::getDataTime, beginTime)
                                                                    .eq(WaterDataDay::getDeviceCode, dataDeviceVo.getDeviceCode()));
                    if (Objects.nonNull(waterDataDay)) {
                        waterDataDay.setFlow(flow);
                        waterDataDay.setTotal(dayTotal);
                        waterDataDay.setDuration(hour);
                        this.updateById(waterDataDay);

                        //月统计
                        WaterDataMonth waterDataMonth = monthMap.get(dataDeviceVo.getDeviceCode());
                        int monthFlag = beginTime.getMonthValue();
                        if (Objects.nonNull(waterDataMonth)) {
                            waterDataMonth.setUnitTraffic(new BigDecimal(dataDeviceVo.getUnitTraffic()));
                            waterDataMonth.setFlow(waterDataMonth.getFlow().subtract(flowOld).add(flow));
                            waterDataMonth.setTotal(waterDataMonth.getTotal().subtract(dayTotalOld).add(dayTotal));
                            waterDataMonth.setDuration(waterDataMonth.getDuration().subtract(hourOld).add(hour));
                            waterDataMonthService.updateById(waterDataMonth);
                        }
                        //年统计
                        WaterDataYear waterDataYear = yearMap.get(dataDeviceVo.getDeviceCode());
                        if (Objects.nonNull(waterDataYear)) {
                            waterDataYear.setFlow(waterDataYear.getFlow().subtract(flowOld).add(flow));
                            waterDataYear.setDuration(waterDataYear.getDuration().subtract(hourOld).add(hour));
                            if (monthFlag == 1) {
                                waterDataYear.setOne(waterDataYear.getOne().subtract(dayTotalOld).add(dayTotal));
                            } else if (monthFlag == 2) {
                                waterDataYear.setTwo(waterDataYear.getTwo().subtract(dayTotalOld).add(dayTotal));
                            } else if (monthFlag == 3) {
                                waterDataYear.setThree(waterDataYear.getThree().subtract(dayTotalOld).add(dayTotal));
                            } else if (monthFlag == 4) {
                                waterDataYear.setFour(waterDataYear.getFour().subtract(dayTotalOld).add(dayTotal));
                            } else if (monthFlag == 5) {
                                waterDataYear.setFive(waterDataYear.getFive().subtract(dayTotalOld).add(dayTotal));
                            } else if (monthFlag == 6) {
                                waterDataYear.setSix(waterDataYear.getSix().subtract(dayTotalOld).add(dayTotal));
                            } else if (monthFlag == 7) {
                                waterDataYear.setSeven(waterDataYear.getSeven().subtract(dayTotalOld).add(dayTotal));
                            } else if (monthFlag == 8) {
                                waterDataYear.setEight(waterDataYear.getEight().subtract(dayTotalOld).add(dayTotal));
                            } else if (monthFlag == 9) {
                                waterDataYear.setNine(waterDataYear.getNine().subtract(dayTotalOld).add(dayTotal));
                            } else if (monthFlag == 10) {
                                waterDataYear.setTen(waterDataYear.getTen().subtract(dayTotalOld).add(dayTotal));
                            } else if (monthFlag == 11) {
                                waterDataYear.setEleven(waterDataYear.getEleven().subtract(dayTotalOld).add(dayTotal));
                            } else if (monthFlag == 12) {
                                waterDataYear.setTwelve(waterDataYear.getTwelve().subtract(dayTotalOld).add(dayTotal));
                            }
                            BigDecimal yearTotal = BigDecimal.ZERO;
                            BigDecimal bigDecimal = Objects.isNull(waterDataYear.getOne()) ? BigDecimal.ZERO : waterDataYear.getOne();
                            BigDecimal bigDecimal1 = Objects.isNull(waterDataYear.getTwo()) ? BigDecimal.ZERO : waterDataYear.getTwo();
                            BigDecimal bigDecimal2 = Objects.isNull(waterDataYear.getThree()) ? BigDecimal.ZERO : waterDataYear.getThree();
                            BigDecimal bigDecimal3 = Objects.isNull(waterDataYear.getFour()) ? BigDecimal.ZERO : waterDataYear.getFour();
                            BigDecimal bigDecimal4 = Objects.isNull(waterDataYear.getFive()) ? BigDecimal.ZERO : waterDataYear.getFive();
                            BigDecimal bigDecimal5 = Objects.isNull(waterDataYear.getSix()) ? BigDecimal.ZERO : waterDataYear.getSix();
                            BigDecimal bigDecimal6 = Objects.isNull(waterDataYear.getSeven()) ? BigDecimal.ZERO : waterDataYear.getSeven();
                            BigDecimal bigDecimal7 = Objects.isNull(waterDataYear.getEight()) ? BigDecimal.ZERO : waterDataYear.getEight();
                            BigDecimal bigDecimal8 = Objects.isNull(waterDataYear.getNine()) ? BigDecimal.ZERO : waterDataYear.getNine();
                            BigDecimal bigDecimal9 = Objects.isNull(waterDataYear.getTen()) ? BigDecimal.ZERO : waterDataYear.getTen();
                            BigDecimal bigDecimal10 =
                                    Objects.isNull(waterDataYear.getEleven()) ? BigDecimal.ZERO : waterDataYear.getEleven();
                            BigDecimal bigDecimal11 =
                                    Objects.isNull(waterDataYear.getTwelve()) ? BigDecimal.ZERO : waterDataYear.getTwelve();
                            yearTotal = yearTotal.add(bigDecimal).add(bigDecimal1).add(bigDecimal2).add(bigDecimal3).add(bigDecimal4)
                                    .add(bigDecimal5).add(bigDecimal6).add(bigDecimal7).add(bigDecimal8).add(bigDecimal9).add(bigDecimal10)
                                    .add(bigDecimal11);
                            waterDataYear.setTotal(yearTotal);
                            waterDataYearService.updateById(waterDataYear);
                        }
                    }

                }
                System.out.println("冷冻水数据修改================================");
            } catch (Exception e) {
                log.error("冷冻水数据修改==={}", e);
            }
        }

    }

    private void getElectricRatio(ReportDetailParam reportDetailParam, List<ReportDetailDayVo> list) {
        var st = DateUtils.parse(reportDetailParam.getStartTime());
        var et = DateUtils.parse(reportDetailParam.getEndTime());
        List<String> deviceCodes = list.stream().map(ReportDetailDayVo::getDeviceCode).collect(Collectors.toList());
        reportDetailParam.setDeviceCodes(deviceCodes);
        //时间间隔
        int between = (int) DateUtils.between(st, et, ChronoUnit.DAYS) + 1;
        //环比时间
        var stQOQ = DateUtils.offset(st, ChronoUnit.DAYS, -between);
        var etQOQ = DateUtils.offset(et, ChronoUnit.DAYS, -between);
        reportDetailParam.setStartTime(DateUtils.format(stQOQ, "yyyy-MM-dd"));
        reportDetailParam.setEndTime(DateUtils.format(etQOQ, "yyyy-MM-dd"));
        List<ReportDetailDayVo> reportDetailDayVoPageQOQ = waterDataDayMapper.reportDetailDayExcelElectricCharge(reportDetailParam);
        var collectQOQ = reportDetailDayVoPageQOQ.stream()
                .collect(Collectors.toMap(ReportDetailDayVo::getDeviceCode, s -> s));
        //同比时间
        var stYOY = DateUtils.offset(st, ChronoUnit.YEARS, -1);
        var etYOY = DateUtils.offset(et, ChronoUnit.YEARS, -1);
        reportDetailParam.setStartTime(DateUtils.format(stYOY, "yyyy-MM-dd"));
        reportDetailParam.setEndTime(DateUtils.format(etYOY, "yyyy-MM-dd"));
        List<ReportDetailDayVo> reportDetailDayVoPageYOY = waterDataDayMapper.reportDetailDayExcelElectricCharge(reportDetailParam);
        Map<String, ReportDetailDayVo> collectYOY = reportDetailDayVoPageYOY.stream()
                .collect(Collectors.toMap(ReportDetailDayVo::getDeviceCode, s -> s));
        for (ReportDetailDayVo reportDetailDayVo : list) {
            //环比
            if (MapUtils.isNotEmpty(collectQOQ)) {
                ReportDetailDayVo reportDetailDayVoQOQ = collectQOQ.get(reportDetailDayVo.getDeviceCode());
                if (Objects.nonNull(reportDetailDayVoQOQ)) {
                    reportDetailDayVo.setDurationQOQ(reportDetailDayVoQOQ.getDuration());
                    reportDetailDayVo.setFlowQOQ(reportDetailDayVoQOQ.getFlow());
                    reportDetailDayVo.setTotalQOQ(reportDetailDayVoQOQ.getTotal());
                    reportDetailDayVo.setDurationQOQRatio(
                            reportDetailDayVoQOQ.getDuration().compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal(100)
                                                                                               : reportDetailDayVo.getDuration()
                                    .subtract(reportDetailDayVoQOQ.getDuration())
                                    .divide(reportDetailDayVoQOQ.getDuration(), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                    reportDetailDayVo.setFlowQOQRatio(reportDetailDayVoQOQ.getFlow().compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal(100)
                                                                                                                     : reportDetailDayVo
                                                              .getFlow().subtract(reportDetailDayVoQOQ.getFlow())
                                                              .divide(reportDetailDayVoQOQ.getFlow(), 2, RoundingMode.HALF_UP)
                                                              .multiply(new BigDecimal(100)));
                    reportDetailDayVo.setTotalQOQRatio(reportDetailDayVoQOQ.getTotal().compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal(100)
                                                                                                                       : reportDetailDayVo
                                                               .getTotal().subtract(reportDetailDayVoQOQ.getTotal())
                                                               .divide(reportDetailDayVoQOQ.getTotal(), 2, RoundingMode.HALF_UP)
                                                               .multiply(new BigDecimal(100)));
                }
            }
            //同比
            if (MapUtils.isNotEmpty(collectYOY)) {
                ReportDetailDayVo reportDetailDayVoYOY = collectYOY.get(reportDetailDayVo.getDeviceCode());
                if (Objects.nonNull(reportDetailDayVoYOY)) {
                    reportDetailDayVo.setDurationYOY(reportDetailDayVoYOY.getDuration());
                    reportDetailDayVo.setFlowYOY(reportDetailDayVoYOY.getFlow());
                    reportDetailDayVo.setTotalYOY(reportDetailDayVoYOY.getTotal());
                    reportDetailDayVo.setDurationYOYRatio(
                            reportDetailDayVoYOY.getDuration().compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal(100)
                                                                                               : reportDetailDayVo.getDuration()
                                    .subtract(reportDetailDayVoYOY.getDuration())
                                    .divide(reportDetailDayVoYOY.getDuration(), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                    reportDetailDayVo.setFlowYOYRatio(reportDetailDayVoYOY.getFlow().compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal(100)
                                                                                                                     : reportDetailDayVo
                                                              .getFlow().subtract(reportDetailDayVoYOY.getFlow())
                                                              .divide(reportDetailDayVoYOY.getFlow(), 2, RoundingMode.HALF_UP)
                                                              .multiply(new BigDecimal(100)));
                    reportDetailDayVo.setTotalYOYRatio(reportDetailDayVoYOY.getTotal().compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal(100)
                                                                                                                       : reportDetailDayVo
                                                               .getTotal().subtract(reportDetailDayVoYOY.getTotal())
                                                               .divide(reportDetailDayVoYOY.getTotal(), 2, RoundingMode.HALF_UP)
                                                               .multiply(new BigDecimal(100)));
                }
            }
        }

    }

}
