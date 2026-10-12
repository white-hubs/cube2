package com.guhecloud.cube.srv.gen.loose.works.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectReformProcessTimeVO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectReformPO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/13 9:34
 * @Description:
 */

@Mapper
public interface ProjectReformMapper extends BaseMapper<ProjectReformPO> {

    @InterceptorIgnore(tenantLine = "on")
    List<ProjectReformPO> selectAll(@Param("period") Period period, @Param("tenant") Integer tenant);

    ProjectReformProcessTimeVO selectAvgProcessTime(@Param("period") Period period);
}
