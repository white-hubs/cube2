package com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.property.management.client.property.param.property.AllotExcelParam;
import com.guhecloud.cube.property.management.client.property.param.property.AllotPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.AllotUnitExcelParam;
import com.guhecloud.cube.property.management.client.property.vo.property.AllotPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyAllot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资产派发记录;(outbound)数据库访问层
 *
 * @author : lijie
 * @date : 2023-3-9
 */
@Mapper
public interface PropertyAllotMapper extends BaseMapper<PropertyAllot> {
    AllotPageVO queryById(@Param("id") Long id);

    Page<AllotPageVO> getAllotPage(@Param("page") Page page, @Param("param") AllotPageParam param);

    AllotPageVO queryByPropertyId(@Param("propertyId") Long propertyId);

    List<PropertyDetailedVO> propertyDetailed(@Param("param") AllotPageParam param);

    List<PropertyDetailedVO> getAllotDetails(@Param("id") Long id, @Param("param") AllotPageParam param);

    List<AllotExcelParam> getExcel(@Param("id") String id);

    List<AllotUnitExcelParam> getAllotUnitExcel(@Param("id") String id);

    List<Long> getPropertyDetailedIds(@Param("allotNumbers") String allotNumbers);

}
