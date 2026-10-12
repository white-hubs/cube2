package com.guhecloud.cube.srv.gen.energymanagement.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyChargeReport;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeReportQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ChargeTotalReportVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ChargeTypeReportVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用能费用报表mapper
 */
@Mapper
public interface EnergyChargeReportMapper extends BaseMapper<EnergyChargeReport> {
    /**
     * 获取每日的类型费用汇总分页列表
     * 每天每个类型的总费用
     * (只有水电气，要其他的类型需要自己加)
     */
    Page<ChargeTotalReportVO> getTypeDaySumCostPage(Page<ChargeTotalReportVO> page, @Param("param") EnergyChargeReportQueryParam param);

    /**
     * 获取每日的类型费用汇总列表
     * 每天每个类型的总费用
     * (只有水电气，要其他的类型需要自己加)
     */
    List<ChargeTotalReportVO> getTypeDaySumCostList(@Param("param") EnergyChargeReportQueryParam param);

    /**
     * 获取类型一段时间的总费用
     *
     * @param param 条件
     *
     * @return 总费用
     */
    List<ChargeTypeReportVO> getTypePeriodSumCost(@Param("param") EnergyChargeReportQueryParam param);

    /**
     * 获取趋势
     *
     * @param param 条件
     *              format 格式时间化格式
     *              period 时间区间
     *
     * @return 趋势
     */
    List<TrendResultVO> selectTrend(@Param("param") EnergyChargeReportQueryParam param);
}
