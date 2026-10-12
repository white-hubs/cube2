package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.querygateway.query.ExamTaskUserRecordStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.querygateway.query.HeadquartersExamTaskQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.interfaces.vo.ExamTaskUserRecordExportVO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.interfaces.vo.ExamTaskUserRecordVO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.interfaces.vo.HeadquartersExamTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HeadquartersExamTaskMapper extends BaseMapper<HeadquartersExamTaskPO> {

    Page<HeadquartersExamTaskVO> selectExamTaskPage(Page page, @Param("query") HeadquartersExamTaskQuery query);

    Page<ExamTaskUserRecordVO> selectExamTaskUserRecordPage(Page page, @Param("tenant") Integer tenant,
                                                            @Param("query") ExamTaskUserRecordStatisticsQuery query
                                                           );

    List<ExamTaskUserRecordExportVO> selectExamUserRecordExport(@Param("tenant") Integer tenant,
                                                                @Param("query") ExamTaskUserRecordStatisticsQuery query
                                                               );
}
