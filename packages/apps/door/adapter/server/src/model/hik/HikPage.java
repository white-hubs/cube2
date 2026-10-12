package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik;

import lombok.Data;

import java.util.List;

@Data
public class HikPage<T> {
    private int total;
    private int pageNo;
    private int pageSize;
    private List<T> list;
}
