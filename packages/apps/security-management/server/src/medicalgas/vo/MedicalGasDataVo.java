package com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicalGasDataVo {
    private String name;
    private int num;
    private BigDecimal ratio = BigDecimal.ZERO;
    private String timeFlag;
}
