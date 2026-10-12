package com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model;

import lombok.Data;

import java.util.List;

@Data
public class WaxPage<T> {
    private int total;
    private int pageNum;
    private int pageSize;
    private int totalPage;
    private List<T> rows;
}
