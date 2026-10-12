package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamTaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamTaskService extends ServiceImpl<ExamTaskMapper, ExamTaskPO> {

}
