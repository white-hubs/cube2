package com.guhecloud.cube.srv.gen.safety.education.server.common.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway.query.CoursewareLibraryQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.CoursewareLibraryPO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.CoursewareLibraryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CoursewareLibraryMapper extends BaseMapper<CoursewareLibraryPO> {

    /**
     * 课件分页列表
     *
     * @param page
     * @param coursewareLibraryQuery
     *
     * @return
     */
    Page<CoursewareLibraryVO> selectCoursewareLibraryPage(Page page, @Param("query") CoursewareLibraryQuery coursewareLibraryQuery);
}
