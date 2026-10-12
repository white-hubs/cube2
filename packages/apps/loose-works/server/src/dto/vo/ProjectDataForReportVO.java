package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectDataForReportVO {
    private Long nowTotal;
    private Long ringTotal;
    private Long sameTotal;

    List<AbnormalProjectReportVO> abnormalProjectReportVOS;
}
