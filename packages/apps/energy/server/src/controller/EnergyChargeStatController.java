package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.ChargeComprehensiveParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.TreeIds;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyChargeStatService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ChargeStatisticsVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeStatVo;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.guhecloud.platform.base.constants.DateFormat.COMPACT_DATETIME_FORMAT;

/**
 * @author appet
 * @Description 能耗费用统计
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyChargeStatController {
    private final EnergyChargeStatService energyChargeStatService;

    private List<Long> getIds(List<Long> ids, List<TreeIds> treeIds, List<Long> areaOrRouteIds) {
        for (TreeIds treeId : treeIds) {
            if (ids.contains(treeId.getId())) {
                areaOrRouteIds.add(treeId.getId());
            } else if (ListUtils.nonEmpty(treeId.getChildren())) {
                getIds(ids, treeId.getChildren(), areaOrRouteIds);
            }
        }
        return areaOrRouteIds;
    }

    /**
     * @Description 综合费用
     * @Author lrj
     */
    @GetMapping("/comprehensive-expenses")
    public Map<String, Object> getComprehensiveExpenses(ChargeComprehensiveParam param) {

        return energyChargeStatService.getComprehensiveExpenses(param);
    }

    /**
     * @Description 总费用趋势
     * @Author lrj
     */
    @GetMapping("/comprehensive-expenses-trend")
    public Map<String, List<ChargeStatisticsVo>> getComprehensiveExpensesTrend(ChargeComprehensiveParam param) throws Exception {

        return energyChargeStatService.getComprehensiveExpensesTrend(param);
    }

    /**
     * @Description 用电费用统计
     * @Author lrj
     */
    @PostMapping("/area-comprehensive-expenses")
    public EnergyChargeStatVo getAreaComprehensiveExpenses(@RequestBody ChargeComprehensiveParam param) {
        return energyChargeStatService.getAreaComprehensiveExpenses(param);
    }

    /**
     * @Description 费用趋势
     * @Author lrj
     */
    @PostMapping("/area-comprehensive-trend")
    public List<ChargeStatisticsVo> getAreaOrRouteComprehensiveTrend(@RequestBody ChargeComprehensiveParam param) {
        return energyChargeStatService.getAreaOrRouteComprehensiveTrend(param);
    }

    /**
     * @Description 费用明细
     * @Author lrj
     */
    @PostMapping("/area-comprehensive-detail")
    public List<ChargeStatisticsVo> getAreaOrRouteComprehensiveDetail(@RequestBody ChargeComprehensiveParam param) {
        return energyChargeStatService.getAreaOrRouteComprehensiveDetail(param);
    }

    /**
     * @Description 用能明细导表
     * @Author lrj
     */
    @PostMapping("/area-comprehensive-detail/excel")
    public void getEnergyConsumptionDetails(HttpServletResponse response,
                                            @RequestBody ChargeComprehensiveParam param
                                           ) throws UnsupportedEncodingException {
        List<ChargeStatisticsVo> areaOrRouteComprehensiveDetail = energyChargeStatService.getAreaOrRouteComprehensiveDetail(param);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("name", "名称");
        writer.addHeaderAlias("amount", "总费用");
        if (param.getInstrumentType().equals(InstrumentTypeEnum.ELECTRICITY_METER.getCode())) {
            writer.addHeaderAlias("fvAmount", "峰电费");
            writer.addHeaderAlias("pvAmount", "平电费");
            writer.addHeaderAlias("gvAmount", "谷电费");
        }
        writer.write(areaOrRouteComprehensiveDetail, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        LocalDateTime date = LocalDateTime.now();
        String format1 = DateUtils.format(date, COMPACT_DATETIME_FORMAT);
        String name = "费用明细" + format1;
        String fileName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();

        }
    }

}