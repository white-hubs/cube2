package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Appet
 * @Description 培训任务
 * @date 2023-04-18 15:08:36
 */
@Data
public class HeadquartersCultivateTaskDetailVO extends HeadquartersCultivateTaskVO {

    private List<HeadquartersCultivateTaskMarketVO> cultivateTaskMarkets;

    private List<HeadquartersCultivateCoursewarVO> cultivateCoursewares;

}