package com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/5/19 17:26
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelViewVO {
    private Long id;
    private String name;
    private String deviceGenreId;
}
