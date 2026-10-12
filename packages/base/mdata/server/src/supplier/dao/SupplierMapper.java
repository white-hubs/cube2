package com.guhecloud.cube.biz.mdata.server.supplier.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.Supplier;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierQueryParam;
import com.guhecloud.cube.biz.mdata.server.supplier.vo.SupplierPageVO;
import com.guhecloud.cube.biz.mdata.server.supplier.vo.SupplierVO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lzy
 * @date 2021/12/3
 */
@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {

    Page<Supplier> pageSupplier(Page<?> page, @Param("param") SupplierQueryParam queryParam);

    Page<UserDTO> getCustomers(Page<?> page, @Param("param") SupplierQueryParam queryParam);

    Page<SupplierVO> getCustomersV2(Page page, @Param("param") SupplierQueryParam queryParam);

    List<SupplierVO> getCustomersV2(@Param("param") SupplierQueryParam queryParam);

    @Select("select s.`id`,sa.`id` as supplierAuthenticationId,s.`name`,s.liaison,s.`phone`,sa.`status`,sa.`type`,s.`address`,sa.`update_time` from supplier s " +
            "left join `supplier_authentication` sa on s.`id` = sa.`supplier_id` and sa.`status` != 'INVALID' ${ew.customSqlSegment}"
    )
    Page<SupplierPageVO> pageSupplierDetail(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

}
