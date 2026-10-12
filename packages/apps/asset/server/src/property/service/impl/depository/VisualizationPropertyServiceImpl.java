package com.guhecloud.cube.srv.gen.property.management.server.property.service.impl.depository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.property.management.client.common.param.StatisticalChartParam;
import com.guhecloud.cube.property.management.client.common.util.VisualizationData;
import com.guhecloud.cube.property.management.client.common.util.VisualizationGraphicsUtil;
import com.guhecloud.cube.property.management.client.common.vo.PieChartValueVO;
import com.guhecloud.cube.property.management.client.common.vo.PropertyTypePieChartVO;
import com.guhecloud.cube.property.management.client.common.vo.StatisticalChartDataVO;
import com.guhecloud.cube.property.management.client.common.vo.StatisticalChartVO;
import com.guhecloud.cube.property.management.client.property.vo.property.OverdueVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableStoreStockService;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.assembly.AssemblyClassifyMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyDetailedMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose.PropertyVisualizationMapper;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.assembly.AssemblyClassify;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyDetailed;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.AssemblyClassifyTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertySourceEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.PropertyStatusEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.VisualizationPropertyService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 资产总览
 *
 * @author : lijie
 * @date : 2023-4-16
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisualizationPropertyServiceImpl implements VisualizationPropertyService {
    private final PropertyVisualizationMapper visualizationMapper;
    private final ConsumableStoreStockService consumableStoreStockService;
    private final AssemblyClassifyMapper assemblyClassifyMapper;
    private final AreaClient areaClient;
    private final PropertyDetailedMapper propertyDetailedMapper;

    @Override
    public OverdueVO overdue(StatisticalChartParam param) {
        OverdueVO overdue = new OverdueVO();
        LocalDateTime date = ObjectUtils.isEmpty(param.getPeriod()) ? DateUtils.now() : null;
        // 资产总数
        Integer total = visualizationMapper.getPropertyNumber(
                ObjectUtils.isNotEmpty(param.getDimension()) ? "total" : null, param.getPeriodTiem(), null);
        overdue.setPropertyNumber(Optional.ofNullable(total).orElse(0));
        // 报废资产数
        total = visualizationMapper.getWeedOutNumber(param.getPeriodTiem());
        overdue.setWeedOutNumber(Optional.ofNullable(total).orElse(0));
        // 使用年限到期数
        total = visualizationMapper.getPropertyNumber("usage", param.getPeriodTiem(), date);
        overdue.setUsageNumber(Optional.ofNullable(total).orElse(0));
        // 维保到期资产数
        total = visualizationMapper.getPropertyNumber("maintenance", param.getPeriodTiem(), date);
        overdue.setMaintenanceNumber(Optional.ofNullable(total).orElse(0));
        // 报修到期资产数
        total = visualizationMapper.getPropertyNumber("repairReport", param.getPeriodTiem(), date);
        overdue.setRepairReportNumber(Optional.ofNullable(total).orElse(0));
        // 耗材总数
        total = consumableStoreStockService.getConsumableNumber();
        overdue.setConsumableNumber(Optional.ofNullable(total).orElse(0));
        return overdue;
    }

    @Override
    public List<PieChartValueVO> getPropertyByStatus(StatisticalChartParam param) {
        List<String> tableName = new ArrayList<>();
        List<String> newTableName = new ArrayList<>();
        for (PropertyStatusEnum ex : PropertyStatusEnum.values()) {
            if (!PropertyStatusEnum.UNDER.name().equals(ex.name()) && !PropertyStatusEnum.DELETE.name().equals(ex.name())) {
                tableName.add(ex.name());
                newTableName.add(ex.name());
            }
        }
        List<PieChartValueVO> data = visualizationMapper.getPropertyProportionByStatus();
        return VisualizationData.getPieChartData(tableName, newTableName, data);
    }

    @Override
    public List<PieChartValueVO> getPropertyByType(StatisticalChartParam param) {
        List<PieChartValueVO> data = visualizationMapper.getPropertyType(param.getPeriodTiem(), null);
        List<PieChartValueVO> newData = new ArrayList<>();
        if (ObjectUtils.isEmpty(data) || data.isEmpty()) {
            List<AssemblyClassify> classifyList = assemblyClassifyMapper.selectList(new LambdaQueryWrapper<AssemblyClassify>()
                                                                                            .eq(AssemblyClassify::getType, AssemblyClassifyTypeEnum.ASSET_TYPE.getCode()));
            if (ObjectUtils.isNotEmpty(classifyList) && !classifyList.isEmpty()) {
                int max = classifyList.size() == 6 ? 6 : 5;
                for (int i = 0; i < max; i++) {
                    if (i < classifyList.size()) {
                        PieChartValueVO vo = new PieChartValueVO(classifyList.get(i).getName(), new BigDecimal(0));
                        data.add(vo);
                    }
                }
                if (classifyList.size() > 6) {
                    PieChartValueVO vo = new PieChartValueVO("其他", BigDecimal.ZERO);
                    data.add(vo);
                }
            }
            return data;
        }
        BigDecimal num = BigDecimal.ZERO;
        boolean bool = false;
        for (int i = 0; i < data.size(); i++) {
            if (i <= 5) {
                newData.add(data.get(i));
            } else {
                bool = true;
                num = num.add(data.get(i).getValue());
            }
        }
        if (bool) {
            PieChartValueVO pieChartValue = new PieChartValueVO("其他", num);
            newData.add(pieChartValue);
        }
        return newData;
    }

    @Override
    public PropertyTypePieChartVO getPropertyByOptType(StatisticalChartParam param) {
        Assert.isParameterError(CollectionUtils.isEmpty(param.getTypeNames()), "资产分类不能为空");
        Long total = propertyDetailedMapper.selectCount(new LambdaQueryWrapper<PropertyDetailed>()
                                                                .eq(PropertyDetailed::getAuthority, PropertySourceEnum.DEPOSITORY.name())
                                                                .in(PropertyDetailed::getStatus, PropertyStatusEnum.getStatusByBusinessType(null, null)));
        if (ObjectUtils.isEmpty(total) || total < 1) {
            return PropertyTypePieChartVO.builder().build();
        }
        List<PieChartValueVO> data = visualizationMapper.getPropertyType(param.getPeriodTiem(), param.getTypeNames());
        List<PieChartValueVO> pieData = new ArrayList<>();
        BigDecimal typeTotal = BigDecimal.ZERO;
        for (String ex : param.getTypeNames()) {
            boolean bool = true;
            for (PieChartValueVO pie : data) {
                if (ex.equals(pie.getName())) {
                    typeTotal = typeTotal.add(pie.getValue());
                    bool = false;
                    pie.setTotalRatio(new BigDecimal(total).divide(pie.getValue(), 2, RoundingMode.HALF_UP));
                    pieData.add(pie);
                }
            }
            if (bool) {
                PieChartValueVO vo = new PieChartValueVO(ex, new BigDecimal(0));
                pieData.add(vo);
            }
        }
        return PropertyTypePieChartVO.builder()
                .total(total)
                .typeTotal(typeTotal.intValueExact())
                .details(pieData)
                .build();
    }

    @Override
    public StatisticalChartVO getPropertyByDepHistogram(StatisticalChartParam param) {
        List<StatisticalChartDataVO> values = visualizationMapper.getPropertyByDepHistogramValue(param.getPeriodTiem());
        return VisualizationGraphicsUtil.getGraphics(values);
    }

    @Override
    public List<PieChartValueVO> getPropertyByDepPie(StatisticalChartParam param) {
        return visualizationMapper.getPropertyByDepPie(param.getPeriodTiem());
    }

    @Override
    public List<PieChartValueVO> getPropertyBySourcePie(StatisticalChartParam param) {
        List<PieChartValueVO> values = visualizationMapper.getPropertyBySourcePie(param.getPeriodTiem());
        List<PieChartValueVO> data = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (PieChartValueVO ex : values) {
            if (StringUtils.isBlank(ex.getName())) {
                continue;
            }
            List<Long> ids = StringUtils.splitAndTranslate(ex.getName(), Long::valueOf);
            List<AreaDTO> areaList = areaClient.getByIds(ids);
            if (ObjectUtils.isNotEmpty(areaList)) {
                List<String> areaName = areaList.stream().map(AreaDTO::getName).collect(Collectors.toList());
                data.add(new PieChartValueVO(String.join("/", areaName), ex.getValue()));
                total = total.add(ex.getValue());
            }
        }
        if (total.compareTo(BigDecimal.ZERO) > 0) {
            for (PieChartValueVO ex : data) {
                ex.setTotalRatio(MathUtils.calculateProportionPercentage(ex.getValue(), total));
            }
        }
        return data;
    }

}
