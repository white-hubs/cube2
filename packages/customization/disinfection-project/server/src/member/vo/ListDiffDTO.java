package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import lombok.Data;

import java.util.List;

@Data
public class ListDiffDTO {
    private final List<Long> addList;
    private final List<Long> removeList;
}
