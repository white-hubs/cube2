package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author appet
 * @since 2021-06-15
 */
@Data
public class InspectStandardTypeReferCommand {

    private String targetCategory;

    private String targetType;

    //默认巡检标准（1.是；0.否）
    private String defaultStandard;

    private Long standardId;

}
