package com.guhecloud.cube.srv.gen.tag.tag.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PropertyTagRefPageRequest extends TagRefPageRequest {
    private String typeId;

}
