package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPaperPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamTaskPaperMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ExamTaskPaperService extends ServiceImpl<ExamTaskPaperMapper, ExamTaskPaperPO> {

    public List<ExamTaskPaperPO> getExamTaskPaperBaseInfoVOS(Set<Long> examTaskIds) {
        return this.baseMapper.selectExamTaskPaperBaseByExamTaskIds(examTaskIds);
    }
}
