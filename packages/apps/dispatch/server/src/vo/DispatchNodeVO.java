package com.guhecloud.cube.biz.dispatch.server.vo;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author Appet
 * @Description 节点模型
 * @date 2022-07-26 10:08:36
 */
@Data
@SuperBuilder
public class DispatchNodeVO {
    private String code;//节点编号
    private String name;//节点名称
    private String type;//节点类型
    private String subType;//节点子类型
    private Integer sort;//排序号
    private List<String> parentCodes;//父级节点编号
    private List<String> childCodes;//子节点编号
}