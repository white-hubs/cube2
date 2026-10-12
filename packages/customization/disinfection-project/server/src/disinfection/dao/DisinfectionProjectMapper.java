package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionProject;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.DisinfectionUserPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.BimBaseQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.KeyPlacesQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.DisinfectionUserInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.ProjectStatisticsBaseVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DisinfectionProjectMapper extends BaseMapper<DisinfectionProject> {

    /**
     * 根据指定参数分组查询项目数量
     *
     * @param groupType
     * @param param
     *
     * @return
     */
    List<ProjectStatisticsBaseVO> selectProjectNumbGroupByParam(@Param("groupType") String groupType, @Param("param")
                                                                BimBaseQueryParam param
                                                               );

    /**
     * 重点场所消杀项目统计
     *
     * @return
     */
    List<ProjectStatisticsBaseVO> selectProjectNumberGroupByType(@Param("param") KeyPlacesQueryParam param);

    List<DisinfectionUserInfoVO> selectDisinfectionUserStatistics(@Param("param") DisinfectionUserPageParam param);

    Page<DisinfectionUserInfoVO> selectDisinfectionUserStatistics(@Param("page") Page<?> page,
                                                                  @Param("param") DisinfectionUserPageParam param
                                                                 );

    List<Long> selectDisinfectionUserIdList();

    List<ProjectStatisticsBaseVO> selectDisinfectionTaskNumberGroupByUser(@Param("period") Period period);

    List<ProjectStatisticsBaseVO> selectProjectNumberGroupByAddress();

    List<Integer> getAllTenants();
}