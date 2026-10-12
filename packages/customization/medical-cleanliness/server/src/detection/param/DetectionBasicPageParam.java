package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DetectionBasicPageParam extends Paging {
    private String keyword;
}
