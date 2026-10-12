package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.energymanagement.server.dto.KeyValueDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.DosageAnalysisParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.CarbonQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyAnalysisQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyProjectQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyService;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.TimeStatUtil;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.AreaStatisticsVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataTimeFlagVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DataVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DosageAnalysisDistributionVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DosageAnalysisVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyAnalysisMenuVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyConsumptionDetailsElseVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyConsumptionDetailsVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendResultVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.guhecloud.platform.base.constants.DateFormat.COMPACT_DATETIME_FORMAT;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyController {

    private final EnergyService energyService;

    /**
     * @Description 能碳总览-数据统计-30天
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    @GetMapping("/comprehensive-energy")
    public Map<String, Object> getComprehensiveEnergy() {
        return energyService.getComprehensiveEnergy();
    }

    /**
     * @Description 碳排放趋势-近30
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    @GetMapping("/energy-trend")
    public Map<String, Object> getEnergyTrend() {

        return energyService.getEnergyTrend();
    }

    /**
     * @Description 用能趋势-30日
     * @Author lrj
     */
    @GetMapping("/energy-consumption-trend")
    public Map<String, List<DataTimeFlagVo>> energyConsumptionTrend() {
        return energyService.energyConsumptionTrend();
    }

    /**
     * @Description 用电排行-30日
     * @Author lrj
     */
    @GetMapping("/electricity-consumption-ranking")
    public List<AreaStatisticsVo> electricityConsumptionRanking() {
        return energyService.electricityConsumptionRanking();
    }

    /**
     * @Description 能源走向-近30日
     * @Author lrj
     */
    @GetMapping("/energy-direction")
    public Map<String, Object> energyDirection() {
        return energyService.energyDirection();
    }

    /**
     * @Description 碳排统计
     * @Author lrj
     */
    @GetMapping("/carbon-emission-statistics")
    public Map<String, Object> carbonEmissionStatistics() {

        return energyService.carbonEmissionStatistics(true);
    }

    /**
     * @Description 碳排趋势
     * @Author lrj
     */
    @GetMapping("/carbon-emission-trends")
    public List<DataTimeFlagVo> carbonEmissionTrends(CarbonQueryParam carbonQueryParam) {
        carbonQueryParam.setIsCarbon(true);
        return energyService.carbonEmissionTrends(carbonQueryParam);
    }

    /**
     * @Description 碳排分项占比
     * @Author lrj
     */
    @GetMapping("/proportion-of-carbon-emissions-items")
    public List<DataTimeFlagVo> proportionCarbonEmissionsItems(CarbonQueryParam carbonQueryParam) {
        return energyService.proportionCarbonEmissionsItems(carbonQueryParam);
    }

    /**
     * @Description 综合能耗
     * @Author lrj
     */
    @GetMapping("/comprehensive-energy-consumption")
    public Map<String, Object> comprehensiveEnergyConsumption() {

        return energyService.carbonEmissionStatistics(false);
    }

    /**
     * @Description 综合能耗趋势
     * @Author lrj
     */
    @GetMapping("/comprehensive-energy-consumption-trend")
    public List<DataTimeFlagVo> comprehensiveEnergyConsumptionTrend(CarbonQueryParam param) {
        param.setIsCarbon(false);
        return energyService.carbonEmissionTrends(param);
    }

    /**
     * @Description分项统计(饼图及柱状图)
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    @GetMapping("/project")
    public Map<String, Object> getEnergyProject(EnergyProjectQueryParam param) {
        return energyService.getEnergyProject(param);
    }

    /**
     * @Description 区域分项（饼图及柱状图）
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    @PostMapping("/energy-area")
    public Map<String, Object> getEnergyArea(@RequestBody EnergyTrendParam param) {
        return energyService.getEnergyArea(param);
    }

    /**
     * @Description 用能情况
     * @Author lrj
     */
    @PostMapping("/dosage-analysis")
    public Map<String, Object> dosageAnalysis(@RequestBody DosageAnalysisParam param) {
        return energyService.dosageAnalysis(param);
    }

    /**
     * @Description 用能分析
     * @Author lrj
     */
    @PostMapping("/energy-analysis")
    public List<DosageAnalysisVo> energyAnalysis(@RequestBody DosageAnalysisParam param) {
        return energyService.energyAnalysis(param);
    }

    /**
     * @Description 能耗分布
     * @Author lrj
     */
    @PostMapping("/energy-analysis-distribution")
    public List<DosageAnalysisDistributionVo> energyAnalysisDistribution(@RequestBody DosageAnalysisParam param) {
        return energyService.energyAnalysisDistribution(param);
    }

    /**
     * @Description 能耗排名
     * @Author lrj
     */
    @PostMapping("/energy-consumption-ranking")
    public List<DosageAnalysisVo> energyConsumptionRanking(@RequestBody DosageAnalysisParam param) {
        return energyService.energyConsumptionRanking(param);
    }

    /**
     * @Description 用能明细
     * @Author lrj
     */
    @PostMapping("/energy-consumption-details")
    public List<EnergyConsumptionDetailsVo> getEnergyConsumptionDetails(@RequestBody DosageAnalysisParam param) {
        return energyService.getEnergyConsumptionDetails(param);
    }

    /**
     * @Description 用能明细导表
     * @Author lrj
     */
    @PostMapping("/energy-consumption-details/excel")
    public void getEnergyConsumptionDetails(HttpServletResponse response,
                                            @RequestBody DosageAnalysisParam param
                                           ) throws IOException {
        List<EnergyConsumptionDetailsVo> energyConsumptionDetails = energyService.getEnergyConsumptionDetails(param);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("name", "名称");
        writer.addHeaderAlias("value", "总量");
        if (param.getInstrumentType().equals(InstrumentTypeEnum.ELECTRICITY_METER.getCode())) {
            writer.addHeaderAlias("fvalue", "峰电量");
            writer.addHeaderAlias("pvalue", "平电量");
            writer.addHeaderAlias("gvalue", "谷电量");
            writer.write(energyConsumptionDetails, true);
        } else {
            List<EnergyConsumptionDetailsElseVo> copying = ObjectUtils.copying(energyConsumptionDetails, EnergyConsumptionDetailsElseVo.class);
            writer.write(copying, true);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        LocalDateTime date = LocalDateTime.now();
        String format1 = DateUtils.format(date, COMPACT_DATETIME_FORMAT);
        String name = "能耗仪表" + format1;
        String fileName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            writer.close();
            if (Objects.nonNull(out)) {
                out.close();
            }
        }
    }

    /**
     * @Description pc能耗分析分项菜单列表
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    @GetMapping("/energy-analysis-menu")
    public List<EnergyAnalysisMenuVo> getEnergyAnalysisMenu() {

        return energyService.getEnergyAnalysisMenu();
    }

    /**
     * @Description app能耗分析分项菜单列表
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    @GetMapping("/energy-analysis-app-menu")
    public List<EnergyAnalysisMenuVo> getAppEnergyAnalysisMenu() {

        return energyService.getAppEnergyAnalysisMenu();
    }

    /**
     * @Description 分析总览统计
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    @GetMapping("/energy-analysis-overview")
    public Map<String, Object> getEnergyAnalysisOverview(EnergyAnalysisQueryParam param) throws ParseException {
        return energyService.getEnergyAnalysisOverview(param);
    }

    /**
     * @Description 能耗分析趋势
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    @GetMapping("/energy-analysis-trend")
    public List<DataTimeFlagVo> getEnergyAnalysisTrend(EnergyTrendParam param) throws ParseException {
        return energyService.getEnergyAnalysisTrend(param);
    }

    /**
     * @Description 能耗分析采集里历史
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    @GetMapping("/energy-analysis-history")
    public Page<DataVo> EnergyAnalysisHistory(EnergyAnalysisQueryParam param) throws ParseException {
        return energyService.EnergyAnalysisHistory(param);
    }

    /**
     * @Description app能耗分析采集里历史
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    @GetMapping("/app-energy-analysis-history")
    public Page<DataVo> appEnergyAnalysisHistory(EnergyAnalysisQueryParam param) throws ParseException {
        Page<DataVo> analysisHistoryVoPage = energyService.EnergyAnalysisAppHistory(param);
        return analysisHistoryVoPage;
    }

    /**
     * 设备用能趋势
     *
     * @param param diviceId 设备Id
     *
     * @return 趋势
     */
    @GetMapping("/device-use-trend")
    public List<TrendResultVO> getDeviceTrend(EnergyTrendUsageParam param) {
        TimeStatUtil.initStatParam(param);
        return energyService.getDeviceTrend(param);
    }

    /**
     * 设备一段时间的用能排行
     *
     * @param param 条件
     *
     * @return
     */
    @GetMapping("/device-use-ranking")
    public Pagination<KeyValueDTO> getDevicePeriodUseRanking(EnergyTrendUsageParam param) {
        TimeStatUtil.initStatParam(param);
        return energyService.getDevicePeriodUseRanking(param);
    }

}
