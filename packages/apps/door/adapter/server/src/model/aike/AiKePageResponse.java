package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.aike;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiKePageResponse {
    private Boolean success;
    private int resultCode;
    private String message;
    private Page data;

    @Data
    public static class Page {
        private long page_num;
        private long page_size;
        private long total;
        private List<JSONObject> list;
    }
}
