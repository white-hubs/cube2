package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.query.ExamQuestionBankQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamQuestionBankPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamQuestionBankVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExamQuestionBankMapper extends BaseMapper<ExamQuestionBankPO> {

    Page<ExamQuestionBankVO> selectExamQuestionBankPage(Page page, @Param("query") ExamQuestionBankQuery examQuestionBankQuery);
}
