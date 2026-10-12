package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeCountVO {
    private String productType;
    private Long count;
}
