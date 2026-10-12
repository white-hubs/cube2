package com.guhecloud.cube.deliver.project.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.deliver.project.merchant.entity.CompanyUser;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyUserQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商铺-用户关联 Mapper 接口
 * </p>
 *
 * @author bq
 * @since 2021-01-13
 */
@Mapper
public interface CompanyUserMapper extends BaseMapper<CompanyUser> {

    List<CompanyUser> queryCompanyUsers(@Param("queryDTO") CompanyUserQueryDTO queryDTO);
}
