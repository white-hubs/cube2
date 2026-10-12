package com.guhecloud.cube.biz.ouaa.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/10/9 14:43
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImgCodeDTO {
    private String key;
    private String code;
}
