package com.guhecloud.cube.srv.gen.property.management.server.property.dao.dispose;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyWeedOutPageParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyWeedOutPageVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyWeedOut;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 清理报废;数据库访问层
 *
 * @author : lijie
 * @date : 2023-3-17
 */
@Mapper
public interface PropertyWeedOutMapper extends BaseMapper<PropertyWeedOut> {

    Page<PropertyWeedOutPageVO> getPropertyWeedOutPage(@Param("page") Page page, @Param("param") PropertyWeedOutPageParam param);

    List<PropertyDetailedVO> propertyDetailed(@Param("weedOutNumbers") String weedOutNumbers, @Param("keyword") String keyword);

}
