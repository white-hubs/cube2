package com.guhecloud.cube.srv.gen.tag.tag.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TagRefRequest {
    @NotNull(message = "标签ID不能为空")
    private Long tagId;
    @NotNull(message = "引用不能为空")
    private List<RefRequest> refRequests;
}
