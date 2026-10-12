package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPaperPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface ExamTaskPaperMapper extends BaseMapper<ExamTaskPaperPO> {

    List<ExamTaskPaperPO> selectExamTaskPaperBaseByExamTaskIds(@Param("examTaskIds") Set<Long> examTaskIds);
}
