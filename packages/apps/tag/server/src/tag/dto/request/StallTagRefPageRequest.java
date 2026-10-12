package com.guhecloud.cube.srv.gen.tag.tag.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StallTagRefPageRequest extends TagRefPageRequest {
    private String type;
    private Boolean enabled;
    private Long areaId;
}
