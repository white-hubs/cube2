package com.guhecloud.cube.srv.gen.adapter.onecard.server.dto.cbd.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BasePageResponse<T> {
    private String code;
    private PageInfo data;
    private String msg;
    private T records;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PageInfo {
        private Long total;
        private Integer size;
        private Integer current;
        private Integer pages;
    }
}
