package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.aike;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiKeResponse {
    private Boolean success;
    private int resultCode;
    private String message;
    private List<Object> data;
}
