package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.message.model;

import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity.HeadquartersCultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity.HeadquartersCultivateTaskMarket;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateCoursewarVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.enumeration.CultivateOperationEnum;
import lombok.Data;

import java.util.List;

/**
 * 培训任务
 *
 * @author lanchun
 * @date 2024/4/10 15:01
 */
@Data
public class HeadquartersCultivateTaskMessage {
    private Long id;
    private HeadquartersCultivateTask task;
    private List<HeadquartersCultivateTaskMarket> markets;
    /**
     * 操作类型
     */
    private CultivateOperationEnum operationType;
    private List<HeadquartersCultivateCoursewarVO> headquartersCultivateCoursewars;
}