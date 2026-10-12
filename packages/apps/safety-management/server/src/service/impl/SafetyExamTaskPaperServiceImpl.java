package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.SafetyExamTaskPaperMapper;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyExamTaskPaperPO;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyExamTaskPaperService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SafetyExamTaskPaperServiceImpl extends ServiceImpl<SafetyExamTaskPaperMapper, SafetyExamTaskPaperPO> implements SafetyExamTaskPaperService {

    @Override
    public List<SafetyExamTaskPaperPO> getPaperByTaskIds(Collection<Long> taskIds) {
        return CollectionUtils.isEmpty(taskIds) ? Collections.emptyList()
                                                : this.baseMapper.selectList(Wrappers.<SafetyExamTaskPaperPO>lambdaQuery()
                                                                                     .in(SafetyExamTaskPaperPO::getExamTaskId, taskIds));
    }
}
