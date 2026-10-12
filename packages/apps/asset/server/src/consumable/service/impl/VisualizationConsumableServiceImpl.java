package com.guhecloud.cube.srv.gen.property.management.server.consumable.service.impl;

import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.property.management.client.common.constant.TimeDimensionConstant;
import com.guhecloud.cube.property.management.client.common.param.StatisticalChartParam;
import com.guhecloud.cube.property.management.client.common.util.DateLocalUtil;
import com.guhecloud.cube.property.management.client.common.util.VisualizationData;
import com.guhecloud.cube.property.management.client.common.util.VisualizationGraphicsUtil;
import com.guhecloud.cube.property.management.client.common.vo.ConsumableStockVO;
import com.guhecloud.cube.property.management.client.common.vo.DateTimeSlotVO;
import com.guhecloud.cube.property.management.client.common.vo.PieChartValueVO;
import com.guhecloud.cube.property.management.client.common.vo.StatisticalChartVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.VisualizationConsumableMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.VisualizationConsumableService;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisualizationConsumableServiceImpl implements VisualizationConsumableService {
    private final VisualizationConsumableMapper visualizationConsumableMapper;
    private final OrganizationClient organizationClient;

    @Override
    public List<PieChartValueVO> reduceRecord(StatisticalChartParam param) {
        // 如果是查询今天的数据，则返回的x轴为今天的时间，因为资产入库时间为年月日没有时
        // 产品设计缺陷，特殊处理，不推荐这样处理
        if (ObjectUtils.isEmpty(param.getDimension()) || TimeDimensionConstant.TODAY.equals(param.getDimension())) {
            DateTimeSlotVO date = DateLocalUtil.getToday();
            String valus = visualizationConsumableMapper.getReduceRecordToday(date);
            List<PieChartValueVO> data = new ArrayList<>();
            data.add(
                    new PieChartValueVO(DateUtils.formatDate(DateUtils.now()), new BigDecimal(Optional.ofNullable(valus).orElse("0"))));
            return data;
        }
        StatisticalChartVO chart = VisualizationGraphicsUtil.getGraphics(param.getDimensiondefault(), "耗材出库趋势");
        //  查询数据
        List<PieChartValueVO> data = visualizationConsumableMapper.getReduceRecord(param.getPeriodTiem(),
                                                                                   param.getDimensiondefault());
        return VisualizationData.getPieChartData(param.getDimension(), chart.getXdata(), data);
    }

    @Override
    public List<PieChartValueVO> consumableByDep(StatisticalChartParam param) {
        List<PieChartValueVO> data = visualizationConsumableMapper.consumableByDep(param.getPeriodTiem());
        List<PieChartValueVO> list = new ArrayList<>();
        for (PieChartValueVO ex : data) {
            if (ObjectUtils.isNotEmpty(ex)) {
                Long orgId = Long.parseLong(ex.getName());
                if (ObjectUtils.isNotEmpty(orgId)) {
                    OrganizationDTO org = organizationClient.getOrganizationsById(orgId);
                    if (ObjectUtils.isNotEmpty(org)) {
                        list.add(new PieChartValueVO(org.getName(), ex.getValue()));
                    }
                }
            }
        }
        return list;
    }

    @Override
    public ConsumableStockVO stockAvailability() {
        List<ConsumableStockVO> consumableStockList = visualizationConsumableMapper.stockAvailability();
        ConsumableStockVO consumableStockVO = new ConsumableStockVO();
        if (ObjectUtils.isNotEmpty(consumableStockList)) {
            consumableStockVO.setconsumableNumber(consumableStockList);
        }
        return consumableStockVO;
    }

}
