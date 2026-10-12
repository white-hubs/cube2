package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 对象区域信息
 * @date 2023/11/9 14:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ObjAreaVO {
    private String state;
    private String areaId;
    private String areaName;
}
