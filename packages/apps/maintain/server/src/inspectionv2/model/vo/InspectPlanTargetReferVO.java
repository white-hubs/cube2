package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class InspectPlanTargetReferVO extends InspectTargetInfoDTO {

    /**
     * 记录ID
     */
    private Long recordId;

    /**
     * 标准详情
     */
    private List<InspectStandardVO> standardVOS;

}
