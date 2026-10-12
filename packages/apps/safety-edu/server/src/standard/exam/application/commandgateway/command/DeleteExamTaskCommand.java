package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteExamTaskCommand {

    /**
     * 任务集合
     */
    private Set<Long> examTaskIds;
}
