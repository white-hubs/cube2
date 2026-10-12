package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.ExamTaskMapper;
import com.guhecloud.cube.srv.gen.safety.management.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.management.service.ExamTaskService;
import org.springframework.stereotype.Service;

@Service(value = "safetyExamTaskService")
public class ExamTaskServiceImpl extends ServiceImpl<ExamTaskMapper, ExamTaskPO> implements ExamTaskService {
}
