package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserRecordPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamTaskUserRecordMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperQuestionVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExamTaskUserRecordService extends ServiceImpl<ExamTaskUserRecordMapper, ExamTaskUserRecordPO> {

    /**
     * 获取用户考试记录的试卷信息
     *
     * @param userExamTaskRecordId
     *
     * @return
     */
    public ExamPaperVO getUserTaskRecordPaperInfo(Long userExamTaskRecordId) {
        ExamTaskUserRecordPO examTaskUserRecordPO = this.baseMapper.selectById(userExamTaskRecordId);
        Assert.isNotFound(examTaskUserRecordPO, "考试记录不存在");
        ExamPaperVO examPaperVO = JsonUtils.deserialize(examTaskUserRecordPO.getExamPaperContent(), ExamPaperVO.class);
        List<ExamPaperQuestionVO> examPaperQuestionVOS = new ArrayList<>();
        examPaperVO.getQuestionCategories().stream().forEach(examPaperQuestionCategoryVO ->
                                                                     examPaperQuestionVOS.addAll(examPaperQuestionCategoryVO.getExamQuestions())
                                                            );
        examPaperVO.setExamQuestions(examPaperQuestionVOS);
        return examPaperVO;
    }

    public Optional<ExamTaskUserRecordPO> getUserTaskLastRecord(Long taskId, String userIdentity) {
        List<ExamTaskUserRecordPO> examTaskUserRecordPOS = this.baseMapper
                .selectList(Wrappers.<ExamTaskUserRecordPO>lambdaQuery()
                                    .eq(ExamTaskUserRecordPO::getExamTaskId, taskId)
                                    .eq(ExamTaskUserRecordPO::getUserId, userIdentity)
                           );
        if (CollectionUtils.isEmpty(examTaskUserRecordPOS)) {
            return Optional.empty();
        }
        return Optional.of(examTaskUserRecordPOS.get(0));
    }
}
