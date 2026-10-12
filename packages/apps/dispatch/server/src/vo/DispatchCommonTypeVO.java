package com.guhecloud.cube.biz.dispatch.server.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author Appet
 * @Description 通用类型
 * @date 2022-08-08 10:08:36
 */
@Data
@Builder
public class DispatchCommonTypeVO {
    //编号
    private String code;
    //名称
    private String name;

}