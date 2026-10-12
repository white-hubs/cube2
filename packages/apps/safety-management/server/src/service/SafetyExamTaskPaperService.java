package com.guhecloud.cube.srv.gen.safety.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyExamTaskPaperPO;

import java.util.Collection;
import java.util.List;

public interface SafetyExamTaskPaperService extends IService<SafetyExamTaskPaperPO> {

    List<SafetyExamTaskPaperPO> getPaperByTaskIds(Collection<Long> taskIds);
}
