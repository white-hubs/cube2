package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.UserExamTaskVO;
import lombok.Data;

import java.util.List;

/**
 * @author Appet
 * @Description 培训任务
 * @date 2023-04-18 15:08:36
 */
@Data
public class CultivateTaskDetailVO extends CultivateTaskVO {

    private List<CultivateTaskUserVO> cultivateUsers;

    private List<CultivateCoursewarVO> cultivateCoursewares;

    private UserExamTaskVO examTaskVO;

}