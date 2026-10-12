package com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.cube.property.management.client.property.param.property.OveragePageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckTaskDataSourceParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedExcelParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyDetailedPageParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyByDepAll;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyByDepAllPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedTotalVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyStatisticsVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyAllot;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyDetailed;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资产明细;(property_detailed)数据库访问层
 *
 * @author : lijie
 * @date : 2023-3-8
 */
@Mapper
public interface PropertyDetailedMapper extends BaseMapper<PropertyDetailed> {
    /**
     * 分页查询
     */
    Page<PropertyDetailedPageVO> getPropertyDetailedPage(@Param("page") Page page,
                                                         @Param("param") PropertyDetailedPageParam param,
                                                         @Param("statusList") List<String> statusList
                                                        );

    List<PropertyDetailedPageVO> getPropertyDetailedAll(@Param("param") PropertyDetailedExcelParam param,
                                                        @Param("statusList") List<String> statusList
                                                       );

    List<PropertyDetailedPageVO> getPropertyDetailedList(@Param("param") PropertyCheckTaskDataSourceParam param,
                                                         @Param("areaId") String areaId
                                                        );

    /**
     * 查询同批次入库的资产id
     */
    List<PropertyDetailed> getAllIdByDepository(@Param("depositoryNumbers") String depositoryNumbers);

    Integer updateAllByList(@Param("ids") List<Long> ids, @Param("status") String status);

    /**
     * 查询部门资产汇总
     */
    List<PropertyByDepAllPageVO> getPropertyByDepAllPage(@Param("param") OrganizationQueryParam param);

    PropertyByDepAll getPropertyByDepAllMoney(@Param("param") OrganizationQueryParam param);

    PropertyStatisticsVO getPropertyStatistics(@Param("param") PropertyDetailedPageParam param);

    /**
     * 根据入库单号修改资产状态
     */
    Integer updateByDepositoryNumbers(@Param("depositoryNumbers") String depositoryNumbers);

    /**
     * 修改出库、调拨资产
     */
    Integer updateByAllotNumbers(@Param("allot") PropertyAllot allot,
                                 @Param("ids") List<Long> ids,
                                 @Param("newStatus") String newStatus,
                                 @Param("status") String status
                                );

    /**
     * 修改出库、调拨资产
     */
    Integer updateByWeedOutNumbers(@Param("weedOutNumbers") String weedOutNumbers);

    Page<PropertyDetailedPageVO> getOveragePage(@Param("page") Page page, @Param("param") OveragePageParam param);

    /**
     * 查询维保到期、资产过期金额
     */
    PropertyDetailedTotalVO getOverageAmount(@Param("param") OveragePageParam param);

    List<Integer> getAllTenant();

    /**
     * 安全管理数据采集接口
     *
     * @param period    时间范围
     * @param typeNames 类型名称
     * @param status    状态
     *
     * @return 采集数据
     */
    @InterceptorIgnore(tenantLine = "on")
    List<PropertyDetailed> getSafetyManagementData(@Param("period") Period period, @Param("typeNames") List<String> typeNames,
                                                   @Param("status") List<String> status
                                                  );
}
