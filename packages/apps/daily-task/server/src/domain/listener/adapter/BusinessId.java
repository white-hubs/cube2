package com.guhecloud.cube.biz.daily.task.domain.listener.adapter;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pengjian
 * @date 2022/8/31 15:52
 * @description
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
