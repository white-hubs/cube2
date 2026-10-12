package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UnisInsightBaseResponse<T> {

    @JsonProperty("error_code")
    private String errorCode;

    private String message;

    private T data;

    public boolean succeed() {
        return "0000000000".equals(errorCode);
    }
}
