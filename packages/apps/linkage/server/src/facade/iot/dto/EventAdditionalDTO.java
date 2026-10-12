package com.guhecloud.cube.biz.linkage.server.facade.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventAdditionalDTO {

    /**
     * 附件信息
     */
    private EventAdditionalPicFileDTO additionalWithPicFileDes;
}
