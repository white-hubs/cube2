package com.guhecloud.cube.srv.gen.safety.management.education.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.education.application.querygateway.query.EducationMarketTaskUrlQuery;
import com.guhecloud.cube.srv.gen.safety.management.education.interfaces.vo.EducationMarketTaskUrlVO;
import com.guhecloud.cube.srv.gen.safety.management.education.po.EducationMarketTaskUrlPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EducationMarketTaskUrlMapper extends BaseMapper<EducationMarketTaskUrlPO> {

    Page<EducationMarketTaskUrlVO> selectMarketTaskUrls(Page page, @Param("query") EducationMarketTaskUrlQuery query);
}
