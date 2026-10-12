package com.guhecloud.cube.srv.gen.safety.management.service;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountPropertyFilingNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.FilingDeviceQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.PropertyFilingNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.risksource.RiskSourceMetricQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.KeyValueVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AppSafetyWorkSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.StackedBarChartVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmEventTypeRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.FilingDeviceVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.FlammableDeviceFilingVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.MarketNumStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.PropertyByTypePageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.PropertyFilingAnalyseVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.PropertyFilingStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.RiskSourceCountStatusVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @author wql
 * @describe 资产统计
 * @date 2023/9/8 17:08
 */
public interface PropertyStatisticsService {
    Pagination<FilingDeviceVO> getDeviceExpireList(FilingDeviceQuery expiredDevice);

    List<PropertyFilingAnalyseVO> getFilingAnalyse(BaseQuery query);

    Pagination<MarketNumStatisticsVO> getDeviceExpireStatistics(BaseQuery query);

    FlammableDeviceFilingVO getFlammableDeviceStatistics(BaseQuery query);

    List<KeyValueVO> getPropertyTypeList();

    PropertyFilingStatisticsVO getPropertyFilingStatistics(BaseQuery query);

    Integer getDeviceFilingNum(CountPropertyFilingNumQuery propertyQuery);

    Integer getFireFightingEquipmentExpiredNum(BaseQuery query);

    Pagination<MarketNumStatisticsVO> getFlammableDeviceList(BaseQuery query);

    Pagination<MarketNumStatisticsVO> getElectricVehicleDeviceList(BaseQuery query);

    Pagination<MarketNumStatisticsVO> getPropertyFilingList(BaseQuery query);

    Integer countPropertyFilingNum(PropertyFilingNumQuery propertyFilingNumQuery);

    Pagination<PropertyByTypePageVO> getPropertyTypePage(BaseQuery query);

    AppSafetyWorkSituationVO.WorkSituationVO getPropertyFilingRatioByMarket();

    /**
     * 根据类型统计风险源状态数
     *
     * @param query
     *
     * @return
     */
    List<RiskSourceCountStatusVO> countStatusByType(RiskSourceMetricQuery query);

    /**
     * 根据 marketTenantID 统计市场建档数
     *
     * @return
     */
    List<GroupCountVO> countByMarket(BaseStatisticsQuery query);

    /**
     * 根据 type 统计市场建档数
     *
     * @return
     */
    List<GroupCountVO> countByType(RiskSourceMetricQuery query);

    Pagination<StackedBarChartVO> getVehicleCountBar(RiskSourceMetricQuery param);

    List<AlarmEventTypeRatioVO> getVehicleCountPie(RiskSourceMetricQuery param);

    Long getElectrlcVehicleDeviceCount();
}
