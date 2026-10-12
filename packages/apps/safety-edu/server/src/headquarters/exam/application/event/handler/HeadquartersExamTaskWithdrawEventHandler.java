package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.event.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.HeadquartersExamTaskPaperService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.event.HeadquartersExamTaskWithdrawEvent;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskPaperPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.ExamTaskCommandService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.DeleteExamTaskCommand;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 总部任务撤回事件监听
 *
 * @author lanchun
 * @date 2024/4/10 13:55
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersExamTaskWithdrawEventHandler {

    private final HeadquartersExamTaskPaperService headquartersExamTaskPaperService;

    private final ExamTaskCommandService examTaskCommandService;

    @Transactional(rollbackFor = Exception.class)
    @EventListener
    public void deletedExamTaskRefInfo(final HeadquartersExamTaskWithdrawEvent event) {
        List<HeadquartersExamTaskPaperPO> taskPaperPOS = this.headquartersExamTaskPaperService.list(Wrappers
                                                                                                            .<HeadquartersExamTaskPaperPO>lambdaQuery()
                                                                                                            .eq(HeadquartersExamTaskPaperPO::getHeadquartersExamTaskId, event
                                                                                                                    .getExamTask()
                                                                                                                    .getId()));
        if (!CollectionUtils.isEmpty(taskPaperPOS)) {
            DatabaseContextHolder.softDelete(HeadquartersExamTaskPaperPO.class, taskPaperPOS);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @EventListener
    public void deletedMarketExamTaskInfo(final HeadquartersExamTaskWithdrawEvent event) {
        DeleteExamTaskCommand deleteExamTaskCommand = new DeleteExamTaskCommand(Collections.singleton(event.getExamTask().getId()));
        this.examTaskCommandService.deleteExamTaskBySourceId(deleteExamTaskCommand);
    }
}
