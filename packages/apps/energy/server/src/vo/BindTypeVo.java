package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.util.List;

@Data
public class BindTypeVo {
    private Integer level;
    private String instrumentType;
    private String code;
    private String name;
    private List<BindTypeVo> children;
}
