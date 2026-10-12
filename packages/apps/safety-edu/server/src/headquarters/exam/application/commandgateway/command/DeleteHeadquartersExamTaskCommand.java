package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteHeadquartersExamTaskCommand {

    /**
     * 任务集合
     */
    private Set<Long> examTaskIds;
}
