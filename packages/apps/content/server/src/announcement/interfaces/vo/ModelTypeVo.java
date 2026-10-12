package com.guhecloud.cube.srv.gen.content.management.server.announcement.interfaces.vo;

import com.guhecloud.cube.srv.gen.content.management.server.announcement.enumeration.InformationTypeEnum;
import lombok.Data;

@Data
public class ModelTypeVo {
    private Long id;
    //模块类型
    private String modelType;
    //类型名称
    private String typeName;
    //一级类型
    private InformationTypeEnum oneLevelType;
}
