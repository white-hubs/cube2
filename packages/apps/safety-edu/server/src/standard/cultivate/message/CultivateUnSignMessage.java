package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTaskUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Appet
 * @Description 培训任务
 * @date 2023-04-18 15:08:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CultivateUnSignMessage {

    public static final String TOPIC = "CULTIVATE_USER_UN_SIGN";

    private CultivateTask task;
    private List<CultivateTaskUser> users;
}