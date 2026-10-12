package com.guhecloud.cube.srv.gen.safety.education.server.common.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway.query.CoursewareLibraryQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.CoursewareLibraryPO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.infrastructure.repository.mapper.CoursewareLibraryMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.CoursewareLibraryVO;
import com.guhecloud.platform.database.util.PagingUtils;
import org.springframework.stereotype.Service;

@Service
public class CoursewareLibraryService extends ServiceImpl<CoursewareLibraryMapper, CoursewareLibraryPO> {

    public Page<CoursewareLibraryVO> getCoursewareLibraryPage(CoursewareLibraryQuery coursewareLibraryQuery) {
        return this.baseMapper.selectCoursewareLibraryPage(PagingUtils.convert(coursewareLibraryQuery), coursewareLibraryQuery);
    }
}
