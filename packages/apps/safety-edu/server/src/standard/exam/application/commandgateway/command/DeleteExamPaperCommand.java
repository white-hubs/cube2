package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 试卷删除命令
 *
 * @author lanchun
 * @date 2023/4/19 11:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteExamPaperCommand {

    /**
     * 试卷主键集合
     */
    private Set<Long> examPaperIds;
}
