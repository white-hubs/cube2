package com.guhecloud.cube.srv.gen.energymanagement.server.controller.job;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyCollectConfigMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailyUsage;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.YearEnergy;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageStatParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyDailyUsageService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.YearEnergyService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyCollectConfigVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyTypeStatVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 年用能job
 */
@Slf4j
@Component
public class YearEnergyJob {

    @Autowired
    private EnergyDailyUsageService energyDailyUsageService;
    @Autowired
    private YearEnergyService yearEnergyService;
    @Autowired
    private EnergyCollectConfigMapper energyCollectConfigMapper;

    /**
     * 年用能统计
     *
     * @param param 年份（2024）
     *
     * @return
     */
    @JobAnnotation(name = "yearEnergy", cron = "0 0 0 1 1 ?", desc = "能耗-能耗年用能自动统计")
    public void execute(String param) {
        LocalDateTime startTime = null;
        if (StringUtils.isNotBlank(param)) {
            try {
                startTime = DateUtils.parseDate(param + "-01-01");
            } catch (Exception e) {
                throw new ServiceException("年份不正确！");
            }
        } else {
            // 年初统计去年的
            startTime = DateUtils.parseDate((LocalDateTime.now().getYear() - 1) + "-01-01");
        }
        SessionContextHolder.clearSessionContext();
        var energyDailyUsagesList = energyDailyUsageService.list(Wrappers.<EnergyDailyUsage>lambdaQuery()
                                                                         .groupBy(EnergyDailyUsage::getTenant)
                                                                );
        if (energyDailyUsagesList == null || energyDailyUsagesList.isEmpty()) {
            log.warn("能碳年用能自动统计无租户！");
            return;
        }
        // 这里是在遍历租户
        for (EnergyDailyUsage energyDailyUsage : energyDailyUsagesList) {
            var tenant = energyDailyUsage.getTenant();
            var account = "system";
            SessionContextHolder.requestTenant(tenant);
            SecurityContextHolder.setPrincipal(Principal.builder().id(1L).name(account).account(account).admin(true).type(Principal.Type.CLIENT).build());
            // 年统计
            this.statYearEnergy(startTime, LocalDateTime.now(), tenant, account, ActorTypeEnum.SYSTEM);
        }
    }

    /**
     * 年用能统计
     *
     * @param startTime 开始时间 年份的1月1日
     * @param newTime   当前时间
     * @param tenant    租户
     * @param account   登录人
     */
    private void statYearEnergy(LocalDateTime startTime, LocalDateTime newTime, Integer tenant, String account, ActorTypeEnum userType) {
        // 看是否已有年用能
        YearEnergy oldYear = yearEnergyService.getOne(Wrappers.<YearEnergy>lambdaQuery()
                                                              .eq(YearEnergy::getYear, String.valueOf(startTime.getYear()))
                                                              .last(" limit 1 ")
                                                     );
        if (oldYear != null) {
            log.warn("能碳年用能统计已有数据！");
            return;
        }

        // 获取分类的年用能统计数据
        EnergyDailyUsageStatParam queryParam = new EnergyDailyUsageStatParam();
        queryParam.setPeriod(Period.of(startTime, DateUtils.endOfYear(startTime)));
        List<EnergyTypeStatVO> yearStatList = energyDailyUsageService.getDailyUsageStatOn(queryParam);
        if (yearStatList == null || yearStatList.isEmpty()) {
            log.warn("能碳年用能自动统计结果为空！");
            return;
        }

        // 获取分类的燃料数据
        Map<String, EnergyCollectConfigVO> fuelMap = new HashMap<>();
        List<EnergyCollectConfigVO> configFuelList = energyCollectConfigMapper.getEnergyCollectConfigFuelList(Wrappers.emptyWrapper());
        if (configFuelList != null && !configFuelList.isEmpty()) {
            for (EnergyCollectConfigVO configFuel : configFuelList) {
                fuelMap.put(configFuel.getCategoryCode(), configFuel);
            }
        }

        List<YearEnergy> insertList = new ArrayList<>();
        for (EnergyTypeStatVO energyTypeStatVO : yearStatList) {
            EnergyCollectConfigVO configFuel = fuelMap.get(energyTypeStatVO.getInstrumentType());
            // 组装年用能实体
            YearEnergy yearEnergy = new YearEnergy();
            yearEnergy.setId(IdWorker.getId());
            yearEnergy.setDateYear(startTime);
            yearEnergy.setYear(String.valueOf(startTime.getYear()));
            yearEnergy.setZhValue(energyTypeStatVO.getZhValue());
            yearEnergy.setDcValue(energyTypeStatVO.getDcValue());
            yearEnergy.setEnergyValue(energyTypeStatVO.getDdValue());
            yearEnergy.setSourceType("auto");
            yearEnergy.setInstrumentType(energyTypeStatVO.getInstrumentType());
            yearEnergy.setInstrumentName(energyTypeStatVO.getInstrumentTypeName());

            yearEnergy.setDeleted(false);
            yearEnergy.setTenant(tenant);
            EntityAuditor.populateAudit(yearEnergy, account, StringUtils.EMPTY_STRING, userType, newTime);

            if (configFuel != null) {
                yearEnergy.setCarbonFactor(configFuel.getCarbonFactor());
                yearEnergy.setCalculateProportion(configFuel.getCalculateProportion());
                yearEnergy.setFuelName(configFuel.getFuelName());
                yearEnergy.setUnit(configFuel.getUnit());
            }
            insertList.add(yearEnergy);
        }
        if (insertList.isEmpty()) {
            return;
        }
        yearEnergyService.saveBatch(insertList, 1000);
    }

}
