package com.guhecloud.cube.srv.gen.tag.tag.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceTagRefPageRequest extends TagRefPageRequest {
    private String anyMatchedAreaId;
    private Long productId;
    private List<String> tagCodes;
}
