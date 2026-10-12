package com.guhecloud.cube.srv.gen.loose.works.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProjectDataForReportVO {
    private Long nowTotal;
    private Long ringTotal;
    private Long sameTotal;

    List<AbnormalProjectReportVO> abnormalProjectReportVOS;
}
