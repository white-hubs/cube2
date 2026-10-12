package com.guhecloud.cube.srv.gen.tag.tag.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LooseWorkRefPageRequest extends TagRefPageRequest {
    private String status;
}
