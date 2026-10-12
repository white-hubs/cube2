package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.model;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTaskUser;
import lombok.Data;

import java.util.List;

/**
 * @author Appet
 * @Description 培训任务
 * @date 2023-04-18 15:08:36
 */
@Data
public class CultivateTaskMessage {
    private CultivateTask task;
    private List<CultivateTaskUser> users;
}