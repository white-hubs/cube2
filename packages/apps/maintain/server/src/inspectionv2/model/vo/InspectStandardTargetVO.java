package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectTargetInfoDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.KeyValueDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class InspectStandardTargetVO extends InspectTargetInfoDTO {

    /**
     * 记录ID
     */
    private Long recordId;
    /**
     * 关联对象集合
     */
    private List<KeyValueDTO> childTargetNameList;

    /**
     * 标准详情
     */
    private List<InspectStandardVO> standardVOS;

}
