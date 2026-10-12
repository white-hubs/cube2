package com.guhecloud.cube.srv.gen.tag.tag.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class TagRefRemoveRequest {
    private Long tagId;
    private List<Long> refIds;
}
