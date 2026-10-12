package com.guhecloud.cube.srv.gen.tag.tag.dto.request;

import com.guhecloud.cube.srv.gen.tag.tag.enums.TagRefTypeEnum;
import lombok.Data;

@Data
public class RefRequest {
    private TagRefTypeEnum tagRefType;
    private Long refId;
}
