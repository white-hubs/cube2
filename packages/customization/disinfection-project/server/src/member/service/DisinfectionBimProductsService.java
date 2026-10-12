package com.guhecloud.cube.srv.gen.disinfection.server.member.service;

import com.guhecloud.cube.srv.gen.disinfection.server.member.param.CountsTrendParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.BoundDevicePageQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.BimPointVO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.DistrictDeviceCountVO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.BimStatisticsOverviewVO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.BimBoundDevicePageResult;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ManualCommandUsageCountVO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ProductTypeCountVO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ProductUserCountVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface DisinfectionBimProductsService {
    Long productsCounts();

    List<ProductTypeCountVO> productsTypeCounts();

    List<ProductUserCountVO> productsUsersCounts();

    List<BimPointVO> countsTrend(CountsTrendParam countsTrendParam);

    List<BimPointVO> productsMonthUserCounts(Period period);

    List<ProductTypeCountVO> productsNewCounts(Period period);

    BimStatisticsOverviewVO statisticsOverview();

    /**
     * 近半年每月新增设备数量（按设备类型过滤可选）
     *
     * @param productTypeCode 设备类型编码（产品分类编码），为空则统计全部
     */
    List<BimPointVO> halfYearNewDeviceMonthCounts(String productTypeCode);

    /**
     * 获取绑定设备分页(设备名称、状态)
     */
    Pagination<BimBoundDevicePageResult> boundDevicesPage(BoundDevicePageQueryParam param);

    /**
     * 统计各区县绑定设备数量（基于数据库区县字段）
     */
    List<DistrictDeviceCountVO> districtBoundDeviceCounts();

    /**
     * 雾灌指令和驱蚊指令使用次数统计
     */
    ManualCommandUsageCountVO manualCommandUsageCounts();
}
