package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik;

import lombok.Data;

import java.util.List;

@Data
public class HikPage2<T> {
    private int total;
    private int totalPage;
    private int page;
    private int pageSize;
    private List<T> rows;
}
