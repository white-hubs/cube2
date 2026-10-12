package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 19:07
 * @Description:
 */
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Getter
public class BusinessId {
    private String businessKey;
    @NotBlank
    private String businessType;
}

