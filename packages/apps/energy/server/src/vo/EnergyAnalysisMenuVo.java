package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import lombok.Data;

import java.util.List;

@Data
public class EnergyAnalysisMenuVo {
    private Integer level;
    private String code;
    private String name;
    private List<BindTypeVo> children;
}
