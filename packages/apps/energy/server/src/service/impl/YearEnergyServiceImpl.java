package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyCollectConfigMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyDailyUsageMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.YearEnergyMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyCollectConfig;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailyUsage;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.YearEnergy;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.YearEnergyParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageStatParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.YearEnergyQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.YearEnergyService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyCollectConfigVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyTypeStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.YearEnergyVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 年用能服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class YearEnergyServiceImpl extends ServiceImpl<YearEnergyMapper, YearEnergy> implements YearEnergyService {

    private final EnergyCollectConfigMapper energyCollectConfigMapper;
    private final EnergyDailyUsageMapper energyDailyUsageMapper;

    /**
     * 获取年用能分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @Override
    public Pagination<YearEnergyVO> getYearEnergyPage(YearEnergyQueryParam param) {
        Page<YearEnergy> page = this.page(PagingUtils.convert(param), Wrappers.<YearEnergy>lambdaQuery()
                                                  .eq(StringUtils.isNotBlank(param.getYear()), YearEnergy::getYear, param.getYear())
                                                  .eq(StringUtils.isNotBlank(param.getInstrumentType()), YearEnergy::getInstrumentType, param.getInstrumentType())
                                                  .orderByDesc(YearEnergy::getDateYear)
                                         );
        return PagingUtils.convert(page, YearEnergyVO.class);
    }

    /**
     * 年用能详情
     *
     * @param param id
     *
     * @return 详情
     */
    @Override
    public YearEnergyVO getYearEnergyDetail(YearEnergyQueryParam param) {
        return ObjectUtils.copying(this.getById(param.getId()), YearEnergyVO.class);
    }

    /**
     * 新增年用能
     *
     * @param param 用能
     *
     * @return id
     */
    @Override
    public Long addYearEnergy(YearEnergyParam param) {
        YearEnergy oldEnergy = this.getOne(Wrappers.<YearEnergy>lambdaQuery()
                                                   .eq(YearEnergy::getYear, param.getYear())
                                                   .eq(YearEnergy::getInstrumentType, param.getInstrumentType())
                                                   .last(" limit 1 ")
                                          );
        Assert.isConflict(oldEnergy != null, "已存在" + param.getYear() + "年的" + param.getInstrumentName() + "用能!");
        YearEnergy yearEnergy = ObjectUtils.copying(param, YearEnergy.class);
        yearEnergy.setDateYear(DateUtils.parseDate(param.getYear() + "-01-01"));
        // 设置碳排和综合能耗
        this.setDzValue(yearEnergy);
        this.save(yearEnergy);
        return yearEnergy.getId();
    }

    /**
     * 编辑年用能
     *
     * @param param 用能
     */
    @Override
    public void editYearEnergy(YearEnergyParam param) {
        YearEnergy yearEnergy = this.getById(param.getId());
        Assert.isNotFound(yearEnergy, "数据不存在，请检查是否被删除！");
        BeanUtils.copyProperties(param, yearEnergy);
        YearEnergy oldEnergy = this.getOne(Wrappers.<YearEnergy>lambdaQuery()
                                                   .eq(YearEnergy::getYear, param.getYear())
                                                   .eq(YearEnergy::getInstrumentType, param.getInstrumentType())
                                                   .ne(YearEnergy::getId, param.getId())
                                                   .last(" limit 1 ")
                                          );
        Assert.isConflict(oldEnergy != null, "已存在" + param.getYear() + "年的" + param.getInstrumentName() + "用能!");
        if (StringUtils.isNotBlank(yearEnergy.getYear())) {
            yearEnergy.setDateYear(DateUtils.parseDate(yearEnergy.getYear() + "-01-01"));
        }
        EntityAuditor.populateUpdateAudit(yearEnergy);
        this.setDzValue(yearEnergy);
        this.updateById(yearEnergy);
    }

    /**
     * 删除年用能
     *
     * @param id 主键
     */
    @Override
    public void removeYearEnergy(Long id) {
        this.update(Wrappers.<YearEnergy>lambdaUpdate()
                            .eq(YearEnergy::getId, id)
                            .set(YearEnergy::getDeleted, System.currentTimeMillis())
                   );
    }

    /**
     * 年用能趋势
     *
     * @param param 时间段，用能类型
     *
     * @return 趋势
     */
    @Override
    public List<EnergyTypeStatVO> getYearEnergyTrend(YearEnergyQueryParam param) {
        if (param == null || StringUtils.isBlank(param.getInstrumentType())) {
            throw new ServiceException("用能类型不能为空！");
        }
        LocalDateTime newDate = LocalDateTime.now();
        if (param.getPeriod() == null || param.getPeriod().getBegin() == null || param.getPeriod().getEnd() == null) {
            param.setPeriod(Period.of(DateUtils.endOfYear(newDate), DateUtils.beginOfYear(newDate).minusYears(10)));
        }
        List<EnergyTypeStatVO> list = this.baseMapper.getYearEnergyTrend(param);
        // 如果不是最后一天(今天的天数小于今年的天数),需要判断今年趋势(今年开始到当前日期的每日平均用能 x 今年的天数)
        // 今年的天数
        long yearDay = DateUtils.between(DateUtils.beginOfYear(newDate), DateUtils.endOfYear(newDate), ChronoUnit.DAYS);
        // 今天的天数
        long betweenDay = DateUtils.between(DateUtils.beginOfYear(newDate), newDate, ChronoUnit.DAYS);
        if (betweenDay < yearDay && list != null && !list.isEmpty()) {
            // 获取今年的日用能
            EnergyDailyUsageParam newYearParam = new EnergyDailyUsageParam();
            newYearParam.setPeriod(param.getPeriod());
            newYearParam.setInstrumentType(param.getInstrumentType());
            EnergyTypeStatVO typeStatVO = energyDailyUsageMapper.getUsageStatAllIn(Wrappers.<EnergyDailyUsage>lambdaQuery()
                                                                                           .eq(EnergyDailyUsage::getInstrumentType, param.getInstrumentType())
                                                                                           .ge(EnergyDailyUsage::getStatDate, param
                                                                                                   .getPeriod().getBegin())
                                                                                           .le(EnergyDailyUsage::getStatDate, param
                                                                                                   .getPeriod().getEnd())
                                                                                  );
            BigDecimal ddValue = BigDecimal.ZERO;
            BigDecimal dcValue = BigDecimal.ZERO;
            BigDecimal zhValue = BigDecimal.ZERO;
            if (typeStatVO != null) {
                if (typeStatVO.getDdValue() != null) {
                    ddValue = typeStatVO.getDdValue().divide(BigDecimal.valueOf(betweenDay), 2, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(yearDay));
                }
                if (typeStatVO.getDcValue() != null) {
                    dcValue = typeStatVO.getDcValue().divide(BigDecimal.valueOf(betweenDay), 2, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(yearDay));
                }
                if (typeStatVO.getZhValue() != null) {
                    zhValue = typeStatVO.getZhValue().divide(BigDecimal.valueOf(betweenDay), 2, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(yearDay));
                }
            }
            // 组装计算出来的预期趋势
            EnergyTypeStatVO newYearTrend = new EnergyTypeStatVO();
            String year = DateUtils.format(newDate, "YYYY");
            newYearTrend.setTime(year);
            newYearTrend.setDdValue(ddValue);
            newYearTrend.setDcValue(dcValue);
            newYearTrend.setZhValue(zhValue);

            // 如果最后一条是今年的，就替换最后一条，如果不是就添加今年的数据。(按年排序的，最后一条是最大的年份)
            EnergyTypeStatVO listLastVO = list.get(list.size() - 1);
            if (DateUtils.format(newDate, "YYYY").equals(listLastVO.getTime())) {
                list.set(list.size() - 1, newYearTrend);
            } else {
                list.add(newYearTrend);
            }
        }

        return list;
    }

    /**
     * 年用能日折算
     *
     * @param param 类型和时间段
     *
     * @return 用能
     */
    @Override
    public EnergyTypeStatVO getYearEnergyConversionDaily(EnergyDailyUsageStatParam param) {
        if (param == null || param.getPeriod() == null || param.getPeriod().getBegin() == null || param.getPeriod().getEnd() == null) {
            throw new ServiceException("参数不能为空！");
        }
        EnergyTypeStatVO result = new EnergyTypeStatVO();
        result.setDdValue(BigDecimal.ZERO);
        result.setDcValue(BigDecimal.ZERO);
        result.setZhValue(BigDecimal.ZERO);
        String startYearStr = DateUtils.format(param.getPeriod().getBegin(), "yyyy");
        YearEnergy yearEnergy = this.getOne(Wrappers.<YearEnergy>lambdaQuery()
                                                    .eq(YearEnergy::getYear, startYearStr)
                                                    .eq(StringUtils.isNotBlank(param.getInstrumentType()), YearEnergy::getInstrumentType, param.getInstrumentType())
                                                    .last(" limit 1 ")
                                           );
        if (yearEnergy == null) {
            return result;
        }
        result.setInstrumentType(yearEnergy.getInstrumentType());
        result.setInstrumentTypeName(yearEnergy.getInstrumentName());
        BigDecimal ddValue = yearEnergy.getEnergyValue() == null ? BigDecimal.ZERO : yearEnergy.getEnergyValue();
        BigDecimal dcValue = yearEnergy.getDcValue() == null ? BigDecimal.ZERO : yearEnergy.getDcValue();
        BigDecimal zhValue = yearEnergy.getZhValue() == null ? BigDecimal.ZERO : yearEnergy.getZhValue();
        // 时间段的开始时间所在年的天数
        LocalDateTime startYearTime = DateUtils.beginOfYear(param.getPeriod().getBegin());
        long yearDay = DateUtils.between(startYearTime, DateUtils.endOfYear(startYearTime), ChronoUnit.DAYS);
        // 开始到结束的天数
        long betweenDay = DateUtils.between(param.getPeriod().getBegin(), param.getPeriod().getEnd(), ChronoUnit.DAYS);
        if (betweenDay < yearDay) {
            if (yearEnergy.getEnergyValue() != null) {
                ddValue = yearEnergy.getEnergyValue().divide(BigDecimal.valueOf(yearDay), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(betweenDay));
            }
            if (yearEnergy.getDcValue() != null) {
                dcValue = yearEnergy.getDcValue().divide(BigDecimal.valueOf(yearDay), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(betweenDay));
            }
            if (yearEnergy.getZhValue() != null) {
                zhValue = yearEnergy.getZhValue().divide(BigDecimal.valueOf(yearDay), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(betweenDay));
            }
        }

        result.setDdValue(ddValue);
        result.setDcValue(dcValue);
        result.setZhValue(zhValue);
        return result;
    }

    /**
     * 根据类型和用量设置碳排和综合能耗
     *
     * @param yearEnergy 年用能
     */
    private void setDzValue(YearEnergy yearEnergy) {
        BigDecimal carbonFactor = BigDecimal.ZERO;
        BigDecimal calculateProportion = BigDecimal.ZERO;
        List<EnergyCollectConfigVO> energyCollectConfigVOList = energyCollectConfigMapper.getEnergyCollectConfigFuelList(Wrappers
                                                                                                                                 .<EnergyCollectConfig>lambdaQuery()
                                                                                                                                 .eq(EnergyCollectConfig::getCategoryCode, yearEnergy.getInstrumentType())
                                                                                                                        );
        if (energyCollectConfigVOList != null && !energyCollectConfigVOList.isEmpty()) {
            EnergyCollectConfigVO vo = energyCollectConfigVOList.get(0);
            if (vo.getCarbonFactor() != null) {
                carbonFactor = vo.getCarbonFactor();
            }
            if (vo.getCalculateProportion() != null) {
                calculateProportion = vo.getCalculateProportion();
            }
            yearEnergy.setFuelName(vo.getFuelName());
            yearEnergy.setUnit(vo.getUnit());
            yearEnergy.setCarbonFactor(vo.getCarbonFactor());
            yearEnergy.setCalculateProportion(vo.getCalculateProportion());
        }
        BigDecimal dcValue = BigDecimal.ZERO;
        BigDecimal zhValue = BigDecimal.ZERO;
        if (yearEnergy.getEnergyValue() != null) {
            dcValue = carbonFactor.multiply(yearEnergy.getEnergyValue()).setScale(2, RoundingMode.HALF_UP);
            zhValue = calculateProportion.multiply(yearEnergy.getEnergyValue()).setScale(2, RoundingMode.HALF_UP);
        }
        yearEnergy.setDcValue(dcValue);
        yearEnergy.setZhValue(zhValue);
    }
}
